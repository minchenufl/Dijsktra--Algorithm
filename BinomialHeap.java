/**
 * This class implements binomial heap 
 * @author Min Chen
 *
 */
public class BinomialHeap 
{
  private Bnode root;
	
	/**
	 * constructor, create a null heap 
	 */
	public BinomialHeap()
	{
		root = null;
	}
	
	/**
	 * constructor, create a heap with one node
	 * @param node will be the root of the heap
	 */
	public BinomialHeap(Bnode node)
	{
		root = node;
	}
	
	/**
	 * Inner class for the binomial heap node
	 * @author Min Chen
	 *
	 */
	public static class Bnode
	{
		private int index;
		private int degree;
		private double distance;
		private Bnode parent;
		private Bnode child;
		private Bnode sibling;
		
		public Bnode(int index, double distance)
		{
			this.index = index;
			this.distance = distance;
			this.degree = 0;
		}
		
		public void setIndex(int i)
		{
			index = i;
		}
		
		public int getIndex()
		{
			return index;
		}
		
		public void setDistance(double d)
		{
			distance = d;
		}
		public double getDistance()
		{
			return distance;
		}
		
		public void setParent(Bnode p)
		{
			parent = p;
		}
		
		public Bnode getParent()
		{
			return parent;
		}
		
		public void setChild(Bnode c)
		{
			child = c;
		}
		
		public Bnode getChild()
		{
			return child;
		}
		
		public void setSibling(Bnode s)
		{
			sibling = s;
		}
		
		public Bnode getSibling()
		{
			return sibling;
		}
		
		public void increaseDgree()
		{
			degree ++;
		}
		
		public void decraseDegree()
		{
			degree --;
		}
		public int getDegree()
		{
			return degree;
		}
		
	}
	
	/**
	 * link node y and node z, y becomes z's child
	 * @param y a node in the heap, becomes z's child
	 * @param z a node in the heap, becomes y's parent
	 */
	public void link(Bnode y, Bnode z)		
	{
		y.setParent(z);
		y.setSibling(z.getChild());			//if z has no child, y has no sibling
		z.setChild(y);
		z.increaseDgree();
	}
	
	/**
	 * Get the node with the minimum data
	 * @return the node with minimum data
	 */
	public Bnode getMin()
	{
		Bnode y = null;
		Bnode x = root;
		while(x != null)
		{
			if(y == null || x.getDistance() < y.getDistance())
				y = x;
			x = x.getSibling();
		}
		return y;
	}
	
	/**
	 * get the root of the heap
	 * @return the root of the heap
	 */
	public Bnode getRoot()
	{
		return root;
	}
	
	/**
	 * merge the root list with another heap, 
	 * the degrees in the merged root list appear
	 * in monotonically increasing order						
	 * @param h the heap to be merged with this heap
	 */
	public void merge(BinomialHeap h)   
	{
		Bnode a = root;
		Bnode b = h.getRoot();
		// if b == null, do nothing
		if(a== null && b!=null)
			root = b;
		
		if(a!=null && b!=null)		//insert b between a and a.sibling if a.degree < b.degree < a.sibling.degree
		{
			if(a.getDegree() > b.getDegree())
			{
				root = b;
				b = a;
				a = root;
			}
			
			while(b!=null)
			{
				if(a.getSibling() == null)
				{	a.setSibling(b);
					break;
				}
				
				else 
				{
					if(a.getSibling().getDegree() < b.getDegree())
						a = a.getSibling();
					else 
					{
						Bnode c = b.getSibling();
						b.setSibling(a.getSibling());
						a.setSibling(b);
						a = a.getSibling();
						b = c;
					}
				}
			}	
		}
	}

