package graspvis.controller;

import grasp.lang.IArchitecture;
import graspvis.color.BasicColorScheme;
import graspvis.color.ColorMap;
import graspvis.color.ColorScheme;
import graspvis.exception.AlgorithmException;
import graspvis.exception.CompilationException;
import graspvis.exception.SystemException;
import graspvis.grasp.Executor;
import graspvis.model.BasicGraph;
import graspvis.model.BasicTree;
import graspvis.model.BasicTreeNode;
import graspvis.model.ElementType;
import graspvis.model.Graph;
import graspvis.model.Highlight;
import graspvis.model.Node;
import graspvis.model.Spring;
import graspvis.model.Tree;
import graspvis.traversal.GraphTraversal;
import graspvis.traversal.Traversal;
import graspvis.traversal.TreeTraversal;
import graspvis.ui.Frame;
import graspvis.ui.ViewConfigurationDialog;
import graspvis.ui.ViewConfigurationDialog.ViewKeys;
import graspvis.ui.view.ElementView;
import graspvis.ui.view.OverviewView;
import graspvis.util.Session;
import graspvis.util.Util;

import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import processing.core.PApplet;

public class FrameController {
	// constellation view frame
	private Frame frame;
	//private View constellationView;
	private Executor executor;
	private IArchitecture architecture;
	private Traversal dfsTraversal;
	private Traversal treeTraversal;

	private Graph graph;
	//private TreeNode tree;
	private Tree tree;
	
	private PApplet[] views;
	
	private boolean viewExists = false;
	private Object keyFile = null;
	
	public FrameController() {
		executor = new Executor();
		dfsTraversal = new GraphTraversal();
		treeTraversal = new TreeTraversal(graph);
		views = new PApplet[4];
		
		graph = new BasicGraph();
		tree = new BasicTree(new BasicTreeNode());
		
		frame = new Frame(this);
		frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		
		showViewConfigurationDialog();
		initViews();
		initColor(graph);
	}

