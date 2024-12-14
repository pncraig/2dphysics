package tests;

import java.awt.Graphics2D;

import physics.Particle;
import physics.Vec2;

/**
 * This class represents a particle but has some additional methods
 * for drawing the particle and detecting collisions with the mouse.
 * @author pncra
 *
 */
public class GUIParticle extends Particle implements Drawable, Hoverable {
	private static final int EXTRA_RADIUS = 3;
	private int r;
	
	public GUIParticle(Particle p) {
		super(p);
		
		this.r = (int)p.getMass();
	}
	
	/**
	 * Check if the mouse is hovering over this particles. Checks if
	 * the distance between the mouse pointer and the center of the particle
	 * is less than the radius.
	 * 
	 * @param mousePosition the position of the mouse pointer
	 * @return true if the mouse is hovering over the particles, false otherwise
	 */
	public boolean mouseOver(Vec2 mousePosition) {
		double dist = Vec2.sub(this.getPosition(), mousePosition).mag();
		return dist <= this.r;
	}
	
	/**
	 * Draw this particle.
	 * 
	 * @param g the Graphics object to draw to
	 */
	public void draw(Graphics2D g) {
		g.fillOval((int)(this.getPosition().getX() - this.r), (int)(this.getPosition().getY() - this.r), this.r * 2, this.r * 2);
	}
	
	@Override
	public void drawHighlighted(Graphics2D g, Vec2 mousePosition) {
		if (this.mouseOver(mousePosition)) {
			int rad = this.r + EXTRA_RADIUS;
			if (rad <= 0) {
				rad = 1;
			}
			g.drawOval((int)this.getPosition().getX() - rad, (int)this.getPosition().getY() - rad, rad * 2, rad * 2);
		}
	}
	
	/**
	 * Get the radius of this particle.
	 * 
	 * @return the radius of this particle
	 */
	public double getRadius() {
		return this.r;
	}
}
