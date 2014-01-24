/**
 * 
 */
package graspvis.traversal;

import grasp.lang.IArchitecture;
import graspvis.exception.AlgorithmException;
import graspvis.model.Graph;
import graspvis.model.Node;
import graspvis.model.Tree;

/**
 * @author nauval
 *
 */
public interface Traversal {
	
	/**
	 * Sets the {@code IArchitecture} that will be traversed
	 * @param {@code IArchitecture} of compiled Grasp graph
	 */
	public void setArchitecture(IArchitecture theArchitecture);
	
	/**
	 * Traverse the given graph
	 */
	public Graph traverse();
	
	/**
	 * Create a tree from the graph with given id as the root node.
	 * @param node of the node
	 * @param level of depth
	 * @return {@code TreeNode} with node id as its root and has given level of depth
	 * @throws AlgorithmException 
	 */
	public Tree traverse(Node node, int level) throws AlgorithmException;

	public void setGraph(Graph graph);
}
