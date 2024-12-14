package tests;

import java.awt.Graphics2D;

import graphics.GameAction;

public class UnpausedState implements EditorState {
	
	private SimulationEditor se;
	
	public UnpausedState(SimulationEditor se) {
		this.se = se;
	}
	
	@Override
	public EditorState handleInput(GameAction paused) {
		if (paused.isPressed()) {
			return this.se.PAUSED_STATE;
		}
		
		return null;
	}
	
	@Override
	public void render(Graphics2D g) {
		
	}
	
	@Override
	public void enter() {
		System.out.println("Enter unpaused state");
	}
	
	@Override
	public void exit() {
		System.out.println("Exit unpaused state");
	}
}
