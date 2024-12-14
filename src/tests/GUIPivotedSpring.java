package tests;

import java.awt.Graphics2D;

import physics.PivotedSpring;

public class GUIPivotedSpring extends PivotedSpring implements Drawable {
	public GUIPivotedSpring(PivotedSpring s) {
		super(s);
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
