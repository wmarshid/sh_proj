package graspvis.traversal;

import grasp.lang.IArchitecture;
import grasp.lang.ICausalContext;
import grasp.lang.IComponent;
import grasp.lang.IConnector;
import grasp.lang.IExtensible;
import grasp.lang.IFirstClass;
import grasp.lang.ILayer;
import grasp.lang.ILink;
import grasp.lang.IProvides;
import grasp.lang.IRationale;
import grasp.lang.ISystem;
import grasp.lang.ITemplate;
import graspvis.model.BasicGraph;
import graspvis.model.BasicNode;
import graspvis.model.ElementType;
import graspvis.model.Graph;
import graspvis.model.Node;
import graspvis.model.RelationshipType;
import graspvis.model.Tree;
import graspvis.util.Util;

import java.util.ArrayList;
import java.util.Collection;

public class GraphTraversal implements Traversal {
	private Graph graph;

	private IFirstClass system;

	/** Initial x position position of the nodes*/
	private float middlePositionX = 400;
	/** Initial y position position of the nodes*/
	private float middlePositionY = 400;

	public GraphTraversal() {
		graph = new BasicGraph();
	}
	
	public GraphTraversal (IArchitecture architecture) {
		graph = new BasicGraph();
		this.system = getSystem(architecture);
	}
	
	@Override
	public void setArchitecture(IArchitecture architecture) {
		graph = new BasicGraph();
		system = getSystem(architecture);
	}
	
	@Override
	public Graph traverse() {
		DFTraversal(system);
		return graph;
	}
	
	private ISystem getSystem(IArchitecture architecture) {
		ISystem s = null;
		for (IFirstClass element : architecture.getBody()) {
			if (element.getType().toString().equals("SYSTEM")) {
				s = (ISystem) element;
				break;
			}
		}
		return s;
	}

	private void DFTraversal(IFirstClass element) {
		ElementType type = getElementType(element);
		ElementType cType = null;

		ArrayList<ILink> links = new ArrayList<ILink>();
		ArrayList<IFirstClass> children = new ArrayList<IFirstClass>();


		if (element.getBody().isEmpty()) {
			createElementNode(element, type);
			return;
		}

		for (IFirstClass child : element.getBody()) {
			cType = getElementType(child);

			if (cType == ElementType.LINK) {
				links.add((ILink) child);
			} else {
				DFTraversal(child);
				// left out interface which has in and or as its name 
				if (type != null && child.getName() != "in" && child.getName() != "out") {
					children.add(child);
				}
			}
		}

		
		// create connection relationship (link) from provider to consumer
		// iterates all links and create a connection relationship out of each of them
		for (ILink link : links) {
			IFirstClass prov = null;//link.getProviders();
			Collection<IProvides> provides = link.getProviders();
			for (IProvides p : provides) {
				prov = p;
			}
			IFirstClass cons = link.getConsumer();
			createConnectionSpring(prov, cons);
		}
		
		
		// create node if not SYSTEM
		if (type != null) {
			createElementNode(element, type);
		
			// create containment relationship, another for loop again
			for (IFirstClass child : children) {
				ElementType childType = getElementType(child);
				createContainmentSpring(element, child, type, childType);
			}

			// create template connection
			createTemplateConnection(element, type);

			// create rationale connection
			createRationaleConnection(element, type);
		}
	}

