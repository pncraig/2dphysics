package tests;

import java.awt.Graphics2D;

import java.awt.event.MouseEvent;

import physics.Particle;
import physics.PivotedSpring;

public class EditorUnpausedMode implements EditorMode {
	private SimulationEditor se;
	private EditorFrame ef;
	private SimPivotedSpring mouseSpring;
	
	public EditorUnpausedMode(SimulationEditor se, EditorFrame ef) {
		this.se = se;
		this.ef = ef;
		
		this.mouseSpring = null;
	}
	
	@Override
	public void mousePressed(int mask) {
		if (this.mouseSpring != null) {
			PivotedSpring s = (PivotedSpring)this.mouseSpring.getPhysicsObject();
			s.getPivot().setX(this.se.getMousePosition().getX());
			s.getPivot().setY(this.se.getMousePosition().getY());
		}
	}
	
	@Override
	public void mouseReleased(int mask) {
		if (this.mouseSpring != null) {
			this.se.removeSimObject(this.mouseSpring);
			this.mouseSpring = null;
		}
	}
	
	@Override
	public void mouseClicked(int mask) {
		SimParticle p = this.se.getClosestSimParticle();
		
		if (p != null) {
			this.mouseSpring = new SimPivotedSpring(new PivotedSpring((Particle)p.getPhysicsObject(), this.se.getMousePosition(), 150, 100));
			this.se.addSimObject(this.mouseSpring);
		}
	}
	
	@Override
	public void mouseDoubleClicked(int mask) {

	}
	
	@Override
	public void draw(Graphics2D g) {
		if (this.mouseSpring != null) {
			g.drawOval((int)this.se.getMousePosition().getX() - 5, 
					   (int)this.se.getMousePosition().getY() - 5,
					   10, 10);
		}
	}
	
	@Override
	public void enter() {
		//this.ef.setVisible(false);
		this.se.clearObjectEditors();
		//this.ef.setLocation(2000, 2000);
	}
	
	@Override
	public void exit() {
		//this.ef.setVisible(true);
		//this.ef.setLocation(0, 0);
	}
}
