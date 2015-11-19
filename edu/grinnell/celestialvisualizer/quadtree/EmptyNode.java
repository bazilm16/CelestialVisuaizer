    package edu.grinnell.celestialvisualizer.quadtree;
    import edu.grinnell.celestialvisualizer.util.BoundingBox;
    import edu.grinnell.celestialvisualizer.util.Point;
    import edu.grinnell.celestialvisualizer.util.Vector2d;

    public class EmptyNode implements Node{
    	
    	/**
    	 * This method returns false because an empty node
    	 * does't exist
    	 * @param pos, the position of the element
    	 * @param bb, the bounding box we are checking
    	 * @return returns false
    	 */
    	@Override
    	public boolean lookup(Point pos, BoundingBox bb) {
    		return false;
    	}
    	
    	/**
    	 *  @param p the point we are calculating the acceleration over
        * @param bb the bounding box of the world
        * @param thresh the threshold value, defined above
        * @return a 0 vector because it is an empty niode
        */
    	@Override
    	public Vector2d calculateAcceleration(Point p, BoundingBox bb, double thresh) {
    		return Vector2d.zero;
    	}

    	/**
         * Inserts the given body (as a mass) into the quad tree.
         * 
         * @param mass the mass of the body
         * @param p the position of the body
         * @param bb the bounding box of the world
         * @return the new quad tree (as a node) that results from inserting this
         *         body into the tree.
         */
    	@Override
    	public Node insert(double mass, Point p, BoundingBox bb) {
    		return new LeafNode(mass, p);
    	}

    }

