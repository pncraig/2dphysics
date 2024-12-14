package tests;

import java.awt.Graphics2D;

import physics.Vec2;

public class GUIPivot implements Drawable, Hoverable {
	public static final int PIVOT_RADIUS = 10;
	
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
	
	public boolean mouseOver(Vec2 mousePosition) {
		double dist = Vec2.sub(this.position, mousePosition).mag();
		return dist <= PIVOT_RADIUS;
	}
	
	@Override
	public void draw(Graphics2D g) {
		g.drawOval((int)this.position.getX() - PIVOT_RADIUS, (int)this.position.getY() - PIVOT_RADIUS, 2 * PIVOT_RADIUS, 2 * PIVOT_RADIUS);
	}
}
