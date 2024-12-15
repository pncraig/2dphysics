package tests;

import java.awt.Graphics2D;

import physics.Vec2;

public class GUIPivot implements Drawable, Hoverable {
	public static final int PIVOT_RADIUS = 8;
	public static final int HIGHLIGHTED_PIVOT_RADIUS = 12;
	
	private Vec2 position;
	
	public GUIPivot(double x, double y) {
		this.position = new Vec2(x, y);
	}
	
	public GUIPivot(Vec2 position) {
		this(position.getX(), position.getY());
	}
	
	/**
	 * Get the position of this pivot.
	 * 
	 * @return the position of this pivot as a vector
	 */
	public Vec2 getPosition() {
		return new Vec2(this.position);
	}
	
	/**
	 * Set the position of this pivot.
	 * 
	 * @param p the new position of this pivot
	 */
	public void setPosition(Vec2 p) {
		this.position = new Vec2(p);
	}
	
	@Override
	public boolean mouseOver(Vec2 mousePosition) {
		double dist = Vec2.sub(this.position, mousePosition).mag();
		return dist <= PIVOT_RADIUS;
	}
	
	@Override
	public void draw(Graphics2D g) {
		g.drawOval((int)this.position.getX() - PIVOT_RADIUS, (int)this.position.getY() - PIVOT_RADIUS, 2 * PIVOT_RADIUS, 2 * PIVOT_RADIUS);
	}
	
	@Override
	public void drawHighlighted(Graphics2D g, Vec2 mousePosition) {
		if (this.mouseOver(mousePosition)) {
			g.drawOval((int)this.position.getX() - HIGHLIGHTED_PIVOT_RADIUS, (int)this.position.getY() - HIGHLIGHTED_PIVOT_RADIUS, 
					2 * HIGHLIGHTED_PIVOT_RADIUS, 2 * HIGHLIGHTED_PIVOT_RADIUS);
		}
	}
}
