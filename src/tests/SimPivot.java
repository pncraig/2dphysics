package tests;

import java.util.List;

import physics.Vec2;
import physics.ParticleSystem;

import java.awt.Graphics2D;

import javax.swing.JInternalFrame;

import physics.AABB;
import physics.Particle;

public class SimPivot extends SimObject {
	public static final int PIVOT_RADIUS = 5;
	
	private Vec2 x;
	
	public SimPivot(Vec2 x) {
		this.x = new Vec2();
		this.setPosition(x);
	}
	
	/**
	 * Returns a new SimPivot.
	 * 
	 * @param mousePosition the position to spawn the pivot at
	 * @return a new SimPivot
	 */
	public static SimPivot getInstance(Vec2 mousePosition) {
		return new SimPivot(mousePosition);
	}
	
	@Override
	public void addToSystem(ParticleSystem ps) { 
		// Do nothing, pivots aren't represented in the ParticleSystem
	}
	
	@Override
	public List<SimObject> removeFromSystem(ParticleSystem ps) {
		return this.getReferencedObjects();
	}
	
	@Override
	public void setPosition(Vec2 p) {
		this.x.setX(p.getX());
		this.x.setY(p.getY());
	}
	
	@Override
	public boolean mouseOver(Vec2 mousePosition) {
		double dist = Vec2.sub(this.x, mousePosition).mag();
		return dist <= PIVOT_RADIUS;	
	}
	
	@Override
	public void draw(Graphics2D g) {
		g.drawOval((int)this.x.getX() - PIVOT_RADIUS, (int)this.x.getY() - PIVOT_RADIUS, 2 * PIVOT_RADIUS, 2 * PIVOT_RADIUS);
	}
	
	@Override
	public void drawHighlighted(Graphics2D g, Vec2 mousePosition) {
		if (this.mouseOver(mousePosition)) {
			g.drawOval((int)this.x.getX() - PIVOT_RADIUS / 2, (int)this.x.getY() - PIVOT_RADIUS / 2, 
					PIVOT_RADIUS, PIVOT_RADIUS);
		}
	}
	
	@Override
	public void drawSelected(Graphics2D g) {
		AABB box = this.getAABB();
		g.drawRect((int)box.a.getX(), (int)box.a.getY(), (int)(box.b.getX() - box.a.getX()), (int)(box.b.getY() - box.a.getY()));
	}
	
	@Override
	public AABB getAABB() {
		return new AABB(
				this.x.getX() - PIVOT_RADIUS,
				this.x.getY() - PIVOT_RADIUS,
				this.x.getX() + PIVOT_RADIUS,
				this.x.getY() + PIVOT_RADIUS);				
	}
	
	@Override
	public SimObject.Types getType() {
		return SimObject.Types.SIM_PIVOT;
	}
	
	@Override
	public Object getPhysicsObject() {
		return this.x;
	}
	
	@Override
	public void openObjectEditor(SimulationEditor se) {
		if (this.getEditorShowing()) {
			return;
		}
		
		this.setEditorShowing(true);
		
		ObjectEditor oe = new ObjectEditor("Object Editor", this);
		
		VectorField position = new VectorField("Position", this.x);
		
		oe.addField(position);

		se.addObjectEditor(oe.build(this.x));
	}
	
	@Override
	public void updateField(ObjectEditorField field) {
		switch (field.getFieldName()) {
		case "Position":
			this.setPosition((Vec2)field.getValue());
			break;
		default:
			// Do nothing
			break;
		}
	}
}










