package physics;

public class GravityAttraction implements Force {
	private Particle a;
	private Particle b;
	private double G;
	
	public GravityAttraction(Particle a, Particle b, double G) {
		this.a = a;
		this.b = b;
		this.G = G;
	}
	
	@Override
	public void applyForce() {
		Vec2 r = Vec2.sub(this.b.getPosition(), this.a.getPosition());
		double fmag = (this.G * this.a.getMass() * this.b.getMass()) / (r.mag() * r.mag());
		Vec2 f1 = Vec2.mult(r.unit(), fmag);
		this.a.addForce(f1);
		f1.mult(-1);
		this.b.addForce(f1);
	}
}
