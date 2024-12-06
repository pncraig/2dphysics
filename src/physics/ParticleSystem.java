package physics;

import java.util.List;
import java.util.ArrayList;

/**
 * Represents a system of particles.
 */
public class ParticleSystem {
    private List<Particle> particles;
    private List<Force> forces;
    private double t;

    /**
     * Create a new particle system.
     */
    public ParticleSystem() {
        this.particles = new ArrayList<Particle>();
        this.forces = new ArrayList<Force>();
        this.t = 0.0;
    }

    /**
     * Add particles to the system.
     *
     * @param particles the particles to add
     */
    public void addParticle(Particle... particles) {
    	for (int i = 0; i < particles.length; i++) {
    		this.particles.add(particles[i]);
    	}
    }
    
    /**
     * Add particles from a list.
     * 
     * @param particles a list of particles
     */
    public void addParticles(List<Particle> particles) {
    	for (int i = 0; i < particles.size(); i++) {
    		this.particles.add(particles.get(i));
    	}
    }
    
    /**
     * Access the particles in the system. I might have to
     * change this function because it allows the particles
     * within the system to be changed. I'll just have to be
     * careful of that.
     * 
     * @return a list containing all the particles in the system
     */
    public List<Particle> getParticles() {
    	return this.particles;
    }

    /**
     * Add forces to the system.
     *
     * @param forces the forces to add
     */
    public void addForce(Force... forces) {
        for (int i = 0; i < forces.length; i++) {
        	this.forces.add(forces[i]);
        }
    }
    
    /**
     * Add a list of forces.
     * 
     * @param forces a list of forces
     */
    public void addForces(List<Force> forces) {
    	for (int i = 0; i < forces.size(); i++) {
    		this.forces.add(forces.get(i));
    	}
    }

    /**
     * Clear the force accumulators for all the
     * particles in the system.
     */
    public void clearForces() {
        for (int i = 0; i < this.particles.size(); i++) {
            this.particles.get(i).clearForces();
        }
    }

    /**
     * Go through the list of forces and apply
     * them to the particles that they affect.
     */
    public void computeForces() {
        for (int i = 0; i < this.forces.size(); i++) {
            this.forces.get(i).applyForce();
        }
    }

    /**
     * Calculate the derivatives for each particle in the system.
     * By Newton's Second Law (f = ma), the derivative of the
     * velocity is f/m, and the derivative of the position
     * is the velocity.
     *
     * @return an array containing the derivatives of the velocity
     * and position of each particle in the system; the derivatives
     * are stored in consecutive array indices.
     */
    public Vec2[] calculateDerivatives() {
        Vec2[] derivatives = new Vec2[this.particles.size() * 2];
        int j = 0;
        this.clearForces();
        this.computeForces();
        for (int i = 0; i < this.particles.size(); i++) {
            Particle p = this.particles.get(i);
            derivatives[j] = p.getVelocity();
            derivatives[j + 1] = p.getForce().mult(1 / p.getMass());
            j += 2;
        }

        return derivatives;
    }

    /**
     * Copy the state of the system into an array
     * and return it. The state is just an array
     * where every two elements represents a particle;
     * the first element is the position, and the
     * second element is the velocity.
     *
     * @return state of the system
     */
    public Vec2[] getSystemState() {
        int j = 0;
        Vec2[] state = new Vec2[this.particles.size() * 2];
        for (Particle p : this.particles) {
            state[j] = p.getPosition();
            state[j + 1] = p.getVelocity();
            j += 2;
        }
        return state;
    }

    /**
     * Set the state of the system from the state
     * array.
     *
     * @param state the array storing the updated state of the system
     */
    public void setSystemState(Vec2[] state) {
        int j = 0;
        for (int i = 0; i < this.particles.size(); i++) {
            Particle p = this.particles.get(i);
            p.setPosition(state[j]);
            p.setVelocity(state[j + 1]);
            j += 2;
        }
    }

    /**
     * Adds the derivatives to update the particles in the
     * system.
     *
     * @param derivatives the derivatives of the particles in the system
     */
    public void updateSystemState(Vec2[] derivatives) {
        int j = 0;
        for (int i = 0; i < this.particles.size(); i++) {
            Particle p = this.particles.get(i);
            p.setPosition(p.getPosition().add(derivatives[j]));
            p.setVelocity(p.getVelocity().add(derivatives[j + 1]));

            j += 2;
        }
    }

    /**
     * Scale the derivative vectors by the global time delta.
     *
     * @param derivatives the derivatives
     * @param dt the time delta
     */
    public void scaleVector(Vec2[] derivatives, double dt) {
        for (int i = 0; i < derivatives.length; i++) {
            derivatives[i].mult(dt);
        }
    }

    /**
     * Add two vectors that represent the state of the
     * system. These two vectors should only come from
     * getSystemState() and calculateDerivatives(), because
     * that is really the only scenario in which this function
     * makes sense.
     *
     * @param a the first vector
     * @param b the second vector
     * @return the sum of these two vectors
     */
    public Vec2[] addVectors(Vec2[] a, Vec2[] b) {
        Vec2[] sum = new Vec2[a.length];
        for (int i = 0; i < sum.length; i++) {
            sum[i] = Vec2.add(a[i], b[i]);
        }
        return sum;
    }

    /**
     * Update the system clock
     *
     * @param dt time since last update
     */
    public void updateClock(double dt) {
        this.t += dt;
    }
}