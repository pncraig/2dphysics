package tests;

import java.util.List;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.BoxLayout;

import java.awt.Graphics2D;

import physics.Particle;
import physics.ParticleSystem;
import physics.Vec2;
import physics.AABB;

public class SimParticle extends SimObject {
	private Particle p;
	private int r;
	
	public SimParticle(Particle p) {
		this.p = p;
		this.r = (int)(GraphicsUtils.MASS_CONSTANT * this.p.getMass());
	}
	
	/**
	 * Returns a new SimParticle.
	 * 
	 * @param mousePosition the position to spawn the particle at
	 * @return a new SimParticle
	 */
	public static SimParticle getInstance(Vec2 mousePosition) {
		return new SimParticle(new Particle(mousePosition.getX(), mousePosition.getY(), 0, 0, 10));
	}
	
	@Override
	public void addToSystem(ParticleSystem ps) {
		ps.addParticle(this.p);
	}
	
	@Override
	public List<SimObject> removeFromSystem(ParticleSystem ps) {
		ps.removeParticle(this.p);
		return this.getReferencedObjects();
	}
	
	@Override
	public void setPosition(Vec2 p) {
		this.p.setPosition(p);
	}
	
	@Override
	public boolean mouseOver(Vec2 mousePosition) {
		double dist = Vec2.sub(this.p.getPosition(), mousePosition).mag();
		return dist <= this.r;
	}
	
	@Override
	public void draw(Graphics2D g) {
		g.fillOval((int)(this.p.getPosition().getX() - this.r), (int)(this.p.getPosition().getY() - this.r), this.r * 2, this.r * 2);
	}
	
	@Override
	public void drawHighlighted(Graphics2D g, Vec2 mousePosition) {
		if (this.mouseOver(mousePosition)) {
			int rad = this.r;
			if (rad <= 0) {
				rad = 1;
			}
			g.drawOval((int)this.p.getPosition().getX() - rad, (int)this.p.getPosition().getY() - rad, rad * 2, rad * 2);
		}
	}
	
	@Override
	public void drawSelected(Graphics2D g) {
		AABB box = this.getAABB();
		g.drawRect((int)box.a.getX(), (int)box.a.getY(), (int)(box.b.getX() - box.a.getX()), (int)(box.b.getY() - box.a.getY()));
	}
	
	@Override
	public AABB getAABB() {
		return  new AABB(
				this.p.getPosition().getX() - this.r,
				this.p.getPosition().getY() - this.r,
				this.p.getPosition().getX() + this.r,
				this.p.getPosition().getY() + this.r);
	}
	
	@Override
	public SimObject.Types getType() {
		return SimObject.Types.SIM_PARTICLE;
	}
	
	@Override
	public Object getPhysicsObject() {
		return this.p;
	}
	
	@Override
	public void openObjectEditor(SimulationEditor se) {
		if (this.getEditorShowing()) {
			return;
		}
		
		this.setEditorShowing(true);
		
		ObjectEditor oe = new ObjectEditor("Object Editor", this);
		
		VectorField position = new VectorField("Position", this.p.getPosition());
		VectorField velocity = new VectorField("Velocity", this.p.getVelocity());
		NumberField mass = new NumberField("Mass", this.p.getMass());
		
		oe.addField(position);
		oe.addField(velocity);
		oe.addField(mass);

		se.addObjectEditor(oe.build(this.p.getPosition()));
	}
	
	@Override
	public void updateField(ObjectEditorField field) {
		switch (field.getFieldName()) {
		case "Position":
			System.out.println("Changed position!");
			this.p.setPosition((Vec2)field.getValue());
			break;
		case "Velocity":
			this.p.setVelocity((Vec2)field.getValue());
			break;
		case "Mass":
			this.p.setMass((Double)field.getValue());
			break;
		default:
			// Don't do anything in the default case
			break;
		}
	}
}
