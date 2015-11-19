package edu.grinnell.celestialvisualizer.quadtree;

import edu.grinnell.celestialvisualizer.physics.Centroid;
import edu.grinnell.celestialvisualizer.util.BoundingBox;
import edu.grinnell.celestialvisualizer.util.Point;
import edu.grinnell.celestialvisualizer.util.Vector2d;

public class QuadTree {
	public Node root;
	public BoundingBox bb;
	public Point pos;
	public double mass;
	
	public QuadTree(){
		this.root = new EmptyNode();
		
	}

	/**
	 * Makes a tree with a leaf
	 * @return ret, a new quad tree with leaf
	 */
	public QuadTree makeTreeWithLeaf() {
		QuadTree ret = new QuadTree();
		ret.root = new LeafNode(1.0, new Point(2.0, 3.0));
		return ret;
	}

	/**
	 * Makes a tree without a leaf
	 * @return ret, a new quad tree without a leaf
	 */
	public static QuadTree makeTreeWithoutLeaf() {
		return  new QuadTree();
	}
	
	/**
	 * @return the root of the quad tree
	 * */
	public Node getRoot(){
		return this.root;
	}

	/**
     * Looks up this point in the quad tree, returning true if it is present
     * in the tree (as a leaf).
     * @param pos the point to search for
     * @param bb the bounding box encasing the world
     * @return true iff the point is in the quad tree
     */
	public boolean lookup(Point pos, BoundingBox bb) {
		if(root.equals(null)){
			return false;
		}else if(bb.contains(pos)){
			return true;
		}else{
			return false;
		}
	}

	/**
     * Calculates the acceleration on this point according to the quad tree.
     * @param p the point we are calculating the acceleration over
     * @param bb the bounding box of the world
     * @param thresh the threshold value, defined above
     * @return the acceleration on p by the quad tree
     */
	public Vector2d calculateAcceleration(Point p, BoundingBox bb, double eps) {
		if(root.equals(null)){
			return Vector2d.zero;
		}else{
			return root.calculateAcceleration(p, bb, eps);
		}
	}

	public void insert(double mass, Point pos, BoundingBox bb) {
		if(!this.lookup(pos, bb)){
			throw new IllegalArgumentException("position falls outside the bounding box");
		}else if(root == null){
			this.root = new LeafNode(mass, pos);
		}else{
			root = root.insert(mass, pos, bb);
		}
	}

	/**
     * @return true if this Node is equal to the given Object.
     */
	@Override
	public boolean equals(Object other) {
		if(other instanceof QuadTree){
			QuadTree node = (QuadTree) other;
			return this.root.equals(node.root);
		}else{
			return false;
		}
	}

	/**
	 * @return a new quad tree with no leaves
	 * */
	public static QuadTree q0() {
		return new QuadTree();
	}

	/**
	 * @return q1, a new quad tree with root
	 * */
	public static QuadTree q1() {
		QuadTree q1 = q0();
		q1.root = new LeafNode(1.0, p1);
		return q1;
	}

	public static final Point p1 = new Point(1.5, 2.5);
	public static final Point p2 = new Point(2.1, 2.1);

	public static final Centroid b1 = new Centroid(1.0, p1);
	public static final Centroid b2 = new Centroid(1.0, p2);

	/**
	 * @return q2, a quad tree with a centroid
	 * */
	public static QuadTree q2() {
		QuadTree q2 = q1();
		q2.root = new CentroidNode(b1.add(b2),
				new EmptyNode(),    // upper left
				new EmptyNode(),                           // upper right
				new LeafNode(1.0, p1),    // lower left
				new LeafNode(1.0, p2));   // lower right
		return q2;
	}

	public static final Point p3 = new Point(1.0, 1.0);
	public static final Point p4 = new Point(1.5, 2.5);

	public static final Centroid b3 = new Centroid(1.0, p1);
	public static final Centroid b4 = new Centroid(1.0, p2);
	public static final Centroid c1 = b2.add(b4);
	public static final Centroid c2 = c1.add(b3).add(b1);
	
	/**
	 * @return q3, a new quad tree with 3 centroids
	 * */
	public static QuadTree q3() {
		QuadTree q3 = q2();
		q3.root = new CentroidNode(c2,
				new LeafNode(2.0, p3),      // level 1—upper left
				new EmptyNode(),                             // level 1—upper right
				new LeafNode(1.0, p4),      // level 1—lower left
				new CentroidNode(c1,                         // level 1—lower right
						new CentroidNode(c1,                       //   level 2—upper left
								new LeafNode(1.0, new Point(2.1, 2.1)),  //     level 3—upper left
								new EmptyNode(),                         //     level 3—upper right
								new EmptyNode(),                         //     level 3—lower left
								new LeafNode(1.0, new Point(2.6, 2.8))   //     level 3—lower right
								),
						new EmptyNode(),                           //   level 2—upper right
						new EmptyNode(),                           //   level 2—lower left
						new EmptyNode()                            //   level 2—lower right
						)
				);
		return q3;
	}

	/**
	 * @return q4, a new quad tree with 4 centroids
	 * */
		public static QuadTree q4() {
			QuadTree q4 = q3();
			q4.root =  new CentroidNode(c2,
					     new LeafNode(2.0, new Point(1.0, 1.0)),      // level 1—upper left
					      new EmptyNode(),                             // level 1—upper right
					      new LeafNode(1.0, p1),      // level 1—lower left
					      new CentroidNode(c1,                         // level 1—lower right
					        new CentroidNode(c1,                       //   level 2—upper left
					          new LeafNode(1.0, p2),  //     level 3—upper left
					          new EmptyNode(),                         //     level 3—upper right
					          new EmptyNode(),                         //     level 3—lower left
					          new LeafNode(1.0, p3)   //     level 3—lower right
					        ),
					        new EmptyNode(),                           //   level 2—upper right
					        new EmptyNode(),                           //   level 2—lower left
					        new EmptyNode()                            //   level 2—lower right
					     ) );
			return q4;
		}


}
