package edu.grinnell.celestialvisualizer.quadtree;
import edu.grinnell.celestialvisualizer.physics.Centroid;
import edu.grinnell.celestialvisualizer.physics.Physics;
import edu.grinnell.celestialvisualizer.util.BoundingBox;
import edu.grinnell.celestialvisualizer.util.Point;
import edu.grinnell.celestialvisualizer.util.Vector2d;

public class LeafNode implements Node{

	private double mass;
	private Point point;
	
	/**
	 * Creates a new leaf node initialized to provided mass and point
	 * @param mass
	 * @param point
	 */
	public LeafNode(double mass, Point point){
		this.mass = mass;
		this.point = point;
	}
	
	/**
	 * @return returns true of bounding box contains the point
	 * @param Point pos
	 * @param BoundingBox bb
	 */
	
	@Override
	public boolean lookup(Point pos, BoundingBox bb) {
		return bb.contains(pos);
	}

	/**
	 * @return acceleration of LeafNode
	 * @param Point p
	 * @param BoundingBox bb
	 * @param double thresh
	 */
	@Override
	public Vector2d calculateAcceleration(Point p, BoundingBox bb, double thresh) {
		return Physics.calculateAccelerationOn(this.point, this.mass, p);
	}

	/**
	 * Inserts Node into the Quadtree based on whether the point to be inserted
	 * already exists in the boundingbox or not
	 * This also determines whether the new inserted node will include the existing
	 * point or whether an entirely new node will be created
	 * @return Node
	 * @param double mass
	 * @param Point p
	 * @param BoundingBox bb
	 */
	@Override
	public Node insert(double mass, Point p, BoundingBox bb) {
		if(!lookup(p,bb)){
			LeafNode New = new LeafNode(mass,p);
			LeafNode Old = new LeafNode(this.mass, point);
			
			Point newP = point.translate(point.distance(p).scale(this.mass/mass/(this.mass + mass + 1)));
			Centroid c = new Centroid((this.mass + mass), newP);
			
			return new CentroidNode(c, New, Old, new EmptyNode(), new EmptyNode());
		}
		else{
			return new LeafNode(mass,p);	
		}
	}
	
	/**
	 * @return returns true or false based on whether the compared Object equals the LeafNode's fields and type
	 * @param Object other
	 */
	public boolean equals(Object other) {
		if(other instanceof LeafNode){
			LeafNode node = (LeafNode) other;
			return this.mass == node.mass && 
					this.point.equals(node.point);
		}else{
			return false;
		}
	}
}