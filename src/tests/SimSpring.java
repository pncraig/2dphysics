package tests;

import java.util.Stack;
import java.util.List;

import java.awt.Graphics2D;

import physics.Vec2;
import physics.ParticleSystem;
import physics.AABB;
import physics.Particle;
import physics.Spring;

// TODO: Update to casts to GUIParticle/GUIPivot

public class SimSpring extends SimObject {
	private static Stack<SimParticle> selectedObjects = new Stack<>();
	
	public static SimSpring getInstance(SimObject selected) {
		// If no object is selected or the object selected isn't a particle,
		// ignore it and clear the stack
		if (selected == null || selected.getType() != SimObject.Types.SIM_PARTICLE) {
			selectedObjects.clear();
			return null;
		}
	
		// Push the SimParticle onto the stack
		selectedObjects.push((SimParticle)selected);

		// We need two SimParticles to make a spring
		if (selectedObjects.size() < 2) {
			return null;
		}
		
		SimParticle a = selectedObjects.pop();
		SimParticle b = selectedObjects.pop();
		
		// If the SimParticles have the same Particle, it means they are the same. Push one
		// back on the stack and return null
		if (((Particle)a.getPhysicsObject()).equals((Particle)b.getPhysicsObject())) {
			selectedObjects.push(a);
			return null;
		}
		
		selectedObjects.clear();
		
		SimSpring newSpring = new SimSpring(new Spring((Particle)a.getPhysicsObject(), (Particle)b.getPhysicsObject(), 150, 100));
		newSpring.addReferencedObject(a);
		newSpring.addReferencedObject(b);
		a.addReferencedObject(newSpring);
		b.addReferencedObject(newSpring);
		
		return newSpring;
	}
	
	private Spring s;
	
	public SimSpring(Spring s) {
		this.s = s;
	}
	
	@Override
	public void addToSystem(ParticleSystem ps) {
		ps.addForce(s);
	}
	
	@Override
	public List<SimObject> removeFromSystem(ParticleSystem ps) {
		ps.removeForce(this.s);
		
		// Don't return the list of objects referencing this Spring, because
		// all of them are particles and you don't need to remove the particles
		// that are attached to the spring when the spring is removed
		return null;
	}
	
	@Override
	public void setPosition(Vec2 p) {
		// Do nothing, for now. In the future, I might want to move
		// the particles attached to this spring
	}
	
	@Override
	public boolean mouseOver(Vec2 mousePosition) {
		Particle[] particles = this.s.getParticles();
		
		Vec2[] points = GraphicsUtils.getPointsAroundSpring(
				particles[0].getPosition(), 
				particles[1].getPosition(), 
				this.s.getSpringConstant(), 
				10,
				10);
		
		return GraphicsUtils.pointOverRectangle(mousePosition, points[0], points[1], points[2], points[3]);
	}
	
	@Override
	public void drawHighlighted(Graphics2D g, Vec2 mousePosition) {
		if (this.mouseOver(mousePosition)) {
			Particle[] particles = this.s.getParticles();
			
			Vec2[] points = GraphicsUtils.getPointsAroundSpring(
					particles[0].getPosition(), 
					particles[1].getPosition(), 
					this.s.getSpringConstant(), 
					10,
					10);
			
			g.drawLine((int)points[0].getX(), (int)points[0].getY(), (int)points[1].getX(), (int)points[1].getY());
			g.drawLine((int)points[1].getX(), (int)points[1].getY(), (int)points[2].getX(), (int)points[2].getY());
			g.drawLine((int)points[2].getX(), (int)points[2].getY(), (int)points[3].getX(), (int)points[3].getY());
			g.drawLine((int)points[3].getX(), (int)points[3].getY(), (int)points[0].getX(), (int)points[0].getY());
		}
		
		
	}
	
	@Override
	public void draw(Graphics2D g) {
		Particle[] particles = this.s.getParticles();
		GraphicsUtils.drawSpring(g, particles[0].getPosition(), particles[1].getPosition(), this.s.getSpringConstant(), this.s.getLength());
	}
	
	@Override
	public void drawSelected(Graphics2D g) {
		AABB box = this.getAABB();
		g.drawRect((int)box.a.getX(), (int)box.a.getY(), (int)(box.b.getX() - box.a.getX()), (int)(box.b.getY() - box.a.getY()));
	}
	
	@Override
	public AABB getAABB() {
		Particle[] particles = this.s.getParticles();
		Vec2 topLeft = new Vec2(
				(particles[0].getPosition().getX() < particles[1].getPosition().getX()) ? particles[0].getPosition().getX() : particles[1].getPosition().getX(),
				(particles[0].getPosition().getY() < particles[1].getPosition().getY()) ? particles[0].getPosition().getY() : particles[1].getPosition().getY());
		Vec2 bottomRight = new Vec2(
				(particles[0].getPosition().getX() > particles[1].getPosition().getX()) ? particles[0].getPosition().getX() : particles[1].getPosition().getX(),
				(particles[0].getPosition().getY() > particles[1].getPosition().getY()) ? particles[0].getPosition().getY() : particles[1].getPosition().getY());
		return new AABB(
				topLeft.getX(),
				topLeft.getY(),
				bottomRight.getX(),
				bottomRight.getY());
	}
	
	@Override
	public SimObject.Types getType() {
		return SimObject.Types.SIM_SPRING;
	}
	
	@Override
	public Object getPhysicsObject() {
		return this.s;
	}
	
	@Override
	public void openObjectEditor(SimulationEditor se) {
		if (this.getEditorShowing()) {
			return;
		}
		
		this.setEditorShowing(true);
		
		ObjectEditor oe = new ObjectEditor("Object Editor", this);
		
		NumberField restLength = new NumberField("Rest Length", this.s.getLength());
		NumberField springConstant = new NumberField("Spring Constant", this.s.getSpringConstant());
		
		oe.addField(restLength);
		oe.addField(springConstant);

		se.addObjectEditor(oe.build(se.getMousePosition()));
	}
	
	@Override
	public void updateField(ObjectEditorField field) {
		switch (field.getFieldName()) {
		case "Rest Length":
			this.s.setLength((double)field.getValue());
			break;
		case "Spring Constant":
			this.s.setSpringConstant((double)field.getValue());
			break;
		default:
			// Do nothing
			break;
		}
	}
}
















