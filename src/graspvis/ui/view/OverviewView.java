package graspvis.ui.view;

import graspvis.controller.FrameController;
import graspvis.exception.AlgorithmException;
import graspvis.exception.SystemException;
import graspvis.logic.algorithm.Algorithm;
import graspvis.logic.algorithm.FDLayout;
import graspvis.logic.drawer.view.ViewDrawer;
import graspvis.logic.drawer.view.OverviewViewDrawer;
import graspvis.model.BasicGraph;
import graspvis.model.ElementType;
import graspvis.model.Graph;
import graspvis.model.Highlight;
import graspvis.model.Node;
import graspvis.model.RelationshipType;
import graspvis.model.Spring;

import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JOptionPane;

import processing.core.PApplet;
import processing.core.PFont;
import controlP5.Button;
import controlP5.CColor;
import controlP5.ControlEvent;
import controlP5.ControlListener;
import controlP5.ControlP5;
import controlP5.Group;
import controlP5.Slider;
import controlP5.Textlabel;
import controlP5.Toggle;

public class OverviewView extends PApplet{
	private FrameController controller;
	
	boolean savePDF = false;

	// dragged node
	private Node selectedNode = null;
	private Node highlightedNode = null;

	private PFont font;
	//private PFont fontSmaller;

	private Graph graph;

	private Algorithm layoutAlgorithm;

	private ViewDrawer drawer;

	private ControlPanel cpanel;

	boolean layer = false;

	private Graph drawGraph;

	public OverviewView(Graph graph, FrameController controller) {
		this.graph = graph;
		this.controller = controller;
		//((PGraph) this.graph).addObserver(this);
		drawGraph = new BasicGraph();
	}

	public void setGraph(Graph graph) {
		this.graph = graph;
		drawGraph.clear();
		cpanel.setGraph(this.graph);
		cpanel.setDrawGraph(drawGraph);
	}

	public void setup() {
		frameRate(20);
		background(255);

		font = createFont("Arial", 12);

		smooth();
		noStroke();

		cpanel = new ControlPanel(this, graph, drawGraph);

		layoutAlgorithm = new FDLayout(drawGraph);

		drawer = new OverviewViewDrawer(this, drawGraph);
	}

	public void draw() {
		if (savePDF) beginRecord(PDF, timestamp()+".pdf");
		background(255);
		textFont(font);
		try {
			((FDLayout) layoutAlgorithm).setMaxX(width-15);
			((FDLayout) layoutAlgorithm).setMaxY(height-15);
			layoutAlgorithm.run();
		} catch (AlgorithmException e) {
			e.printStackTrace();
		}

		if (selectedNode != null) {
			selectedNode.getPosition().x = mouseX;
			selectedNode.getPosition().y = mouseY;
		}

		try {
			drawer.draw();
		} catch (SystemException e) {
			e.printStackTrace();
		}

		if (savePDF) {
			savePDF = false;
			println("saving to pdf finishing");
			endRecord();
		}
	}

	public void mousePressed() {
		Node n = drawGraph.getNode(mouseX, mouseY);
		selectedNode = n;
		if (n != null) {
			controller.showElement(n, this);
			controller.highlightElement(n);
			drawGraph.highlight((Highlight)n);
			if (highlightedNode == selectedNode) {
				controller.unhighlight();
			}
			highlightedNode = n;
		}
	}

	public void mouseReleased() {
		if (selectedNode != null) {
			selectedNode = null;
		}
	}


	public void keyPressed() {
		if(key=='s' || key=='S') saveFrame(timestamp()+"_##.png"); 

		if(key=='p' || key=='P') {
			savePDF = true; 
			println("saving to pdf - starting (this may take some time)");
		}
	}

	public void highlight(Node node) {
		drawGraph.highlight((Highlight) node);
	}
	
	public void unhighlight() {
		drawGraph.unhighlight();
	}
	
