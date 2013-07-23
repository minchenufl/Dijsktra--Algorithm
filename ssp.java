import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Random;

/**
 * This project uses Dijsktra's algorithm compute the shortest paths
 * to different nodes from a source. Three different schemes are implemented
 * to achieve the Dijsktra's algorithm. Every node in the directed graph can be the 
 * source. So we can obtain the shortest path between arbitrary pair of nodes
 * in the directed graph.
 * @author Min Chen
 *
 */

public class ssp 
{ 
	public static final double INFINITY = Double.POSITIVE_INFINITY;
	
	public static void main(String[] args) throws FileNotFoundException
	{
		Edge[] edges = null;
		Gnode[] g;
		int n;
	
		if(args[0].equals("-r"))			//random mode
		{
			System.out.println("Random mode:");
			randomMode();
		}
		
		else
		{
			if(args.length == 1)		 	//the graph info is input by the user
			{
				System.out.println("Enter graph information:");
				edges = inputEdges();	
			}
			else if(args.length ==2)		//the graph info is read from the designated file
			{
				System.out.println("Load graph information from the file...");
				String filename = args[1];
				edges = loadEdges(filename);
			}
			
			n = getNumberOfNodes(edges);
			g = buildGraph(edges, n);
			double[][] distVec = new double[n][n];
			
			if(args[0].equals("-is"))		//simple scheme
			{	
				System.out.println("Simple scheme:");
				distVec = simpleScheme(g);
			}
			else if(args[0].equals("-if"))	//fibonacci heap scheme
			{
				System.out.println("Fibonacci heap scheme:");
				distVec = fheapScheme(g);
			}
			else if(args[0].equals("-ib"))	//binomial heap scheme
			{
				System.out.println("Binomial heap scheme:");
				distVec = bheapScheme(g);
			}
			
			System.out.print("node \t");
			for(int i=0; i<n; i++)
				System.out.print(i + "\t");
			System.out.println();
			for(int i=0; i<n; i++)
			{
				System.out.print(i + "\t");
				for(int j=0; j<n; j++)
				{
					if(distVec[i][j] == Double.POSITIVE_INFINITY)
						System.out.print("?\t");
					else
						System.out.print(distVec[i][j] + "\t");
				}
				System.out.println();
			}
		}
	}
	
	/**
	 * random mode, generate a graph randomly, 
	 * and then compare the performance of three different schemes
	 */
	public static void randomMode()
	{
		int n = 0; 								//number of node in graph
		double density; 
		Edge[] edges;
		Edge tempEdge;
		Gnode[] g = null;
		int numOfEdges, currentNum;
		int start, end;
		Random gen = new Random();
		boolean isConnected = false;
		double[] time = new double[3];
		int percentage;
		boolean duplicateEdge;
		System.out.println("Number of vertices\t" + "Density\t\t" + "Simple scheme\t" + "F-heap scheme\t" + "B-heap scheme");
		for(int i=100; i<=500; i = i + 100)
			for(int j=1; j<=10; j++)
			{
				isConnected = false;
				n = i;
				density = j * 0.1;
				numOfEdges = (int) (n * (n-1) * density);
				edges = new Edge[numOfEdges];
				while(!isConnected)			//execute the three schemes only when the graph is a connected
				{
					currentNum = 0;
					while(currentNum < numOfEdges)		//generate n*(n-1)*density edges
					{
						start = gen.nextInt(n);
						end = gen.nextInt(n);
						if(start == end)
							continue;
						
						else
						{
							tempEdge = new Edge(start, end, gen.nextInt(1000) + 1);
							duplicateEdge = false;
							for(Edge e : edges)
							{
								if(tempEdge.equals(e))	//check whether the new generated edge already exists
								{	
									duplicateEdge = true;
									break;
								}
							}
							
							if(duplicateEdge == false)
							{
								edges[currentNum] = tempEdge;
								currentNum ++;
							}

						}
					}
					g = buildGraph(edges, n);
					isConnected = isConnected(g);		//test whether the generated graph is connected
				}
				
				time[0] = System.currentTimeMillis();
				simpleScheme(g);
				time[0] = System.currentTimeMillis() - time[0];
				
				time[1] = System.currentTimeMillis();
				fheapScheme(g);
				time[1] = System.currentTimeMillis() - time[1];
				
				time[2] = System.currentTimeMillis();
				bheapScheme(g);
				time[2] = System.currentTimeMillis() - time[2];
				
				percentage = j * 10;
				System.out.println(n + "\t\t\t" + percentage +"%\t\t" + time[0] + "\t\t" + time[1] + "\t\t" + time[2] );
			}
	}
	
