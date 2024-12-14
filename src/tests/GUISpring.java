package tests;

import java.awt.Graphics2D;

import physics.Spring;
import physics.Particle;

public class GUISpring extends Spring implements Drawable {
	public GUISpring(Spring s) {
		super(s);
	}
	
	/**
	 * Draw this spring.
	 * 
	 * @param g the Graphics2D object to draw to
	 */
	public void draw(Graphics2D g) {
		Particle[] particles = this.getParticles();
		GraphicsUtils.drawSpring(g, particles[0].getPosition(), particles[1].getPosition(), this.getSpringConstant(), this.getLength());
	}
}