	private String timestamp() {
		return String.format("%1$ty%1$tm%1$td_%1$tH%1$tM%1$tS", Calendar.getInstance());
	}

}

/**
 * Control panel class
 */

class ControlPanel implements ControlListener {
	private static final String KIND_PREFIX = "kind-";
	private static final String CONTAINTMENT_SLIDER = "containtmentSlider";
	private static final String EXTENTION_SLIDER = "extentionSlider";
	private static final String INSTANTIATION_SLIDER = "instantiationSlider";
	private static final String CAUSALITY_SLIDER = "causalitySlider";
	private static final String CONNECTION_SLIDER = "connectionSlider";
	private final int BACKGROUND = 35;
	private final int HEIGHT = 130;
	private final int WIDTH = 400;
	private final int ALPHA = 50;

	private PApplet applet;
	private ControlP5 cp5;
	private Group cPanel;

	// layer button
	private Button btnLayer;
	private Textlabel lblLayer;

	// Component button
	private Button btnComponent;
	private Textlabel lblComponent;

	// Connector button
	private Button btnConnector;
	private Textlabel lblConnector;

	// template button
	private Button btnTemplate;
	private Textlabel lblTemplate;

	// template button
	private Button btnRationale;
	private Textlabel lblRationale;

	//Relationship buttons
	// connection button
	private Toggle btnConnection;
	private Textlabel lblConnection;

	// causality button
	private Toggle btnCausality;
	private Textlabel lblCausality;

	// Instantiation button
	private Toggle btnInstatiation;
	private Textlabel lblInstatiation;

	// extension button
	private Toggle btnExtension;
	private Textlabel lblExtension;

	// containment button
	private Toggle btnContainment;
	private Textlabel lblContainment;


	private int x = 10, y = 20;
	private int xCbe = 5, yCbe = 5;
	private int xCbr = xCbe + 90, yCbr = yCbe;

	private Graph graph;
	private Graph drawGraph;

	/**
	 * Length of connection relationship
	 */
	private float connectionLength = RelationshipType.CONNECTION.getLength();
	/**
	 * Length of containment relationship
	 */
	private float containmentLength = RelationshipType.CONTAINMENT.getLength();
	/**
	 * Length of instantiation relationship
	 */
	private float instantiationLength = RelationshipType.INSTANTIATION.getLength();
	/**
	 * Length of causality relationship
	 */
	private float causalityLength = RelationshipType.CAUSALITY.getLength();
	/**
	 * Length of extension relationship
	 */
	private float extensionLength = RelationshipType.EXTENSION.getLength();

	// button colors
	private CColor on;
	private CColor onWithKind;
	private CColor off;

	// Button states
	private ButtonStates layerState = ButtonStates.OFF;
	private ButtonStates componentState = ButtonStates.OFF;
	private ButtonStates connectorState = ButtonStates.OFF;
	private ButtonStates templateState = ButtonStates.OFF;
	private ButtonStates rationaleState = ButtonStates.OFF;

	public ControlPanel(PApplet theApplet, Graph theGraph, Graph theDrawGraph) {
		applet = theApplet;
		graph = theGraph;
		drawGraph = theDrawGraph;

		cp5 = new ControlP5(applet);
		cp5.addListener(this);

		initColors();

		initGUI();
	}

	public void resetFields() {
		connectionLength = RelationshipType.CONNECTION.getLength();
		containmentLength = RelationshipType.CONTAINMENT.getLength();
		instantiationLength = RelationshipType.INSTANTIATION.getLength();
		causalityLength = RelationshipType.CAUSALITY.getLength();
		extensionLength = RelationshipType.EXTENSION.getLength();
		
		layerState = ButtonStates.OFF;
		componentState = ButtonStates.OFF;
		connectorState = ButtonStates.OFF;
		templateState = ButtonStates.OFF;
		rationaleState = ButtonStates.OFF;
		
		btnLayer.setColor(off);
		btnComponent.setColor(off);
		btnConnector.setColor(off);
		btnTemplate.setColor(off);
		btnRationale.setColor(off);
		
		btnConnection.setValue(false);
		btnCausality.setValue(false);
		btnContainment.setValue(false);
		btnExtension.setValue(false);
		btnInstatiation.setValue(false);
	}
	
