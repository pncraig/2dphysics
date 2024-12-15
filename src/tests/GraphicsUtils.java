package tests;

import java.awt.Graphics2D;



import physics.Vec2;
import physics.Spring;
import physics.Particle;
import physics.PivotedSpring;

public class GraphicsUtils {
	// The radius of a particle is proportional to it's mass, and
	// the width of a spring is inversely proportional to its spring
	// constant. These constants are the constants by which we control
	// these proportions.
	public static double SPRING_CONSTANT = 4500;
	public static double MASS_CONSTANT = 1;
	
	public static void setMassConstant(double d) {
		MASS_CONSTANT = d;
	}
	
	public static void setSpringConstant(double d) {
		SPRING_CONSTANT = d;
	}
	
	
    public static void drawParticle(Graphics2D g, Particle p) {
    	int r = (int)(MASS_CONSTANT * p.getMass());
        g.fillOval((int)(p.getPosition().getX() - r), (int)(p.getPosition().getY() - r), r * 2, r * 2);
    }
    
    /**
     * Draw a spring.
     * 
     * @param g the graphics context to draw to
     * @param a one of the end points of the spring
     * @param b the other end point of the spring
     * @param k the spring constant of the spring
     * @param l the resting length of the spring
     */
    public static void drawSpring(Graphics2D g, Vec2 a, Vec2 b, double k, double l) {
    	double w = SPRING_CONSTANT / k;
    	
    	// Calculate the vector pointing from particle a to particle b
    	Vec2 r = Vec2.sub(b, a);
    	// Calculate the number of nodes in the spring based on its resting length. The 10 dictates the density
    	int numberOfNodes = (int)(l / 10);
    	// Calculate the distance between each node based on how the spring is stretched
    	double d = r.mag() / numberOfNodes;
    	// Get the direction of the vector between a and b to rotate the rib vectors
    	double dir = r.dir();
    	
    	// Calculate the vector to find the first rib
    	double theta = Math.atan(w / d);
    	double h = 0.5 * Math.sqrt(d * d+ w * w);
    	
    	
    	Vec2 node1 = Vec2.add(a, r.unit().mult(d));
    	g.drawLine((int)a.getX(), (int)a.getY(), (int)node1.getX(), (int)node1.getY());
    	
    	Vec2 ribUp = Vec2.polarVector(h, theta);
    	ribUp.rotate(dir);
    	
    	Vec2 node2 = Vec2.add(node1, ribUp);
    	g.drawLine((int)node1.getX(), (int)node1.getY(), (int)node2.getX(), (int)node2.getY());
    	
    	Vec2 node3 = Vec2.sub(b, r.unit().mult(d));
    	g.drawLine((int)b.getX(), (int)b.getY(), (int)node3.getX(), (int)node3.getY());
    	
    	Vec2 ribDown = Vec2.polarVector(h, -theta);
    	ribDown.rotate(dir);
    	
    	if (numberOfNodes % 2 == 0) {
    		Vec2 node4 = Vec2.sub(node3, ribUp);
    		g.drawLine((int)node3.getX(), (int)node3.getY() , (int)node4.getX(), (int)node4.getY());
    	} else {
    		Vec2 node4 = Vec2.sub(node3, ribDown);
    		g.drawLine((int)node3.getX(), (int)node3.getY() , (int)node4.getX(), (int)node4.getY());
    	}
    	
    	ribUp.mult(2);
    	ribDown.mult(2);
    	Vec2 startNode = node2;
    	for (int i = 0; i < numberOfNodes - 3; i++) {
    		Vec2 drawToNode;
    		if (i % 2 == 0) {
    			drawToNode = Vec2.add(startNode, ribDown);
    		} else {
    			drawToNode = Vec2.add(startNode, ribUp);
    		}
    		g.drawLine((int)startNode.getX(), (int)startNode.getY(), (int)drawToNode.getX(), (int)drawToNode.getY());
    		startNode = drawToNode;
    	}
    }
    