	/**
	 * simple scheme
	 * @param g is the graph
	 * @return shortest distance array
	 */
	public static double[][] simpleScheme(Gnode[] g)
	{
		int n = g.length;
		double[][] distVec = new double[n][n];
		for(int i=0; i<n; i++)
			for(int j=0; j<n; j++)
				distVec[i][j] = INFINITY;
		
		int counter, minIndex = 0;
		double minDist;
		
		for(int source=0; source<n; source++)
		{
			distVec[source][source] = 0;
			counter = 0;
			
			for(Gnode gnode : g)
				gnode.setPathFound(false) ;
			
			while(counter<n)		//find shortest path to n nodes
			{
				minDist = Double.POSITIVE_INFINITY;
				
				for(Gnode gnode : g)	//find the node that has the shortest distance among all undetermined nodes
				{
					if((!gnode.isPathFound()) && minDist > distVec[source][gnode.getIndex()])
					{
						minDist = distVec[source][gnode.getIndex()];
						minIndex = gnode.getIndex();
					}
				}
				
				g[minIndex].setPathFound(true);
				counter ++;
				
				//the distance between the source and a neighbor of the new added node may be reduced
				for(Adjnode an : g[minIndex].getAdjnodes())
				{
					if(distVec[source][an.getIndex()] > distVec[source][minIndex] + an.getDistance())
						distVec[source][an.getIndex()] = distVec[source][minIndex] + an.getDistance();
					//no need to consider the value of isPathFound, because those whose shortest paths have been
					//decided always have a shorter distance than the current shortest path 
				}	
			}
		}
		return distVec;
	}
	
	/**
	 * Fibonacci heap scheme
	 * @param g is the graph
	 * @return the shortest distance array
	 */
	public static double[][] fheapScheme(Gnode[] g)
	{
		FibonacciHeap fheap = new FibonacciHeap();
		int n = g.length;
		double[][] distVec = new double[n][n];
		for(int i=0; i<n; i++)
			for(int j=0; j<n; j++)
				distVec[i][j] = INFINITY;
		int minIndex = 0;
	
		for(int source=0; source<n; source++)
		{
			distVec[source][source] = 0;
			for(Gnode gnode : g)
				gnode.setPathFound(false);
			
			fheap.insert(source, 0);
			
			g[source].setPathFound(true);   //here pathFound denotes whether the node is inserted into the heap or not 
			
			while(fheap.getMin() != null)
			{
				minIndex = fheap.extractMin().getIndex();
				//distance to the adjacent nodes of the recently removed node may be reduced
				for(Adjnode an : g[minIndex].getAdjnodes())
				{
					if(distVec[source][an.getIndex()] > distVec[source][minIndex] + an.getDistance())
					{
						distVec[source][an.getIndex()] = distVec[source][minIndex] + an.getDistance();
						//if the node has not been inserted to the heap, then insert it to the heap
						if(g[an.getIndex()].isPathFound() == false)
						{
							fheap.insert(an.getIndex(), distVec[source][an.getIndex()]);
							g[an.getIndex()].setPathFound(true);
						}
						
						//if the node is already in the heap, find the corresponding heap node, and decrease the distance
						else
							fheap.decreaseKey(fheap.searchNode(an.getIndex(), fheap.getMin()), distVec[source][an.getIndex()]);
					}
				}	
			}
		}
		return distVec;
	}
	
