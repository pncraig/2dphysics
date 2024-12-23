package tests;

import graphics.InputManager;

import java.awt.Graphics2D;

public class EditorRemoveMode implements EditorMode {
	private SimulationEditor se;
	private EditorFrame ef;
	
	public EditorRemoveMode(SimulationEditor se, EditorFrame ef) {
		this.se = se;
		this.ef = ef;
	}
	
	@Override
	public void mousePressed(int mask) {
		
	}
	
	@Override
	public void mouseReleased(int mask) {
		
	}
	
	@Override
	public void mouseClicked(int mask) {
		if ((mask & InputManager.MOUSE_BUTTON_1) == InputManager.MOUSE_BUTTON_1) {
			SimObject o = this.se.getSelectedObject();
			if (o != null) {
				this.se.removeSimObject(o);
			}
		}
	}
	
	@Override
	public void mouseDoubleClicked(int mask) {
		
	}

	@Override
	public void draw(Graphics2D g) {
		SimObject mouseOverObject = this.se.getMouseOverObject();
		
		if (mouseOverObject != null) {
			mouseOverObject.drawHighlighted(g, this.se.getMousePosition());
		}
		
	}
	
	@Override
	public void enter() {
		
	}
	
	@Override
	public void exit() {
		
	}
}
