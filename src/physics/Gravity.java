package physics;

import java.util.List;

/**
 * This class represents gravity as it is felt here on
 * the Earth, causing things to fall.
 * 
 * @author pncra
 */
public class Gravity extends Force {
    private ParticleSystem ps;
    private double g;

    public Gravity(ParticleSystem ps, double g) {
        this.ps = ps;
        this.g = g;
    }
    
    /**
     * Get the gravitational acceleration caused by this 
     * gravity.
     * 
     * @return the gravitational acceleration caused by this gravity
     */
    public double getConstant() {
    	return this.g;
    }
    
    /**
     * Set the gravitational acceleration caused by this
     * gravity.
     * 
     * @param g the new gravitational acceleration caused by this gravity
     */
    public void setConstant(double g) {
    	this.g = g;
    }

    @Override
    public void applyForce() {
        List<Particle> particles = this.ps.getParticles();
        for (int i = 0; i < particles.size(); i++) {
        	particles.get(i).addForce((new Vec2(0, this.g)).mult(particles.get(i).getMass()));
        }
    }
}