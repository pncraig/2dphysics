package tests;

import java.awt.Graphics2D;

import graphics.GameAction;

/**
 * An interface that represents a state the
 * editor could be in.
 * 
 * @author pncra
 */
public interface EditorState {
	/**
	 * Handle the input in the way input
	 * should be handled while in this state.
	 * 
	 * @param paused whether or not the editor is paused (in the future, if I add
	 * more possible editor states, this will be an object that has access to all
	 * the variables which determine the state of the editor)
	 */
	public EditorState handleInput(GameAction paused);
	
	/**
	 * Render the state.
	 */
	public void render(Graphics2D g);
	
	/**
	 * Called when this state is entered
	 */
	public void enter();
	
	/**
	 * Called when this state is exited
	 */
	public void exit();
}
