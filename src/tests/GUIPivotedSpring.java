package tests;

import java.awt.Graphics2D;

import physics.Particle;
import physics.PivotedSpring;
import physics.Vec2;

public class GUIPivotedSpring extends PivotedSpring implements Drawable, Hoverable {
	public GUIPivotedSpring(PivotedSpring s) {
		super(s);
	}
	
	@Override
	public boolean mouseOver(Vec2 mousePosition) {
		Vec2[] points = GraphicsUtils.getPointsAroundSpring(
				this.getParticle().getPosition(), 
				this.getPivot(), 
				this.getSpringConstant(), 
				((GUIParticle)this.getParticle()).getRadius(),
				GUIPivot.PIVOT_RADIUS);
		
		return GraphicsUtils.pointOverRectangle(mousePosition, points[0], points[1], points[2], points[3]);
	}
	
	@Override
	public void drawHighlighted(Graphics2D g, Vec2 mousePosition) {
		if (this.mouseOver(mousePosition)) {
			Vec2[] points = GraphicsUtils.getPointsAroundSpring(
					this.getParticle().getPosition(), 
					this.getPivot(), 
					this.getSpringConstant(), 
					((GUIParticle)this.getParticle()).getRadius(),
					GUIPivot.PIVOT_RADIUS);
			
			g.drawLine((int)points[0].getX(), (int)points[0].getY(), (int)points[1].getX(), (int)points[1].getY());
			g.drawLine((int)points[1].getX(), (int)points[1].getY(), (int)points[2].getX(), (int)points[2].getY());
			g.drawLine((int)points[2].getX(), (int)points[2].getY(), (int)points[3].getX(), (int)points[3].getY());
			g.drawLine((int)points[3].getX(), (int)points[3].getY(), (int)points[0].getX(), (int)points[0].getY());
		}
		
		
	}
	
	/**
	 * Draw this pivoted spring.
	 * 
	 * @param g the graphics context to draw to
	 */
	public void draw(Graphics2D g) {
		GraphicsUtils.drawSpring(g, this.getPivot(), this.getParticle().getPosition(), this.getSpringConstant(), this.getLength());
	}
}
