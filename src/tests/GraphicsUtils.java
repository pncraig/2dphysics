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
	private static double SPRING_CONSTANT = 4500;
	private static double MASS_CONSTANT = 1;
	
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
     * @param g the graphics object
     * @param s the spring to be drawn
     * @param w the width of the spring
     */
    public static void drawSpring(Graphics2D g, Spring s) {
    	double w = SPRING_CONSTANT / s.getSpringConstant();
    	
    	// Get the particles that the spring is attached to
    	Particle[] particles = s.getParticles();
    	Particle a = particles[0];
    	Particle b = particles[1];
    	
    	// Calculate the vector pointing from particle a to particle b
    	Vec2 r = Vec2.sub(b.getPosition(), a.getPosition());
    	// Calculate the number of nodes in the spring based on its resting length. The 10 dictates the density
    	int numberOfNodes = (int)(s.getLength() / 10);
    	// Calculate the distance between each node based on how the spring is stretched
    	double d = r.mag() / numberOfNodes;
    	// Get the direction of the vector between a and b to rotate the rib vectors
    	double dir = r.dir();
    	
    	// Calculate the vector to find the first rib
    	double theta = Math.atan(w / d);
    	double h = 0.5 * Math.sqrt(d * d+ w * w);
    	
    	
    	Vec2 node1 = Vec2.add(a.getPosition(), r.unit().mult(d));
    	g.drawLine((int)a.getPosition().getX(), (int)a.getPosition().getY(), (int)node1.getX(), (int)node1.getY());
    	
    	Vec2 ribUp = Vec2.polarVector(h, theta);
    	ribUp.rotate(dir);
    	
    	Vec2 node2 = Vec2.add(node1, ribUp);
    	g.drawLine((int)node1.getX(), (int)node1.getY(), (int)node2.getX(), (int)node2.getY());
    	
    	Vec2 node3 = Vec2.sub(b.getPosition(), r.unit().mult(d));
    	g.drawLine((int)b.getPosition().getX(), (int)b.getPosition().getY(), (int)node3.getX(), (int)node3.getY());
    	
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
