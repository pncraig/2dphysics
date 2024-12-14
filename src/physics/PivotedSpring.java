package physics;

/**
 * This force models a spring that is attached on 
 * one end to an immovable object.
 * @author pncra
 *
 */
public class PivotedSpring extends Force {
	private Particle p;
	private Vec2 pivot;
	private double k;
	private double l;
	
	public PivotedSpring(Particle p, Vec2 pivot, double k, double l) {
		this.p = p;
		this.pivot = pivot;
		this.k = k;
		this.l = l;
	}
	
	public PivotedSpring(PivotedSpring s) {
		this.p = s.getParticle();
		this.pivot = new Vec2(s.getPivot());
		this.k = s.getSpringConstant();
		this.l = s.getLength();
	}
	
	/**
	 * Get the pivot point of the spring.
	 * 
	 * @return the pivot point of the spring
	 */
	public Vec2 getPivot() {
		return new Vec2(this.pivot);
	}
	
	/**
	 * Get the particle attached to the spring.
	 * 
	 * @return the particle attached to the spring
	 */
	public Particle getParticle() {
		return this.p;
	}
	
	/**
	 * Get the spring constant of this spring.
	 * 
	 * @return the spring constant of this spring
	 */
	public double getSpringConstant() {
		return this.k;
	}
	
	/**
	 * Get the resting length of this spring.
	 * 
	 * @return the resting length of this spring
	 */
	public double getLength() {
		return this.l;
	}
	
	@Override
	public void applyForce() {
		if (this.p == null) {
			return;
		}
		
		Vec2 r = Vec2.sub(this.pivot, this.p.getPosition());
        double fmag = -this.k * (this.l - r.mag());
        Vec2 f= Vec2.mult(r.unit(), fmag);
        this.p.addForce(f);
	}
}