	private void createRationaleConnection(IFirstClass element, ElementType type) {
		Collection<ICausalContext> rationales = null;
		switch (type) {
		case LAYER:
			rationales = ((ILayer) element).getCausalContexts();
			break;
		case COMPONENT:
			rationales = ((IComponent) element).getCausalContexts();
			break;
		case CONNECTOR:
			rationales = ((IConnector) element).getCausalContexts();
			break;
		case LINK:
			rationales = ((ILink) element).getCausalContexts();
			break;
		default:
			break;
		}
		// Iterate all rational
		for (ICausalContext causal : rationales) {
			IRationale rationale = causal.getRationale();
			String id = getElementId(rationale, ElementType.RATIONALE);
			Node node = graph.getNode(id);
			if (node == null) {
				node = createElementNode(rationale, ElementType.RATIONALE);
			}
			createRationaleSpring(element, rationale);
			
			// create extension
			createExtensionConnection(node, rationale, ElementType.RATIONALE);
//			IFirstClass extendee = ((IExtensible) rationale).getExtendee();
//			String idExtendee = getElementId(extendee, ElementType.RATIONALE);
//			Node nodeExtendee = graph.getNode(idExtendee);
//			if (nodeExtendee == null) {
//				createElementNode(extendee, ElementType.RATIONALE);
//			}
//			createSpring(node, node, nodeExtendee, nodeExtendee, RelationshipType.EXTENSION);
		}
	}

	private void createExtensionConnection(Node extender, IExtensible element, ElementType type) {
		// create extension
		IFirstClass extendee = ((IExtensible) element).getExtendee();
		
		if (extendee == null)
			return;
		
		String idExtendee = getElementId(extendee, type);
		Node nodeExtendee = graph.getNode(idExtendee);
		
		if (nodeExtendee == null) 
			nodeExtendee = createElementNode(extendee, type);
		
		createSpring(extender, extender, nodeExtendee, nodeExtendee, RelationshipType.EXTENSION);
	}
	
	private void createRationaleSpring(IFirstClass element, IRationale rationale) {
		Node fromNode =null, toNode = null;
		String elementId = getElementId(element, getElementType(element));
		String rationaleId = getElementId(rationale, ElementType.RATIONALE);

		fromNode = graph.getNode(elementId);
		toNode = graph.getNode(rationaleId);
		createSpring(fromNode, fromNode, toNode, toNode, RelationshipType.CAUSALITY);
	}
	
	private void createTemplateConnection(IFirstClass element, ElementType type) {
		ITemplate tmp = null;
		if (type == ElementType.COMPONENT || type == ElementType.CONNECTOR) {
			String id = "";

			switch (type) {
			case COMPONENT:
				tmp = ((IComponent) element).getTemplate();
				break;
			case CONNECTOR:
				tmp = ((IConnector) element).getTemplate();
			default:
				break;
			}
			
			id = getElementId(tmp, ElementType.TEMPLATE);
			Node node = graph.getNode(id);
			if (node == null) {
				node = createElementNode(tmp, ElementType.TEMPLATE);
			}
			
			createTemplateSpring(element, tmp);
			createExtensionConnection(node, tmp, ElementType.TEMPLATE);
		}
	}
	
	private void createTemplateSpring(IFirstClass element, ITemplate tmp) {
		String elementId = getElementId(element, getElementType(element));
		String templateId = getElementId(tmp, ElementType.TEMPLATE);
		
		Node fromNode = graph.getNode(elementId);
		Node toNode = graph.getNode(templateId);
		
		createSpring(fromNode, fromNode, toNode, toNode, RelationshipType.INSTANTIATION);
	}

	private ElementType getElementType(IFirstClass element) {
		ElementType type = null;

		switch (element.getType().toString().toUpperCase()) {
		case "COMPONENT":
			type = ElementType.COMPONENT;
			break;
		case "CONNECTOR":
			type = ElementType.CONNECTOR;
			break;
		case "LAYER":
			type = ElementType.LAYER;
			break;
		case "RATIONALE":
			type = ElementType.RATIONALE;
			break;
		case "TEMPLATE":
			type = ElementType.TEMPLATE;
			break;
		case "LINK":
			type = ElementType.LINK;
			break;
		case "PROVIDES":
			type = ElementType.PROVIDES;
			break;
		case "REQUIRES":
			type = ElementType.REQUIRES;
			break;
		}
		return type;
	}

	private Node createElementNode(IFirstClass element, ElementType type) {
		float x = middlePositionX/2 + Util.random(-100, 100);
		float y = middlePositionY/2 + Util.random(-100, 100);
		float size = type.getSize();

		String id = getElementId(element, type);
		String label = element.getName() != null? element.getName() : element.getReferencingName();

		return createNode(id, label, type, size, x, y);
	}

