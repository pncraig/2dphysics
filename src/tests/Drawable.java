package tests;

import java.awt.Graphics2D;

/**
 * Classes that implement this interface can be 
 * drawn to a graphics context
 * 
 * @author pncra
 */
public interface Drawable {
	/**
	 * Draws to the graphics context.
	 * 
	 * @param g the graphics context to draw to
	 */
	public void draw(Graphics2D g);
}
