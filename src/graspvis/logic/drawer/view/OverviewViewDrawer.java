package graspvis.logic.drawer.view;

import graspvis.logic.drawer.element.Component;
import graspvis.logic.drawer.element.Connector;
import graspvis.logic.drawer.element.Interface;
import graspvis.logic.drawer.element.Layer;
import graspvis.logic.drawer.element.Provides;
import graspvis.logic.drawer.element.Rationale;
import graspvis.logic.drawer.element.ElementDrawer;
import graspvis.logic.drawer.element.Requires;
import graspvis.logic.drawer.element.Template;
import graspvis.logic.drawer.relationship.Causality;
import graspvis.logic.drawer.relationship.Connection;
import graspvis.logic.drawer.relationship.Containment;
import graspvis.logic.drawer.relationship.Extension;
import graspvis.logic.drawer.relationship.Instantiation;
import graspvis.logic.drawer.relationship.RelationshipDrawer;
import graspvis.model.Graph;
import graspvis.model.Node;
import graspvis.model.RelationshipType;
import graspvis.model.Spring;
import processing.core.PApplet;

public class OverviewViewDrawer implements ViewDrawer {

	private PApplet applet;
	private Graph graph;
	private ElementDrawer layer;
	private ElementDrawer component;
	private ElementDrawer connector;
	private ElementDrawer template;
	private ElementDrawer rationale;
	private ElementDrawer provides;
	private ElementDrawer requires;
	
	private RelationshipDrawer connection;
	private RelationshipDrawer containment;
	private RelationshipDrawer instantiation;
	private RelationshipDrawer causality;
	private RelationshipDrawer extension;
	
	public OverviewViewDrawer(PApplet applet, Graph graph) {
		this.applet = applet;
		this.graph = graph;
		layer = new Layer();
		component = new Component();
		connector = new Connector();
		template = new Template();
		new Interface();
		rationale = new Rationale();
		provides = new Provides();
		requires = new Requires();
		
		connection = new Connection();
		containment = new Containment();
		instantiation = new Instantiation();
		causality = new Causality();
		extension = new Extension();
	}
	
	@Override
	public void draw() {
		drawSprings(graph.getSprings());
		drawNodes(graph.getNodes());
	}
	
	private void drawNodes(Node[] nodes) {
		for (Node node : graph.getNodes()) {
			switch (node.getElementType()) {
			case LAYER:
				layer.draw(applet, node);
				break;
			case CONNECTOR:
				connector.draw(applet, node);
				break;
			case COMPONENT:
				component.draw(applet, node);
				break;
			case TEMPLATE:
				template.draw(applet, node);
				break;
			case PROVIDES:
				((Provides) provides).setReference(getInterfaceReferencePoint(node));
				provides.draw(applet, node);
				break;
			case REQUIRES:
				((Requires) requires).setReference(getInterfaceReferencePoint(node));
				requires.draw(applet, node);
				break;
			case RATIONALE:
				rationale.draw(applet, node);
				break;
			default:
				break;
			}
		}
	}
	
	private void drawSprings(Spring[] springs) {
		for(Spring spring : graph.getSprings()) {
			switch (spring.getRelationshipType()) {
			case CONNECTION:
				connection.draw(applet,spring);
				break;
			case CONTAINMENT:
				containment.draw(applet,spring);
				break;
			case INSTANTIATION:
				instantiation.draw(applet, spring);
				break;
			case CAUSALITY:
				causality.draw(applet, spring);
				break;
			case EXTENSION:
				extension.draw(applet, spring);
				break;
			default:
				break;
			}
		}
	}
	
	/*
	 * Get parent component of given interface
	 */
	private Node getInterfaceReferencePoint(Node iface) {
		Node parent = null;
		for (Spring s : graph.getSprings()) {
			if (s.getRelationshipType() == RelationshipType.INTERFACE && s.getToNode().equals(iface)) {
				parent = s.getFromNode();
			}
		}
		return parent;
	}

}