	/**
	 * Binomial heap scheme
	 * @param g is the graph
	 * @return the shortest distance array
	 */
	public static double[][] bheapScheme(Gnode[] g)
	{
		BinomialHeap bheap = new BinomialHeap();
		int n = g.length;
		double[][] distVec = new double[n][n];
		for(int i=0; i<n; i++)
			for(int j=0; j<n; j++)
				distVec[i][j] = INFINITY;
		int minIndex = 0;
		for(int source=0; source<n; source++)
		{
			distVec[source][source] = 0;
			for(Gnode gnode : g)
				gnode.setPathFound(false);
			
			bheap.insert(source, 0);
			g[source].setPathFound(true);   //here pathFound=true means this node is inserted into the heap 
			
			while(bheap.getRoot() != null)
			{
				minIndex = bheap.extractMin().getIndex();	
				for(Adjnode an : g[minIndex].getAdjnodes())
				{
					if(distVec[source][an.getIndex()] > distVec[source][minIndex] + an.getDistance())
					{
						distVec[source][an.getIndex()] = distVec[source][minIndex] + an.getDistance();
						if(g[an.getIndex()].isPathFound() == false)
						{
							bheap.insert(an.getIndex(), distVec[source][an.getIndex()]);
							g[an.getIndex()].setPathFound(true);
						}
						
						else bheap.decreaseKey(bheap.searchNode(an.getIndex(), bheap.getRoot()), distVec[source][an.getIndex()]);
					}
				}	
			}
		}
		return distVec;
	}

	/**
	 * input the edge information
	 * @return the edges
	 */
	public static Edge[] inputEdges()
	{
		Scanner in = new Scanner(System.in);
		String s , combination = "";
		String[] temp, temp2;
		while(!(s = in.nextLine()).equals("*"))
		{
			combination += s + "/";
		}
			
		temp = combination.split("/");
		Edge[] edges = new Edge[temp.length];
		
		for(int i=0; i<temp.length; i++)	
		{
			temp2 = temp[i].split(" ");
			edges[i] = new Edge(Integer.parseInt(temp2[0]), Integer.parseInt(temp2[1]), Integer.parseInt(temp2[2]));
		}
		
		in.close();
		return edges;
	}
	
	/**
	 * read edge information from a file
	 * @param filename is the file that stores the edge information
	 * @return the edges
	 * @throws FileNotFoundException
	 */
	public static Edge[] loadEdges(String filename) throws FileNotFoundException
	{
		Scanner in = new Scanner(new File(filename));
		String s , combination = "";
		String[] temp, temp2;
		while(!(s = in.nextLine()).equals("*"))
		{
			combination += s + "/";
		}
		
		System.out.println(combination);
			
		temp = combination.split("/");
		Edge[] edges = new Edge[temp.length];
		
		for(int i=0; i<temp.length; i++)	
		{
			temp2 = temp[i].split(" ");
			edges[i] = new Edge(Integer.parseInt(temp2[0]), Integer.parseInt(temp2[1]), Integer.parseInt(temp2[2]));
		}
		
		in.close();
		return edges;
	}
	
	/**
	 * compute the number of nodes in the graph using the edge information
	 * @param edges represents the edge information
	 * @return the number of nodes in the graph
	 */
	public static int getNumberOfNodes(Edge[] edges)
	{
		int n = 0;					//number of nodes;
		for(Edge e : edges)
		{
			n = Math.max(n, e.getStart());
			n = Math.max(n, e.getEnd());
		}
		n = n + 1;					//number of nodes is equal to the maximum index of nodes + 1
		return n;
	}
	
	/**
	 * build a graph using the edge information, and number of nodes
	 * @param edges represents edges in the graph
	 * @param n is the number of nodes
	 * @return the built graph
	 */
	public static Gnode[] buildGraph(Edge[] edges, int n)
	{
		Gnode[] gnodes = new Gnode[n];
		for(int i=0; i<n; i++)
		{
			gnodes[i] = new Gnode(i, n);
		}
		
		for(Edge e: edges)
		{
			gnodes[e.getStart()].insertNeighbor(e.getEnd(), e.getDistance());
		}
			
		return gnodes;
	}
	
	/**
	 * test whether the graph is connected or not
	 * @param g represents the graph
	 * @return true is the graph is connected
	 */
	public static boolean isConnected(Gnode[] g)
	{
		double[][] distVec = simpleScheme(g);		
		//execute simple scheme, if the distance between any pair of nodes is 
		//larger than the possible maximum distance if they are connected, then
		//the graph is not connected
		
		for(int i=0; i<g.length; i++)
			for(int j=0; j<g.length; j++)
			{
				if(distVec[i][i] > 1000 * g.length + 1)
					return false;
			}
		return true;
	}

}
