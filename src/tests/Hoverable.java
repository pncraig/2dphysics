package tests;

import java.awt.Graphics2D;

import physics.Vec2;

/**
 * This interface has one function, mouseOver().
 * Classes that implement this interface must have a 
 * function that returns true if the mouse is hovering
 * over an object of the class, and false otherwise.
 * @author pncra
 *
 */
public interface Hoverable {
	/**
	 * Returns true if the mouse is hovering over this hoverable,
	 * false otherwise.
	 * 
	 * @param mousePosition the position of the mouse
	 * @return true if the mouse is hovering over the hoverable
	 */
	public boolean mouseOver(Vec2 mousePosition);
	
	/**
	 * Draw the hoverable when the mouse is over it.
	 * 
	 * @param g the graphics context to draw to
	 */
	public void drawHighlighted(Graphics2D g, Vec2 mousePosition);
}