	/**
	 * Initiate the colors of P5 GUI, this method has to be called before method {@code initGUI()}
	 */
	private void initColors() {
		// init colors
		off = new CColor();
		off.setBackground(applet.color(244));
		off.setForeground(applet.color(244));
		off.setActive(applet.color(50));

		// init colors
		on = new CColor();
		on.setBackground(applet.color(50));
		on.setForeground(applet.color(50));
		on.setActive(applet.color(196, 196, 35));

		onWithKind = new CColor();
		onWithKind.setBackground(applet.color(196, 196, 35));
		onWithKind.setForeground(applet.color(196, 196, 35));
		onWithKind.setActive(applet.color(244));
	}

	/**
	 * Initiate CP5 GUI components and set their position on the screen
	 */
	private void initGUI() {
		cPanel = cp5.addGroup("cPanel")
				.setLabel("Control Panel")
				.setPosition(x, y)
				.setBackgroundHeight(HEIGHT)
				.setWidth(WIDTH)
				.setBackgroundColor(applet.color(BACKGROUND, ALPHA));

		// Elements buttons
		initToggleElements();

		// Relationship buttons
		initToggleRelationships();

		// Sliders
		initSliders();
	}

	private void initToggleElements() {
		float yLblGap = 5;
		float xLblGap = 20;

		btnLayer = cp5.addButton("btnLayer")
				.setPosition(xCbe, yCbe)
				.setLabelVisible(false)
				.setSize(20, 20)
				.setColor(off)
				.setGroup(cPanel);

		lblLayer = cp5.addTextlabel("lblLayer")
				.setPosition(xCbe + xLblGap, yCbe + yLblGap)
				.setColor(applet.color(20))
				.setText("LAYER")
				.setGroup(cPanel);

		yCbe += 25;
		btnComponent = cp5.addButton("btnComponent")
				.setPosition(xCbe, yCbe)
				.setLabelVisible(false)
				.setSize(20, 20)
				.setColor(off)
				.setGroup(cPanel);

		lblComponent = cp5.addTextlabel("lblComponent")
				.setPosition(xCbe + xLblGap, yCbe + yLblGap)
				.setColor(applet.color(20))
				.setText("COMPONENT")
				.setGroup(cPanel);

		yCbe += 25;
		btnConnector = cp5.addButton("btnConnector")
				.setPosition(xCbe, yCbe)
				.setLabelVisible(false)
				.setSize(20, 20)
				.setColor(off)
				.setGroup(cPanel);

		lblConnector = cp5.addTextlabel("lblConnector")
				.setPosition(xCbe + xLblGap, yCbe + yLblGap)
				.setColor(applet.color(20))
				.setText("CONNECTOR")
				.setGroup(cPanel);

		yCbe += 25;
		btnTemplate = cp5.addButton("btnTemplate")
				.setPosition(xCbe, yCbe)
				.setLabelVisible(false)
				.setSize(20, 20)
				.setColor(off)
				.setGroup(cPanel);

		lblTemplate = cp5.addTextlabel("lblTemplate")
				.setPosition(xCbe + xLblGap, yCbe + yLblGap)
				.setColor(applet.color(20))
				.setText("TEMPLATE")
				.setGroup(cPanel);

		yCbe += 25;
		btnRationale = cp5.addButton("btnRationale")
				.setPosition(xCbe, yCbe)
				.setLabelVisible(false)
				.setSize(20, 20)
				.setColor(off)
				.setGroup(cPanel);

		lblRationale = cp5.addTextlabel("lblRationale")
				.setPosition(xCbe + xLblGap, yCbe + yLblGap)
				.setColor(applet.color(20))
				.setText("RATIONALE")
				.setGroup(cPanel);
	}

