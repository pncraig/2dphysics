package physics;

/**
 * This class represents an abstract force.
 * Implement this interface to create new forces.
 */
public interface Force {
	/**
	 * Classes that implement the Force interface will
	 * have access to one or multiple particles. The ParticleSystem
	 * has access to all the Force objects. Each frame the 
	 * ParticleSystem will call the apply force method for
	 * each force in the system.
	 */
    public void applyForce();
}