    /**
     * Gets a list of 4 points that define a rectangle around a spring anchored
     * at points p1 and p2 with a spring constant k
     * 
     * @param p1 one of the anchors of the spring
     * @param p2 the other anchor of the spring
     * @param k the spring constant of the spring
     * @param r1 the radius of p1
     * @param r2 the radius of p2
     * @return a list of 4 points that define a rectangle around the spring
     */
    public static Vec2[] getPointsAroundSpring(Vec2 p1, Vec2 p2, double k, double r1, double r2) {
    	double w = GraphicsUtils.SPRING_CONSTANT / k;
		Vec2 r = Vec2.sub(p2, p1);
		Vec2 rhat = r.unit();
		
		Vec2 startPoint = Vec2.add(p1, Vec2.mult(rhat, r1));
		Vec2 endPoint = Vec2.sub(p2, Vec2.mult(rhat, r2));
		
		Vec2 a = Vec2.add(startPoint, Vec2.mult(rhat, 0.5 * w).rotate(Math.PI / 2));
		Vec2 b = Vec2.add(endPoint, Vec2.mult(rhat, 0.5 * w).rotate(Math.PI / 2));
		Vec2 c = Vec2.add(endPoint, Vec2.mult(rhat, 0.5 * w).rotate(-Math.PI / 2));
		Vec2 d = Vec2.add(startPoint, Vec2.mult(rhat, 0.5 * w).rotate(-Math.PI / 2));
		
		Vec2[] points = new Vec2[4];
		points[0] = a;
		points[1] = b;
		points[2] = c;
		points[3] = d;
		
		return points;
    }
    
    /**
     * Check if a point is inside a rectangle.
     * 
     * @param p the point to check
     * @param a a corner of the rectangle
     * @param b the corner of the rectangle that is clockwise from a
     * @param c the corner of the rectangle that is clockwise from b
     * @param d the corner of the rectangle that is clockwise from c
     * @return true if the point is inside the rectangle, false otherwise
     */
    public static boolean pointOverRectangle(Vec2 p, Vec2 a, Vec2 b, Vec2 c, Vec2 d) {
    	return !(signedDistanceToLine(p, a, b) > 0.0 ||
    			signedDistanceToLine(p, b, c) > 0.0 ||
    			signedDistanceToLine(p, c, d) > 0.0 ||
    			signedDistanceToLine(p, d, a) > 0.0);
    }
    
    /**
     * Calculates the signed distance point x is from the line defined
     * by points a and b.
     * 
     * @param x the point we are calculating the distance from
     * @param a a point on the line we are calculating the distance from
     * @param b another point on the line we are calculating the distance from
     * @return the distance between point x and the line defined by points a and b
     */
    private static double signedDistanceToLine(Vec2 x, Vec2 a, Vec2 b) {
    	Vec2 v = Vec2.sub(b, a);
    	if (v.mag() == 0.0) return 0.0;
    	Vec2 px = Vec2.sub(x, a);
    	double vCrosspx = v.getX() * px.getY() - v.getY() * px.getX();
    	return vCrosspx / v.mag();
    }
    
    /**
     * Draw a spring.
     * 
     * @param g the graphics object
     * @param s the spring to be drawn
     */
    public static void drawSpring(Graphics2D g, Spring s) {
    	double w = SPRING_CONSTANT / s.getSpringConstant();
    	
    	// Get the particles that the spring is attached to
    	Particle[] particles = s.getParticles();
    	Particle a = particles[0];
    	Particle b = particles[1];
    	
    	drawSpring(g, a.getPosition(), b.getPosition(), s.getSpringConstant(), s.getLength());
    }
    
    /**
     * Draw a spring that is pivoted on one end.
     * 
     * @param g the graphics object used to draw to the screen
     * @param s the pivoted spring to draw
     */
    public static void drawPivotedSpring(Graphics2D g, PivotedSpring s) {
    	if (s.getParticle() == null) {
    		return;
    	}
    	
    	Particle a = new Particle(s.getPivot(), new Vec2(), 1);
    	Particle b = s.getParticle();
    	Spring wrapper = new Spring(a, b, s.getSpringConstant(), s.getLength());
    	
    	g.drawOval((int)s.getPivot().getX() - 5, (int)s.getPivot().getY() - 5, 10, 10);
    	drawSpring(g, wrapper);
    }
}