	private void initToggleRelationships() {
		float yLblGap = 5;
		float xLblGap = 20;
		btnConnection = cp5.addToggle("btnConnection")
				.setPosition(xCbr, yCbr)
				.setSize(20, 20)
				.setLabelVisible(false)
				.setColor(off)
				.setGroup(cPanel);

		lblConnection = cp5.addTextlabel("lblConnection")
				.setPosition(xCbr + xLblGap, yCbr + yLblGap)
				.setColor(applet.color(20))
				.setText("CONNECTION")
				.setGroup(cPanel);

		yCbr += 25;
		btnCausality = cp5.addToggle("btnCausality")
				.setPosition(xCbr, yCbr)
				.setSize(20, 20)
				.setLabelVisible(false)
				.setColor(off)
				.setGroup(cPanel);

		lblCausality = cp5.addTextlabel("lblCausality")
				.setPosition(xCbr + xLblGap, yCbr + yLblGap)
				.setColor(applet.color(20))
				.setText("CAUSALITY")
				.setGroup(cPanel);

		yCbr += 25;
		btnInstatiation = cp5.addToggle("btnInstatiation")
				.setPosition(xCbr, yCbr)
				.setSize(20, 20)
				.setLabelVisible(false)
				.setColor(off)
				.setGroup(cPanel);

		lblInstatiation = cp5.addTextlabel("lblInstatiation")
				.setPosition(xCbr + xLblGap, yCbr + yLblGap)
				.setColor(applet.color(20))
				.setText("INSTANTIATION")
				.setGroup(cPanel);

		yCbr += 25;
		btnExtension = cp5.addToggle("btnExtension")
				.setPosition(xCbr, yCbr)
				.setSize(20, 20)
				.setLabelVisible(false)
				.setColor(off)
				.setGroup(cPanel);

		lblExtension = cp5.addTextlabel("lblExtension")
				.setPosition(xCbr + xLblGap, yCbr + yLblGap)
				.setColor(applet.color(20))
				.setText("EXTENSION")
				.setGroup(cPanel);

		yCbr += 25;
		btnContainment = cp5.addToggle("btnContainment")
				.setPosition(xCbr, yCbr)
				.setSize(20, 20)
				.setLabelVisible(false)
				.setColor(off)
				.setGroup(cPanel);

		lblContainment = cp5.addTextlabel("lblContainment")
				.setPosition(xCbr + xLblGap, yCbr + yLblGap)
				.setColor(applet.color(20))
				.setText("CONTAINMENT")
				.setGroup(cPanel);
	}

	private void initSliders() {
		cp5.addSlider(CONNECTION_SLIDER)
		.setPosition(200, 10)
		.setValue(connectionLength)
		.setColorCaptionLabel(applet.color(10))
		.setCaptionLabel("Connection Length")
		.setRange(5, 200)
		.setGroup(cPanel);

		cp5.addSlider(CAUSALITY_SLIDER)
		.setPosition(200, 35)
		.setColorCaptionLabel(applet.color(10))
		.setCaptionLabel("Causality Length")
		.setValue(causalityLength)
		.setRange(5, 200)
		.setGroup(cPanel);

		cp5.addSlider(INSTANTIATION_SLIDER)
		.setPosition(200, 60)
		.setRange(5, 200)
		.setColorCaptionLabel(applet.color(10))
		.setValue(instantiationLength)
		.setCaptionLabel("Instantiation Length")
		.setGroup(cPanel);

		cp5.addSlider(EXTENTION_SLIDER)
		.setPosition(200, 85)
		.setRange(5, 200)
		.setColorCaptionLabel(applet.color(10))
		.setValue(extensionLength)
		.setCaptionLabel("Extention Length")
		.setGroup(cPanel);

		cp5.addSlider(CONTAINTMENT_SLIDER)
		.setPosition(200, 110)
		.setRange(5, 200)
		.setColorCaptionLabel(applet.color(10))
		.setValue(containmentLength)
		.setCaptionLabel("Containment Length")
		.setGroup(cPanel);
	}

