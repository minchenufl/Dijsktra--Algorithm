/**
 * This class implements the Fibonacci heap
 * @author Min Chen
 *
 */
public class FibonacciHeap 
{
  private Fnode min;
	private int size;
	
	/**
	 * Inner class for nodes of Fibonacci heap
	 * @author Min Chen
	 *
	 */
 	public static class Fnode
	{
		private int index;
		private int degree;
		private double distance;
		private Fnode parent;
		private Fnode child;
		private Fnode leftSibling;
		private Fnode rightSibling;
		private boolean childCut;
		
		public Fnode(int index, double distance)
		{
			this.index = index;
			this.distance = distance;
			this.degree = 0;
			this.childCut = false;
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
		
		public double getDistcance()
		{
			return distance;
		}
		
		public void setParent(Fnode p)
		{
			parent = p;
		}
		
		public Fnode getParent()
		{
			return parent;
		}
		
		public void setChild(Fnode c)
		{
			child = c;
		}
		
		public Fnode getChild()
		{
			return child;
		}
		
		public void setLeftSibling(Fnode ls)
		{
			leftSibling = ls;
		}
		
		public Fnode getLeftSibling()
		{
			return leftSibling;
		}
		
		public void setRightSibling(Fnode rs)
		{
			rightSibling = rs;
		}
		
		public Fnode getRightSibling()
		{
			return rightSibling;
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
		
		public void setChildCut(boolean b)
		{
			childCut = b;
		}
		
		public boolean getChildCut()
		{
			return childCut;
		}
		
	}

 	/**
 	 * constructor, create an empty Fibonacci heap
 	 */
	public FibonacciHeap()
	{
		min = null;
		size = 0;
	}
	
	/**
	 * constructor, create a Fibonacci heap with one node
	 * @param x becomes the min of the new heap
	 */
	public FibonacciHeap(Fnode x)
	{
		min = x;
		x.setChild(null);
		x.setParent(null);
		x.setLeftSibling(x);
		x.setRightSibling(x);
		size = 1;
	}
	

	public Fnode getMin()
	{
		return min;
	}
	
	/**
	 * get the size of the heap
	 * @return the number of nodes in the heap
	 */
	public int getSize()
	{
		return size;
	}
	
	/**
	 * remove y from root list, and make y a child of x, y.distance must be larger than x.distance	
	 * @param y a node removed from the root list, becomes x' child
	 * @param x a node in the root list, becomes y' parent
	 */
	public void link(Fnode y, Fnode x) 		
	{											
		Fnode l = y.getLeftSibling();
		Fnode r = y.getRightSibling();
		
		l.setRightSibling(r);
		r.setLeftSibling(l);
		y.setParent(x);
		y.setChildCut(false);
		
		if(x.getDegree() == 0)				//if y becomes the only child
		{
			y.setLeftSibling(y);
			y.setRightSibling(y);
		}
		
		else								// add y to the child list of x
		{
			y.setRightSibling(x.getChild());
			y.setLeftSibling(x.getChild().getLeftSibling());
			x.getChild().getLeftSibling().setRightSibling(y);
			x.getChild().setLeftSibling(y);
		}
			
		x.setChild(y);
		x.increaseDgree();
			
	}
	
	/**
	 * Union with another Fibonacci heap h
	 * @param h represents another Fibonacci heap
	 */
	public void union(FibonacciHeap h)
	{
		Fnode min2 = h.getMin();
		size += h.getSize();
		
		//if(min2 == null) do nothing
		
		if(min == null && min2 !=null)
		{
			min = min2;
		}

		if(min != null && min2 != null)		//add h's root list to this heap's root list, adjust min if necessary
		{
			Fnode l = min.getLeftSibling();
			
			min2.getLeftSibling().setRightSibling(min);
			l.setRightSibling(min2);
			min.setLeftSibling(min2.getLeftSibling());
			min2.setLeftSibling(l);
			
			if(min.getDistcance() > min2.getDistcance())
				min = min2;
		}
	}
	
	/**
	 * insert a new node to the heap
	 * @param index represents the index of the new node
	 * @param dist represents the value of distance of the new node
	 */
	public void insert(int index, double dist)
	{
		Fnode fnode = new Fnode(index, dist);
		FibonacciHeap h = new FibonacciHeap(fnode);
		this.union(h);
	}
	
	/**
	 * an ancillary function of extracMin(),
	 * link the nodes having the same degree in the root list
	 */
	public void consolidate()
	{
		//possible maximum degree for a F-heap with "size" nodes
		int bucketSize = (int) (Math.log(size) / Math.log(2)) + 2; 	
		//degreeMap[i] means the node whose degree is i
		Fnode[] degreeMap = new Fnode[bucketSize];					
		for(int i=0; i<bucketSize; i++)
			degreeMap[i] = null;
		
		int d;
		Fnode x = min;
		Fnode start = min;
		Fnode y, next;
		do
		{
			d = x.getDegree();
			next = x.getRightSibling();
			while(degreeMap[d] != null)
			{
				y = degreeMap[d];
				if(x.getDistcance() > y.getDistcance())		//exchange x, y if necessary (refer to link())
				{
					Fnode temp = y;
					y = x;
					x = temp;
				}
				if(y == start)								//maintain the ending mark for the loop
					start = start.getRightSibling();
				
				if(y == next)								//maintain the next pointer for x (should be in the root list)
					next = next.getRightSibling();
				this.link(y, x);
				degreeMap[d] = null;
				d ++;
			}
			degreeMap[d] = x;
			x = next;
		}while(x != start);
		
		
		min = null;
		for(int i=0; i<bucketSize; i++)		//adjust min 
		{
			if(min == null || (degreeMap[i] != null && min.getDistcance() > degreeMap[i].getDistcance()))
				min = degreeMap[i];
		}
	}
	
	/**
	 * remove the min node from the root lit
	 * @return the min node
	 */
	public Fnode extractMin()
	{
		Fnode z = min;
		if(z != null)
		{
			Fnode x = z.getChild();		// set children's parent to null
			if(x != null)
			{
				x.setParent(null);
				for(x = x.getRightSibling(); x != z.getChild(); x = x.getRightSibling())
					x.setParent(null);
			
			
				Fnode l = z.getLeftSibling();
				x = z.getChild();
			
				l.setRightSibling(x);	//add z's children to root list
				x.getLeftSibling().setRightSibling(z);
				z.setLeftSibling(x.getLeftSibling());
				x.setLeftSibling(l);
			}
			
			
			z.getLeftSibling().setRightSibling(z.getRightSibling());	//remove z from root list
			z.getRightSibling().setLeftSibling(z.getLeftSibling());
			
					
			//this means that z is the only root even after it children are added to root list (no children)
			if(z == z.getRightSibling())		
				min = null;
				
			else
			{
				min = z.getRightSibling();
				this.consolidate();		// adjustment of min is done in consolidate()
			}
			size--;
		}
		return z;
	}

	/**
	 * decrease the value of distance in node x to k
	 * @param x represents a node in the heap
	 * @param k is the new value of distance in x
	 */
	public void decreaseKey(Fnode x, double k)
	{
		if(k > x.getDistcance())
		{
			System.out.println("Error! New key is greater than current key.");
			System.exit(1);
		}
		
		x.setDistance(k);
		Fnode y = x.getParent();
		if(y != null && x.getDistcance() < y.getDistcance())
		{
			cut(x, y);			//move x to root list
			cascadingCut(y);	//cascading cut if necessary
		}
		
		if(x.getDistcance() < min.getDistcance())
			min = x;			//adjust min if necessary
	}
	
	/**
	 * cut node x from node y, and add x to the root list
	 * @param x represents x to be cut
	 * @param y is x' parent
	 */
	public void cut(Fnode x, Fnode y)  					//y is x's parent
	{
		Fnode z = y.getChild();							//remove x from the child list of y
		if (z == x && x.getRightSibling() == x)			//x is y's only child
			y.setChild(null);
		
		if (z == x && x.getRightSibling() != x)			//x is not the only child, but the pointer points to x
		{
			Fnode l = x.getLeftSibling();
			Fnode r = x.getRightSibling();
			l.setRightSibling(r);
			r.setLeftSibling(l);
			y.setChild(r);
		}
		
		if(z != x)				//x is not the only child, and the pointer does not point to x
		{
			Fnode l = x.getLeftSibling();
			Fnode r = x.getRightSibling();
			
			l.setRightSibling(r);
			r.setLeftSibling(l);
		}
		
		y.decraseDegree();
		
		x.setRightSibling(min);						//add x to root list, as y exists, min is not null
		min.getLeftSibling().setRightSibling(x);
		x.setLeftSibling(min.getLeftSibling());
		min.setLeftSibling(x);
		
		x.setParent(null);
		x.setChildCut(false);
	}
	
	/**
	 * cascade y if its mark is set true, which means y has already lost one child before
	 * @param y a node just lost one child
	 */
	public void cascadingCut(Fnode y)
	{
		Fnode z = y.getParent();
		if(z != null)
		{
			if(y.getChildCut() == false)
				y.setChildCut(true);
			
			else
			{
				this.cut(y, z);
				this.cascadingCut(z);
			}
		}
	}
	
	/**
	 * deleter x from the heap
	 * @param x represents the node to be deleted
	 */
	public void delete(Fnode x)
	{
		this.decreaseKey(x, Double.NEGATIVE_INFINITY);
		this.extractMin();
	}
	
	/**
	 * search a node in the heap rooted at x
	 * @param index the index value of node to be searched
	 * @param x the root of the heap
	 * @return the found node
	 */
	public Fnode searchNode(int index, Fnode x) 
	{
		Fnode wantedNode = null;
		if(x != null)
		{
			Fnode y  = x;
			do
			{
				if(y.getIndex() == index)
					return y;
				if((wantedNode = searchNode(index, y.getChild())) != null)
					return wantedNode;
				y = y.getRightSibling();
				
			}while (y != x);
		}
		return wantedNode;	
	}
	
	/**
	 * print the heap rooted at x
	 * @param x the root of the heap to be printed
	 */
	public void print(Fnode x)
	{
		Fnode y = x;
		if(x != null)
		{
			do
			{
				System.out.print(y.getIndex() + "\t");
				print(y.getChild());
				
				System.out.println();
				
				y = y.getRightSibling();
			}
			while(y != x);
		}
	}
	
}
