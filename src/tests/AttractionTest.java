package tests;

import java.awt.Graphics2D;

import physics.*;

public class AttractionTest extends TestHarness {
	public static void main(String[] args) {
		AttractionTest t = new AttractionTest();
		t.run();
	}
	
	private ParticleSystem ps;
	private NumericalSolver ns;
	
	private Particle a;
	private Particle b;
	private GravityAttraction f;
	
	@Override
	public void init() {
		super.init();
		
		GraphicsUtils.setMassConstant(0.001);
		
		this.ps = new ParticleSystem();
		this.ns = new RK4Solver();
		
		this.a = new Particle(100, this.screen.getHeight() / 2, 0, 10, 10000);
		this.b = new Particle(this.screen.getWidth() - 100, this.screen.getHeight() / 2, 0, -10, 10000);
		this.f = new GravityAttraction(this.a, this.b, 100);
		
		this.ps.addParticle(this.a);
		this.ps.addParticle(this.b);
		this.ps.addForce(this.f);
	}
	
	@Override
	public void update(long elapsedTime) {
		super.update(elapsedTime);
		
		double dt = elapsedTime / 1000.0;
		this.ns.step(this.ps, dt);
	}
	
	@Override
	public void draw(Graphics2D g) {
		GraphicsUtils.drawParticle(g, this.a);
		GraphicsUtils.drawParticle(g, this.b);
	}
}
