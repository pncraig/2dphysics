package tests;

import java.awt.Graphics2D;
import graphics.InputManager;

public class EditorSpawnMode implements EditorMode {
	private SimulationEditor se;
	private EditorFrame ef;
	
	public EditorSpawnMode(SimulationEditor se, EditorFrame ef) {
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
			SimObject.spawn(this.ef.getSpawnType(), this.se);
		}
	}
	
	@Override
	public void mouseDoubleClicked(int mask) {
		
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
		this.ef.spawnOptions.setEnabled(true);
	}
	
	@Override
	public void exit() {
		this.ef.spawnOptions.setEnabled(false);
	}
}
