package tests;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

public class EditorInspectMode implements EditorMode {
	private SimulationEditor se;
	
	public EditorInspectMode(SimulationEditor se) {
		this.se = se;
	}
	
	@Override
	public void mousePressed(int mask) {
		if (this.se.getSelectedObject() != null) {
			this.se.getSelectedObject().setPosition(this.se.getMousePosition());
		}
	}
	
	@Override
	public void mouseReleased(int mask) {
		
	}
	
	@Override
	public void mouseClicked(int mask) {
		
	}
	
	@Override
	public void mouseDoubleClicked(int mask) {
		if (this.se.getSelectedObject() != null) {
			this.se.getSelectedObject().openObjectEditor(this.se);
		}
	}
	
	
	@Override
	public void draw(Graphics2D g) {
		SimObject mouseOverObject = this.se.getMouseOverObject();
		SimObject selectedObject = this.se.getSelectedObject();
		
		if (mouseOverObject != null) {
			mouseOverObject.drawHighlighted(g, this.se.getMousePosition());
		}
		
		if (selectedObject != null) {
			selectedObject.drawSelected(g);
		}
	}
	
	@Override
	public void enter() {
		
	}
	
	@Override
	public void exit() {
		
	}
}
