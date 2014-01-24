package graspvis.grasp;

import grasp.lang.Compiler;
import grasp.lang.IArchitecture;
import grasp.lang.ICompiler;
import grasp.lang.IFirstClass;
import graspvis.exception.CompilationException;

import common.io.FileSource;
import common.io.ISource;
import common.logging.ConsoleLogger;
import common.logging.ILogger;

public class Executor {
	public IArchitecture execute(String graspSource) throws CompilationException {
		/*
		 * Setup a logger. Compiler errors are reported through the logger
		 */
		ILogger logger = new ConsoleLogger().initialize("Grasp", ILogger.Level.ERROR, true);

		/*
		 * Create a file source object for input file
		 */
		ISource source = new FileSource(graspSource);

		/*
		 * Create the compiler object and invoke compiler
		 */
		//logger.print("Compiling Grasp source file...");
		ICompiler compiler = new Compiler();
		IArchitecture graph = compiler.compile(source, logger);

		/*
		 * Check if there were any compiler errors
		 */
		if (compiler.getErrors().isAny() || graph == null) {
			logger.print("Errors encountered during compilation.");
			throw new CompilationException("Errors encountered during compilation.");
		}

		
		/*
		 * Print architecture graph while navigating through it
		 */
		logger.print("Printing architecture graph...");
		printArchitecture(graph, 0);

		//logger.print("Compilation successfull.");
		logger.shutdown();
		return graph;
	}

	/**
	 *  Print architecture graph recursively. This method illustrates how to navigate
	 *  the architecture graph generate by the Grasp compiler.
	 * @param node
	 * @param indent
	 */
	private static void printArchitecture(IFirstClass node, int indent) {
		StringBuffer sb = new StringBuffer(indent);
		for ( int i = 0; i < indent; i++ )
			sb = sb.append("    ");
		System.out.print(sb.toString());
		System.out.printf("%s <%s (%s)>\n", node.getType().toString(), node.getName(), node.getReferencingName());
		if (node.getType().toString().equals("TEMPLATE") && node.getName().equals("SensorNetworkedComponent")) {
			System.out.println(node.getBody().size());
		}
//		if (node.getType().toString().equals("COMPONENT")) {
//			Component com = (Component) node;
//			Template temp = (Template) com.getBase();
//			sb.append("    ");
//			System.out.printf(sb.toString() + "[%s]\n", com.getBase().getName());
//		}
		for (IFirstClass child : node.getBody()) {
			printArchitecture(child, indent+1);
		}
	}
}
