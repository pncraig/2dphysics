package tests;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

/**
 * This interface defines the functions that
 * each editor mode should be able to perform.
 * 
 * @author pncra
 */
public interface EditorMode {
	
	public void mousePressed(int mask);
	
	public void mouseReleased(int mask);
	
	public void mouseClicked(int mask);
	
	public void mouseDoubleClicked(int mask);
	
	public void draw(Graphics2D g);
	
	public void enter();
	
	public void exit();
}