	private Node createNode(String id, String label, ElementType type, float size, float x, float y) {
		Node n = new BasicNode(id,x, y);
		n.setLabel(label);
		n.setRadius(size);
		n.setElementType(type);
		graph.addNode(n);
		return n;
	}
	
	private void createContainmentSpring(IFirstClass element, IFirstClass child, ElementType type, ElementType childType) {
		RelationshipType rType = RelationshipType.CONTAINMENT;
		Node fromNode = null, toNode = null;

		String id = getElementId(element, type);
		String childId = getElementId(child, childType);

		if (childType == ElementType.PROVIDES || childType == ElementType.REQUIRES) {
			rType = RelationshipType.INTERFACE;
		}

		fromNode = graph.getNode(id);
		toNode = graph.getNode(childId);

		createSpring(fromNode, fromNode, toNode, toNode, rType);
	}

	private void createConnectionSpring(IFirstClass provider, IFirstClass consumer) {
		ElementType provType = getElementType(provider);
		ElementType consType = getElementType(consumer);
		
		// use interface's parents as from and to node
		// and use interfaces as its hook
		IFirstClass from = provider.getParent();
		IFirstClass to = consumer.getParent();
		ElementType fromType = getElementType(from);
		ElementType toType = getElementType(to);
		String fromId = getElementId(from, fromType);
		String toId = getElementId(to, toType);
		Node fromNode = graph.getNode(fromId);
		Node toNode = graph.getNode(toId);
		
		String provId = getElementId(provider, provType);
		String consId = getElementId(consumer, consType);
		
		Node fromNodeHook = graph.getNode(provId);
		Node toNodeHook = graph.getNode(consId);
		
		createSpring(fromNode, fromNodeHook, toNode, toNodeHook, RelationshipType.CONNECTION);
	}
	
	/**
	 * 
	 * @param fromNode actual connected node where the force is applied to
	 * @param fromNodeHook node for hook the line when the connection is drawn
	 * @param toNode actual connected node where the force is applied to
	 * @param toNodeHook node for hook the line when the connection is drawn
	 * @param type
	 */
	private void createSpring(Node fromNode, Node fromNodeHook, Node toNode, Node toNodeHook, RelationshipType type) {
		StringBuilder sb = new StringBuilder();
		sb.append(fromNode.getId());
		sb.append("_");
		sb.append(toNode.getId());
		sb.append("_");
		sb.append(type.toString());
		String id = sb.toString();
		
		graph.addSpring(id, fromNode, fromNodeHook, toNode, toNodeHook, type);
	}

	private String getElementId(IFirstClass element, ElementType type) {
		StringBuilder sb = new StringBuilder();
		switch (type) {
		case PROVIDES:
		case REQUIRES:
			sb.append(element.getParent().getName());
			sb.append("-");
			if (element.getName() == null) {
				sb.append(element.getName());
			} else {
				sb.append(element.getReferencingName());
			}
			sb.append(getElementAlias(element));
			break;
		default:
			sb.append(element.getName());
			break;
		}
		return sb.toString();
	}

	private String getElementAlias(IFirstClass element) {
		StringBuilder sb = new StringBuilder();

		if (element.getReferencingName() != null) {
			sb.append("-");
			sb.append(element.getReferencingName());
		}

		return sb.toString();
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

	@Override
	public Tree traverse(Node node, int level) {
		return null;
	}

	/**
	 * @return the initial x position
	 */
	public float getMiddlePositionX() {
		return middlePositionX;
	}

	/**
	 * @param middlePositionY the initial y nodes' position
	 */
	public void setMiddlePositionX(float middlePositionX) {
		this.middlePositionX = middlePositionX;
	}

	/**
	 * @return the initial y position
	 */
	public float getMiddlePositionY() {
		return middlePositionY;
	}

	/**
	 * @param middlePositionY the initial y nodes' position
	 */
	public void setMiddlePositionY(float middlePositionY) {
		this.middlePositionY = middlePositionY;
	}
}