	public void controlEvent(ControlEvent theEvent) {
		// elements
		if (theEvent.isFrom(btnLayer)) {
			elementHandler(ElementType.LAYER, btnLayer, layerState);
		}
		if (theEvent.isFrom(btnComponent)) {
			elementHandler(ElementType.COMPONENT, btnComponent, componentState);
		}
		if (theEvent.isFrom(btnConnector)) {
			elementHandler(ElementType.CONNECTOR, btnConnector, connectorState);
		}
		if (theEvent.isFrom(btnTemplate)) {
			elementHandler(ElementType.TEMPLATE, btnTemplate, templateState);
		}
		if (theEvent.isFrom(btnRationale)) {
			elementHandler(ElementType.RATIONALE, btnRationale, rationaleState);
		}

		// relationship
		// connection
		addRelationship(btnConnection, RelationshipType.CONNECTION);
		// containment
		addRelationship(btnContainment, RelationshipType.CONTAINMENT);
		// Instantiation
		addRelationship(btnInstatiation, RelationshipType.INSTANTIATION);
		// Causality
		addRelationship(btnCausality, RelationshipType.CAUSALITY);
		// Causality
		addRelationship(btnExtension, RelationshipType.EXTENSION);



		if (theEvent.isFrom(cp5.get(CONNECTION_SLIDER))) {
			Slider s = (Slider) cp5.get(CONNECTION_SLIDER);
			drawGraph.setSpringLength(RelationshipType.CONNECTION, s.getValue());
		}

		if (theEvent.isFrom(cp5.get(CAUSALITY_SLIDER))) {
			Slider s = (Slider) cp5.get(CAUSALITY_SLIDER);
			drawGraph.setSpringLength(RelationshipType.CAUSALITY, s.getValue());
		}

		if (theEvent.isFrom(cp5.get(INSTANTIATION_SLIDER))) {
			Slider s = (Slider) cp5.get(INSTANTIATION_SLIDER);
			drawGraph.setSpringLength(RelationshipType.INSTANTIATION, s.getValue());
		}

		if (theEvent.isFrom(cp5.get(EXTENTION_SLIDER))) {
			Slider s = (Slider) cp5.get(EXTENTION_SLIDER);
			drawGraph.setSpringLength(RelationshipType.EXTENSION, s.getValue());
		}

		if (theEvent.isFrom(cp5.get(CONTAINTMENT_SLIDER))) {
			Slider s = (Slider) cp5.get(CONTAINTMENT_SLIDER);
			drawGraph.setSpringLength(RelationshipType.CONTAINMENT, s.getValue());
		}
	}

	private void addRelationship(Toggle source, RelationshipType type) {
		if (source.getState()) {
			addSprings(type);
		} else {
			removeSprings(type);
		}
	}

	private void elementHandler(ElementType type, Button source, ButtonStates state) {
		switch (state) {
		case OFF:
			updateState(ButtonStates.ON, type);
			source.setColor(on);
			addNodes(type);

			// adds interfaces
			if (type == ElementType.COMPONENT || type == ElementType.CONNECTOR) {
				addInterfaces(type);
			}
			break;
		case ON:
			updateState(ButtonStates.ON_WITH_KIND, type);
			source.setColor(onWithKind);
			addKindRelationship(type);
			break;
		default:
			updateState(ButtonStates.OFF, type);
			source.setColor(off);
			removeNodes(type);

			// removes interfaces
			if (type == ElementType.COMPONENT || type == ElementType.CONNECTOR) {
				removeInterfaces(type);
			}
			removeKindRelationship(type);
			break;
		}
	}

