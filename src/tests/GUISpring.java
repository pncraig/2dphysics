package tests;

import java.awt.Graphics2D;

import physics.Spring;
import physics.Particle;

import physics.Vec2;

public class GUISpring extends Spring implements Drawable, Hoverable {
	public GUISpring(Spring s) {
		super(s);
	}
	
	@Override
	public boolean mouseOver(Vec2 mousePosition) {
		Particle[] particles = this.getParticles();
		
		Vec2[] points = GraphicsUtils.getPointsAroundSpring(
				particles[0].getPosition(), 
				particles[1].getPosition(), 
				this.getSpringConstant(), 
				((GUIParticle)particles[0]).getRadius(),
				((GUIParticle)particles[1]).getRadius());
		
		return GraphicsUtils.pointOverRectangle(mousePosition, points[0], points[1], points[2], points[3]);
	}
	
	@Override
	public void drawHighlighted(Graphics2D g, Vec2 mousePosition) {
		if (this.mouseOver(mousePosition)) {
			Particle[] particles = this.getParticles();
			
			Vec2[] points = GraphicsUtils.getPointsAroundSpring(
					particles[0].getPosition(), 
					particles[1].getPosition(), 
					this.getSpringConstant(), 
					((GUIParticle)particles[0]).getRadius(),
					((GUIParticle)particles[1]).getRadius());
			
			g.drawLine((int)points[0].getX(), (int)points[0].getY(), (int)points[1].getX(), (int)points[1].getY());
			g.drawLine((int)points[1].getX(), (int)points[1].getY(), (int)points[2].getX(), (int)points[2].getY());
			g.drawLine((int)points[2].getX(), (int)points[2].getY(), (int)points[3].getX(), (int)points[3].getY());
			g.drawLine((int)points[3].getX(), (int)points[3].getY(), (int)points[0].getX(), (int)points[0].getY());
		}
		
		
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