	/**
	 * merge two heaps, and link the nodes with the same degree in the root list
	 * @param h the heap to be united with this heap
	 */
	public void union(BinomialHeap h)
	{
		this.merge(h);
		
		if(root != null)
		{
			Bnode previous = null;
			Bnode x = root;
			Bnode next = x.getSibling();
			
			while(next != null)
			{
				//the two consecutive nodes have different degrees, 
				//or the node after them also have the same degree
				if((x.getDegree() != next.getDegree()) 
						|| (next.getSibling() != null && next.getSibling().getDegree() == x.getDegree()))
				{
					previous = x;
					x = next;
				}
				
				else 
				{
					if (x.getDistance() <= next.getDistance())
					{
						x.setSibling(next.getSibling());
						this.link(next, x);
					}
					
					else
					{
						if(previous == null)
							root = next;
						
						else previous.setSibling(next);
						
						this.link(x, next);
						x = next;
					}
					
				}
				next = x.getSibling();
			}
		}
	}

	/**
	 * insert a new node into the heap
	 * @param index the new node's index
	 * @param distance the new node's value of distance
	 */
	public void insert(int index, double distance)
	{
		Bnode bnode = new Bnode(index, distance);
		BinomialHeap h = new BinomialHeap(bnode);
		this.union(h);
	}
	
	/**
	 * since the degrees of a node's children 
	 * monotonically decrease from left to right, 
	 * we should reverse the list when we add them
	 * to the root list
	 * @param x the beginning node in the list
	 * @return the new beginning node in the list (the last node in the original list)
	 */
	public Bnode reverseList(Bnode x)
	{
		Bnode y = x;
		if(x != null)
		{
			while(y.getSibling() != null)			//set those children's parent to null
			{
				y.setParent(null);
				y = y.getSibling();
			}
			y.setParent(null);
			
			if(x.getSibling() != null)				//recursively
			{
				reverseList(x.getSibling());
				x.getSibling().setSibling(x);
			}
			
			x.setSibling(null);
		}
		return y;
	}

	/**
	 * remove the min node from the heap
	 * @return the removed min node
	 */
	public Bnode extractMin()
	{
		Bnode min = this.getMin();
		
		if(root == min)
			root = root.getSibling();		//root may be null here
		else
		{
			Bnode x = root;					//remove min from root list
			while(x.getSibling() != min)
				x = x.getSibling();
			
			x.setSibling(min.getSibling());	
		}
		
		Bnode child = min.getChild();		//add its children to root list
		if(child != null)
		{
			child = this.reverseList(child);
			BinomialHeap h = new BinomialHeap(child);
			this.union(h);
		}

		return min;
	}

	/**
	 * decrease the distance value of node x to d
	 * @param x represents a node in the heap
	 * @param d the new value of distance of node x
	 */
	public void decreaseKey(Bnode x, double d)
	{
		if(d > x.getDistance())
		{
			System.out.println("Error! New key is greater than current key.");
			System.exit(1);
		}
		
		x.setDistance(d);
		
		Bnode y = x;
		Bnode z = y.getParent();
		while(z != null && y.getDistance() < z.getDistance())
		{
			double dist = y.getDistance();		// exchange the content of y, z if y.distance < z.distance 
			int index = y.getIndex();			//to maintain the the property of min heap
			y.setIndex(z.getIndex());
			y.setDistance(z.getDistance());
			z.setIndex(index);
			z.setDistance(dist);
			
			y = z;
			z = y.getParent();
		}
	}
	
	/**
	 * find the node with certain index from the heap rooted at x
	 * @param index represents the index value of the wanted node
	 * @param x the root of heap to be searched
	 * @return the found node
	 */
	public Bnode searchNode(int index, Bnode x) 
	{
		Bnode wantedNode = null;
		
		if(x.getIndex() == index)
			return x;
		else
		{
			if(x.getChild() != null && wantedNode == null)
				wantedNode = searchNode(index, x.getChild());
			
			if(x.getSibling() !=null && wantedNode == null)
				wantedNode = searchNode(index, x.getSibling());
			
			return wantedNode;
		}
			
	}
	
	/**
	 * delete node x from the heap
	 * @param x represents the node to be deleted
	 */
	public void delete(Bnode x)
	{
		this.decreaseKey(x, Double.NEGATIVE_INFINITY);
		this.extractMin();
	}
}
