package physics;

// TODO: Add getters (mayber not setters)
// TODO: Add verification that b isn't to the left or above a

/**
 * This class represents an axis-aligned 
 * bounding-box.
 * 
 * @author pncra
 */
public class AABB {
	public Vec2 a;
	public Vec2 b;
	
	public AABB(double ax, double ay, double bx, double by) {
		this.a = new Vec2(ax, ay);
		this.b = new Vec2(bx, by);
	}
	
	public AABB(Vec2 a, Vec2 b) {
		this.a = new Vec2(a);
		this.b = new Vec2(b);
	}
}
