package tests;

import java.util.Stack;
import java.util.List;

import physics.ParticleSystem;

import physics.PivotedSpring;
import physics.Particle;
import physics.Vec2;

import java.awt.Graphics2D;

import physics.AABB;

public class SimPivotedSpring extends SimObject{
	private static Stack<SimObject> selectedObjects = new Stack<>();
	
	public static SimPivotedSpring getInstance(SimObject selected) {
		if (selected == null || (selected.getType() != SimObject.Types.SIM_PARTICLE && selected.getType() != SimObject.Types.SIM_PIVOT)) {
			selectedObjects.clear();
			return null;
		}
		
		selectedObjects.push(selected);
		
		if (selectedObjects.size() < 2) {
			return null;
		}
		
		SimObject a = selectedObjects.pop();
		SimObject b = selectedObjects.pop();
		
		if (a.getType() == SimObject.Types.SIM_PARTICLE && b.getType() == SimObject.Types.SIM_PARTICLE ||
			a.getType() == SimObject.Types.SIM_PIVOT && b.getType() == SimObject.Types.SIM_PIVOT) {
			selectedObjects.push(a);
			return null;
		}
		
		selectedObjects.clear();
		if (a.getType() == SimObject.Types.SIM_PARTICLE && b.getType() == SimObject.Types.SIM_PIVOT) {
			SimPivotedSpring newSpring = new SimPivotedSpring(new PivotedSpring((Particle)a.getPhysicsObject(), (Vec2)b.getPhysicsObject(), 150.0, 100.0));
			newSpring.addReferencedObject(a);
			newSpring.addReferencedObject(b);
			a.addReferencedObject(newSpring);
			b.addReferencedObject(newSpring);
			return newSpring;
		} else if (a.getType() == SimObject.Types.SIM_PIVOT && b.getType() == SimObject.Types.SIM_PARTICLE) {
			SimPivotedSpring newSpring = new SimPivotedSpring(new PivotedSpring((Particle)b.getPhysicsObject(), (Vec2)a.getPhysicsObject(), 150.0, 100.0));
			newSpring.addReferencedObject(a);
			newSpring.addReferencedObject(b);
			a.addReferencedObject(newSpring);
			b.addReferencedObject(newSpring);
			return newSpring;
		}
		
		
		return null;
	}
	
	
	private PivotedSpring s;
	
	public SimPivotedSpring(PivotedSpring s) {
		this.s = s;
	}
	
	@Override
	public void addToSystem(ParticleSystem ps) {
		ps.addForce(this.s);
	}
	
	@Override
	public List<SimObject> removeFromSystem(ParticleSystem ps) {
		ps.removeForce(this.s);
		return null;
	}
	
	@Override
	public void setPosition(Vec2 p) {
		// Do nothing
	}
	
	@Override
	public boolean mouseOver(Vec2 mousePosition) {
		Vec2[] points = GraphicsUtils.getPointsAroundSpring(
				this.s.getParticle().getPosition(), 
				this.s.getPivot(), 
				this.s.getSpringConstant(), 
				10,
				SimPivot.PIVOT_RADIUS);
		
		return GraphicsUtils.pointOverRectangle(mousePosition, points[0], points[1], points[2], points[3]);
	}
	
	@Override
	public void drawHighlighted(Graphics2D g, Vec2 mousePosition) {
		if (this.mouseOver(mousePosition)) {
			Vec2[] points = GraphicsUtils.getPointsAroundSpring(
					this.s.getParticle().getPosition(), 
					this.s.getPivot(), 
					this.s.getSpringConstant(), 
					10,
					SimPivot.PIVOT_RADIUS);
			
			g.drawLine((int)points[0].getX(), (int)points[0].getY(), (int)points[1].getX(), (int)points[1].getY());
			g.drawLine((int)points[1].getX(), (int)points[1].getY(), (int)points[2].getX(), (int)points[2].getY());
			g.drawLine((int)points[2].getX(), (int)points[2].getY(), (int)points[3].getX(), (int)points[3].getY());
			g.drawLine((int)points[3].getX(), (int)points[3].getY(), (int)points[0].getX(), (int)points[0].getY());
		}
	}
	
	@Override
	public void draw(Graphics2D g) {
		GraphicsUtils.drawSpring(g, this.s.getPivot(), this.s.getParticle().getPosition(), this.s.getSpringConstant(), this.s.getLength());
	}
	
	@Override
	public void drawSelected(Graphics2D g) {
		AABB box = this.getAABB();
		g.drawRect((int)box.a.getX(), (int)box.a.getY(), (int)(box.b.getX() - box.a.getX()), (int)(box.b.getY() - box.a.getY()));
	}
	
	@Override
	public AABB getAABB() {
		Particle p = this.s.getParticle();
		Vec2 pivot = this.s.getPivot();
		Vec2 topLeft = new Vec2(
				(p.getPosition().getX() < pivot.getX()) ? p.getPosition().getX() : pivot.getX(),
				(p.getPosition().getY() < pivot.getY()) ? p.getPosition().getY() : pivot.getY());
		Vec2 bottomRight = new Vec2(
				(p.getPosition().getX() > pivot.getX()) ? p.getPosition().getX() : pivot.getX(),
				(p.getPosition().getY() > pivot.getY()) ? p.getPosition().getY() : pivot.getY());
		
		return new AABB(
				topLeft.getX(),
				topLeft.getY(),
				bottomRight.getX(),
				bottomRight.getY());
	}
	
	@Override
	public SimObject.Types getType() {
		return SimObject.Types.SIM_PIVOTED_SPRING;
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



















