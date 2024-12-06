package tests;

import java.awt.Graphics2D;

import physics.*;

public class PivotSpringTest extends TestHarness {
	public static void main(String[] args) {
		PivotSpringTest pst = new PivotSpringTest();
		pst.run();
	}
	
	private ParticleSystem ps;
	private NumericalSolver ns;
	
	private Particle a;
	private Particle b;
	
	private PivotedSpring s1;
	private Spring s2;
	private PivotedSpring mouseSpring;
	private Gravity g;
	
	
	private Vec2 mousePosition;
	
	@Override
	public void init() {
		super.init();
		
		GraphicsUtils.setSpringConstant(1000);
		GraphicsUtils.setMassConstant(10);
		
		this.mousePosition = new Vec2(this.im.getMouseX(), this.im.getMouseY());
		
		this.ps = new ParticleSystem();
		this.ns = new RK4Solver();
		
		this.a = new Particle(this.screen.getWidth() / 2 + 150 * Math.sin(Math.PI / 4), 100 + 150 * Math.cos(Math.PI / 4), 0, 0, 1);
		//this.b = new Particle(this.a.getPosition().getX() + 150 * Math.sin(Math.PI / 4), this.a.getPosition().getY() + 150 * Math.cos(Math.PI / 4), 0, 0, 1);
		this.b = new Particle(this.a.getPosition().getX() - 150, this.a.getPosition().getY(), 0, 0, 1);
		
		this.s1 = new PivotedSpring(this.a, new Vec2(this.screen.getWidth() / 2, 100), 50000, 150);
		this.s2 = new Spring(this.a, this.b, 50000, 150);
		this.mouseSpring = new PivotedSpring(this.b, this.mousePosition, 10, 150);
		
		this.g = new Gravity(this.ps, 981);
		
		this.ps.addParticle(
				this.a,
				this.b
		);
		
		this.ps.addForce(
				this.s1,
				this.s2,
				this.g,
				this.mouseSpring
		);
		
	}
	
	@Override
	public void update(long elapsedTime) {
		super.update(elapsedTime);
		
		double dt = elapsedTime / 1000.0;
		this.ns.step(this.ps, dt);
		
		this.mousePosition.setX(this.im.getMouseX());
		this.mousePosition.setY(this.im.getMouseY());
	}
	
	@Override
	public void draw(Graphics2D g) {
		GraphicsUtils.drawParticle(g, this.a);
		GraphicsUtils.drawParticle(g, this.b);
		GraphicsUtils.drawPivotedSpring(g, this.s1);
		GraphicsUtils.drawSpring(g, this.s2);
		GraphicsUtils.drawPivotedSpring(g, this.mouseSpring);
	}
}
