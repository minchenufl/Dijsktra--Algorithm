/**
 * This class implements the nodes in the graph
 * @author Min Chen
 *
 */
public class Gnode 
{
  private int index;
	private Adjnode[] adjNodes;				//create enough space for containing all possible adjacent nodes
	private Adjnode[] actualAdjnodes;		//actual adjacent nodes 
	private int neighborNumber = 0;
	private boolean isPathFound;
	
	public Gnode(int index, int numberOfNodes)
	{
		this.index = index;
		adjNodes = new Adjnode[numberOfNodes];
	}
	
	/**
	 * insert a adjacent node
	 * @param i is the index of the adjacent node
	 * @param d is the distance to the adjacent node
	 */
	public void insertNeighbor(int i, double d)
	{
			adjNodes[neighborNumber] = new Adjnode(i, d);
			neighborNumber ++;
	}
	
	/**
	 * get this node's adjacent node
	 * @return the adjacent node list
	 */
	public Adjnode[] getAdjnodes()
	{
		actualAdjnodes =  new Adjnode[neighborNumber];
		for (int i=0; i<neighborNumber; i++)
				actualAdjnodes[i] = adjNodes[i];

		return actualAdjnodes;
	}
	
	public int getIndex()
	{
		return index;
	}
	
	public int getNeighborNumber()
	{
		return neighborNumber;
	}
	
	public void setPathFound(boolean b)
	{
		isPathFound = b;
	}
	
	public boolean isPathFound()
	{
		return isPathFound;
	}

}
