/**
 * This class implements the adjacent nodes
 * @author Min Chen
 *
 */
public class Adjnode 
{
  private int index;
	private double distance;
	
	/**
	 * Constructor
	 * @param i node index
	 * @param d distance to that adjacent node 
	 */
	public Adjnode(int i, double d)
	{
		index = i;
		distance = d;
	}
	
	public int getIndex()
	{
		return index;
	}

	public double getDistance()
	{
		return distance;
	}
}
