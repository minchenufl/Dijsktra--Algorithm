/**
 * This class implements the edges in the graph
 * @author Min Chen
 *
 */
public class Edge 
{
  private int start;
	private int end;
	private double distance;
	
	/**
	 * Constructor
	 * @param start starting point of an edge
	 * @param end ending point of an edge
	 * @param distance the distance between the points
	 */
	public Edge(int start, int end, double distance)
	{
		this.start = start;
		this.end = end;
		this.distance = distance;
	}
	
	public int getStart()
	{
		return start;
	}
	
	public int getEnd()
	{
		return end;
	}
	
	public double getDistance()
	{
		return distance;
	}
	
	public boolean equals(Edge e)
	{
		if(e != null && start == e.getStart() && end == e.getEnd())
			return true;
		else return false;
	}

}
