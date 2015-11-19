package edu.grinnell.celestialvisualizer.quadtree;
import edu.grinnell.celestialvisualizer.physics.Centroid;
import edu.grinnell.celestialvisualizer.physics.Physics;
import edu.grinnell.celestialvisualizer.util.BoundingBox;
import edu.grinnell.celestialvisualizer.util.Point;
import edu.grinnell.celestialvisualizer.util.Quadrant;
import edu.grinnell.celestialvisualizer.util.Vector2d;

public class CentroidNode implements Node{
	private Centroid cent;
	private Node upperLeft;
	private Node upperRight;
	private Node lowerLeft;
	private Node lowerRight;
	
	/**
	 * Constructs a new centroid Node
	 * @param cent
	 * @param upperLeft
	 * @param upperRight
	 * @param lowerLeft
	 * @param lowerRight
	 */
	public CentroidNode(Centroid cent, Node upperLeft, Node upperRight, Node lowerLeft, Node lowerRight){
		this.cent = cent;
		this.upperLeft = upperLeft;
		this.upperRight = upperRight;
		this.lowerLeft = lowerLeft;
		this.lowerRight = lowerRight;
	}
	
	/**
	 * Looks if any of the quadrants contain the point. If all are null it returns false.
	 * It looks in all quadrants using the lookup method
	 * @param Point pos
	 * @param BoundingBox bb
	 */
	@Override
	public boolean lookup(Point pos, BoundingBox bb) {
		if(this.upperLeft == null && this.upperRight == null &&
				this.lowerLeft == null && this.lowerRight == null){
			return false;
		}
		if(bb.contains(pos)){
			return true;
		}else{
			this.upperLeft.lookup(pos,bb);
			this.upperRight.lookup(pos,bb);
			this.lowerLeft.lookup(pos,bb);
			this.lowerRight.lookup(pos,bb);
		}
		return false;
	}
	
	/**
	 *  @param p the point we are calculating the acceleration over
    * @param bb the bounding box of the world
    * @param thresh the threshold value, defined above
    * @return the acceleration on the centroid node
    */
	@Override
	public Vector2d calculateAcceleration(Point p, BoundingBox bb, double thresh) {
		if(bb.contains(p) || this.cent.getPosition().distance(p).magnitude() < thresh){
			return upperLeft.calculateAcceleration(p, bb.getQuadrant(Quadrant.UPPER_LEFT), thresh)
					.add(upperRight.calculateAcceleration(p, bb.getQuadrant(Quadrant.UPPER_RIGHT), thresh))
					.add(lowerLeft.calculateAcceleration(p, bb.getQuadrant(Quadrant.LOWER_LEFT), thresh))
					.add(lowerRight.calculateAcceleration(p, bb.getQuadrant(Quadrant.LOWER_RIGHT), thresh));
		}else{
			return Physics.calculateAccelerationOn(p, this.cent.getMass(), this.cent.getPosition());
		}
	}
	
	/**
	 * @return true or false based on whether the fields for both Nodes are the same
	 * @param other (the node we're comparing to)
	 */
	
	@Override
	public boolean equals(Object other) {
		if(other instanceof CentroidNode){
			CentroidNode node = (CentroidNode) other;
			return this.upperLeft.equals(node.upperLeft) && this.upperRight.equals(node.upperRight) 
					&& this.lowerLeft.equals(node.lowerLeft) &&this.upperRight.equals(node.upperRight)
					&& this.cent.equals(node.cent);
		}else{
			return false;
		}
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
		//create the new centroid
		//this.cent = this.cent.add(new Centroid(mass,p));
		
		if(bb.quadrantOf(p) == Quadrant.UPPER_LEFT){
			this.upperLeft = this.upperLeft.insert(mass, p, bb.getQuadrant(Quadrant.UPPER_LEFT));
		}
		else if(bb.quadrantOf(p) == Quadrant.UPPER_RIGHT){
			this.upperRight = this.upperRight.insert(mass, p, bb.getQuadrant(Quadrant.UPPER_RIGHT));
		}
		else if(bb.quadrantOf(p) == Quadrant.LOWER_LEFT){
			this.lowerLeft = this.lowerLeft.insert(mass, p, bb.getQuadrant(Quadrant.LOWER_LEFT));
		}
		else if(bb.quadrantOf(p) == Quadrant.LOWER_RIGHT){
			this.lowerRight = this.lowerRight.insert(mass, p, bb.getQuadrant(Quadrant.LOWER_RIGHT));
		}
		return this;
	}
}
