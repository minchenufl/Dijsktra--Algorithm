import java.util.ArrayList;

/**
 * This class implements the nodes in the graph
 * @author Min Chen
 *
 */
public class Gnode 
{
	private int index;
	private ArrayList<Adjnode> adjNodes;		//list of adjacent nodes
	private int neighborNumber = 0;
	private boolean isPathFound;
	
	public Gnode(int index)
	{
		this.index = index;
		adjNodes = new ArrayList<Adjnode>();
	}
	
	/**
	 * insert a adjacent node
	 * @param i is the index of the adjacent node
	 * @param d is the distance to the adjacent node
	 */
	public void insertNeighbor(int i, double d)
	{
		adjNodes.add(new Adjnode(i, d));
	}
	
	/**
	 * get this node's adjacent node
	 * @return the adjacent node list
	 */
	public ArrayList<Adjnode> getAdjnodes()
	{
		return adjNodes;
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
