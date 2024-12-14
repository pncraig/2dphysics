package tests;

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
	public boolean mouseOver(Vec2 mousePosition);
}