	public void openFile() {
		getFile();
		//String file = (String) Session.getSession().get(Session.Key.SOURCE_FILE);
		String file = (String) Session.getSession().get(keyFile);
		
		if (file != null) {
			try {
				architecture = executor.execute(file);
			} catch (CompilationException e) {
				JOptionPane.showMessageDialog(frame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				Session.getSession().remove(keyFile);
				e.printStackTrace();
			}
			if (architecture != null) {
				graph.clear();
				dfsTraversal.setArchitecture(architecture);
				graph = dfsTraversal.traverse();
				initColor(graph);
				//for (PApplet view : views) {
				//	if (view instanceof OverviewView) {
				//		((OverviewView) view).setGraph(graph);
				//	}
				//}
				PApplet view = views[0];
				if (keyFile == Session.Key.VIEW_FILE) view = views[1];
				if (view instanceof OverviewView) {
					((OverviewView) view).setGraph(graph);
				}
			}
		}
	}

	public void showElement(Node node, PApplet view) {
		treeTraversal.setGraph(graph);
		try {
			tree = treeTraversal.traverse(node, 2);
			if (view instanceof ElementView) {
				ElementView v = (ElementView) view;
				v.setTree(tree);
				v.highlight(node);
				v.showTree();
			} else {
				int index = -1;
				for (int i = 0; i < views.length; i++) {
					if (views[i] instanceof ElementView) {
						index = index < 0? i: index;
						ElementView v = (ElementView) views[i];
						if (!v.isFilled()) {
							index = i;
						}
					}
				}
				
				// if index = -1 then there is no element view on the screen
				if (index >= 0) {
					ElementView v = (ElementView) views[index];
					v.setTree(tree);
					v.highlight(node);
					v.showTree();	
				}
			}
		} catch (AlgorithmException e) {
			JOptionPane.showMessageDialog(frame, e.getMessage(), AlgorithmException.ERROR_HEADER, 
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public void highlightElement(Node node) {
		graph.highlight((Highlight) node);
		for (PApplet view : views) {
			if (view instanceof ElementView) 
				((ElementView) view).highlight(node);
			if (view instanceof OverviewView)
				((OverviewView) view).highlight(node);
		}
	}
	
	public void unhighlight() {
		graph.unhighlight();
		for (PApplet view : views) {
			if (view instanceof ElementView) 
				((ElementView) view).unhighlight();
			if (view instanceof OverviewView)
				((OverviewView) view).unhighlight();
		}
	}
	
	public void configureViews() {
		showViewConfigurationDialog();
		initViews();
	}
	
	private void getFile() {
		JFileChooser fc = new JFileChooser();
		fc.addChoosableFileFilter(new FileFilter() {

			@Override
			public String getDescription() {
				return "Grasp source file";
			}

			@Override
			public boolean accept(File f) {
				return f.getName().endsWith(".grasp");
			}
		});
		int returnVal = fc.showOpenDialog(frame);
		
		keyFile = Session.Key.SOURCE_FILE;
		if (viewExists) {
			keyFile = Session.Key.VIEW_FILE;
		}

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			try {
				Session.getSession().put(keyFile, file.getCanonicalPath());
				viewExists = true;
			} catch (IOException e1) {
				Session.getSession().remove(Session.Key.SOURCE_FILE);
				JOptionPane.showMessageDialog(frame, "Error while opening file", "Error", 
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	private void addView() {
		Object keyFileArr = Session.Key.VIEW_FILES;
		Session viewSesh = Session.getSession();
		Object fileArr = viewSesh.get(keyFileArr);
		if ( fileArr != null) {
			System.out.println("fileArr not null");
		}
		
	}
	
	private Object retrieveView(int num) {
		Object obj = null;
		return obj;
		
	}
	
	private Object focusView(int num) {
		return retrieveView(num);
	}
	

	private void showViewConfigurationDialog() {
		ViewConfigurationDialog dialog = new ViewConfigurationDialog(frame, true);
		dialog.setLocationRelativeTo(frame);
		dialog.setVisible(true);
	}

	private void initViews() {
		// init views
		tree.clear();
		Map<Object, Object> viewMap = (Map<Object, Object>) Session.getSession().get(Session.Key.VIEW_CONFIGURATION);
		if (viewMap != null) {
			// section 1
			graspvis.ui.ViewConfigurationDialog.View v = (graspvis.ui.ViewConfigurationDialog.View) viewMap.get(ViewKeys.SECTION1);
			if (v == graspvis.ui.ViewConfigurationDialog.View.ELEMENT)
				views[0] = new ElementView(tree, this);
			else 
				views[0] = new OverviewView(graph, this);
			
			// section 2
			v = (graspvis.ui.ViewConfigurationDialog.View) viewMap.get(ViewKeys.SECTION2);
			if (v == graspvis.ui.ViewConfigurationDialog.View.ELEMENT)
				views[1] = new ElementView(tree, this);
			else 
				views[1] = new OverviewView(graph, this);
			
			// section 3
			v = (graspvis.ui.ViewConfigurationDialog.View) viewMap.get(ViewKeys.SECTION3);
			if (v == graspvis.ui.ViewConfigurationDialog.View.ELEMENT)
				views[2] = new ElementView(tree, this);
			else 
				views[2] = new OverviewView(graph, this);
			
			// section 4
			v = (graspvis.ui.ViewConfigurationDialog.View) viewMap.get(ViewKeys.SECTION4);
			if (v == graspvis.ui.ViewConfigurationDialog.View.ELEMENT)
				views[3] = new ElementView(tree, this);
			else 
				views[3] = new OverviewView(graph, this);
		} else {
			views[0] = new OverviewView(graph, this);
			views[1] = new ElementView(tree, this);
			views[2] = new OverviewView(graph, this);
			views[3] = new OverviewView(graph, this);	
		}
		
		for (int i = 0; i < views.length; i++) {
			frame.setViews(views[i], i+1);
		}
		
		try {
			frame.showApplet();
		} catch (SystemException e) {
			JOptionPane.showMessageDialog(frame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	// load test file, for test purposes
	private void loadFile() {
		String file = "/Users/nauval/Desktop/Grasp sources/wsn_simulator_3.grasp";
		if (file.length() > 0) {
			try {
				architecture = executor.execute(file);
			} catch (CompilationException e) {
				JOptionPane.showMessageDialog(frame, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
			if (architecture != null) {
				graph.clear();
				dfsTraversal.setArchitecture(architecture);
				graph = dfsTraversal.traverse();
				initColor(graph);
				for (PApplet view : views) {
					if (view instanceof OverviewView) {
						((OverviewView) view).setGraph(graph);
					}
				}
			}
		}
	}
	
	private void initColor(Graph graph) {
		ColorScheme elementsCScheme = initElementCScheme();
		ColorScheme interfaceCScheme = initInterfaceCScheme();

		ColorMap cMap = new ColorMap();

		// apply to color map
		cMap.setColor(ElementType.LAYER, elementsCScheme.get("color0"));
		cMap.setColor(ElementType.COMPONENT, elementsCScheme.get("color1"));
		cMap.setColor(ElementType.CONNECTOR, elementsCScheme.get("color2"));
		cMap.setColor(ElementType.RATIONALE, elementsCScheme.get("color4"));
		cMap.setColor(ElementType.TEMPLATE, elementsCScheme.get("color5"));
		cMap.setColor("FONT", elementsCScheme.get("color" + (elementsCScheme.getColors().size() - 1)));

		// map interface
		for (Node node : graph.getNodes()) {
			if (node.getElementType() == ElementType.PROVIDES 
					|| node.getElementType() == ElementType.REQUIRES) {
				if (!cMap.exist(node.getLabel())) {
					cMap.setColor(node.getLabel(), interfaceCScheme.getRandomColor());
				}
			}
		}

		generateColor(graph, cMap);
	}

	private ColorScheme initElementCScheme() {
		ColorScheme elementsCScheme = new BasicColorScheme();
		PApplet applet = Util.getPAppletUtil();
		int[] el = new int[9];
		el[0] = applet.color(178, 24, 43);
		el[1] = applet.color(214, 96, 77);
		el[2] = applet.color(244, 165, 130);
		el[3] = applet.color(253, 219, 199);
		el[4] = applet.color(209, 229, 240);
		el[5] = applet.color(146, 197, 222);
		el[6] = applet.color(67, 147, 195);
		el[7] = applet.color(33, 102, 172);
		el[8] = applet.color(244, 244, 244);

		for (int i = 0; i < el.length; i++) {
			elementsCScheme.add("color"+i, el[i]);
		}
		return elementsCScheme;
	}

	private ColorScheme initInterfaceCScheme() {
		ColorScheme interfaceCScheme = new BasicColorScheme();
		PApplet applet = Util.getPAppletUtil();
		int[] in = new int[8];
		in[0] = applet.color(179, 226, 205);
		in[1] = applet.color(253, 205, 172);
		in[2] = applet.color(203, 213, 232);
		in[3] = applet.color(244, 202, 228);
		in[4] = applet.color(230, 245, 201);
		in[5] = applet.color(255, 242, 174);
		in[6] = applet.color(241, 226, 204);
		in[7] = applet.color(204, 204, 204);

		for (int i = 0; i < in.length; i++) {
			interfaceCScheme.add("color" + i, in[i]);
		}

		return interfaceCScheme;
	}

	private void generateColor(Graph graph, ColorMap cMap) {
		for (Node node : graph.getNodes()) {
			node.setLabelColor(cMap.getColor("FONT"));
			switch (node.getElementType()) {
			case PROVIDES:
			case REQUIRES:
				node.setShapeColor(cMap.getColor(node.getLabel()));
				break;
			default:
				node.setShapeColor(cMap.getColor(node.getElementType()));
				break;
			}
		}
	}

	public void printGraph() {
		for (Node node : graph.getNodes()) {
			System.out.println(node.getId());
		}

		for (Spring spring : graph.getSprings()) {
			System.out.println("Spring from " + spring.getFromNode().getId() 
					+ " to " + spring.getToNode().getId());
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
	}

	/**
	 * @return the tree
	 */
	public Tree getTree() {
		return tree;
	}

	/**
	 * @param tree the tree to set
	 */
	public void setTree(Tree tree) {
		this.tree = tree;
	}

	/**
	 * @return the views
	 */
	public PApplet[] getViews() {
		return views;
	}

	/**
	 * @param views the views to set
	 */
	public void setViews(PApplet[] views) {
		this.views = views;
	}
}