	private void updateState(ButtonStates state, ElementType type) {
		switch (type) {
		case LAYER:
			layerState = state;
			break;
		case COMPONENT:
			componentState = state;
			break;
		case RATIONALE:
			rationaleState = state;
			break;
		case TEMPLATE:
			templateState = state;
			break;
		case CONNECTOR:
			connectorState = state;
			break;
		default:
			break;
		}
	}

	private void addKindRelationship(ElementType type) {
		// Create kind node
		String nodeId = KIND_PREFIX + type.toString();
		String springId = "";
		drawGraph.addNode(nodeId, type.toString(), ElementType.KIND);

		// create spring from kind node to every given type of node
		for (Node n : graph.getNodes()) {
			if (n.getElementType() == type) {
				springId = nodeId + "_" + n.getId() + "_" + RelationshipType.KIND.toString();
				drawGraph.addSpring(springId, nodeId, n.getId(), RelationshipType.KIND);
			}
		}
	}

	private void removeKindRelationship(ElementType type) {
		String nodeId = KIND_PREFIX + type.toString();
		drawGraph.removeNode(nodeId);
	}

	private void addNodes(ElementType type) {
		for (Node node : graph.getNodes()) {
			if (node.getElementType() == type) {
				//System.out.println("add nodes to draw graph " + node.getId());
				//dGraph.addNode(node.getId(), node.getLabel(), node.getType());
				drawGraph.addNode(node);
			}
		}
	}

	private void removeNodes(ElementType type) {
		for (Node node : graph.getNodes()) {
			if (node.getElementType() == type) {
				//System.out.println("remove nodes to draw graph");
				drawGraph.removeNode(node.getId());
			}
		}
	}

	private void addSprings(RelationshipType type) {
		for (Spring s : graph.getSprings()) {
			if (s.getRelationshipType() == type) {
				// System.out.println("add spring " + spring.getId());
				drawGraph.addSpring(s.getId(), s.getFromNode(), s.getHookFrom(),
						s.getToNode(), s.getHookTo(), s.getRelationshipType());
			}
		}
	}

	private void removeSprings(RelationshipType type) {
		for (Spring spring : graph.getSprings()) {
			if (spring.getRelationshipType() == type) {
				//System.out.println("remove spring " + spring.getId());
				drawGraph.removeSpring(spring.getId());
			}
		}
	}

	private void addInterfaces(ElementType type) {
		// search for springs with INTERFACE relationship and from node is type component
		for (Spring s : graph.getSprings()) {
			if (s.getRelationshipType() == RelationshipType.INTERFACE && s.getFromNode().getElementType() == type) {
				// adds interface
				Node i = s.getToNode();
				//dGraph.addNode(i.getId(), i.getLabel(), i.getType());
				drawGraph.addNode(i);

				// adds spring
				drawGraph.addSpring(s.getId(), s.getFromNode().getId(), i.getId(), s.getRelationshipType());
			}
		}
	}

	private void removeInterfaces(ElementType type) {
		// search for springs with INTERFACE relationship and fromNode's type is the type
		for (Spring s : graph.getSprings()) {
			if (s.getRelationshipType() == RelationshipType.INTERFACE && s.getFromNode().getElementType() == type) {
				// removes interface
				Node i = s.getToNode();
				drawGraph.removeNode(i.getId());

				// removes spring
				drawGraph.removeSpring(s.getId());
			}
		}
	}

	/**
	 * @return the graph
	 */
	public Graph getGraph() {
		return graph;
	}

	/**
	 * @param graph the graph to set
	 */
	public void setGraph(Graph graph) {
		this.graph = graph;
		resetFields();
	}

	/**
	 * @return the dGraph
	 */
	public Graph getDrawGraph() {
		return drawGraph;
	}

	/**
	 * @param dGraph the dGraph to set
	 */
	public void setDrawGraph(Graph dGraph) {
		this.drawGraph = dGraph;
	}
}

enum ButtonStates {ON, ON_WITH_KIND, OFF}