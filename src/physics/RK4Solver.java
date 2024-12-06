package physics;

/**
 * Solver the implements the fourth-order Runge-Kutta
 * algorithm
 */
public class RK4Solver implements NumericalSolver {
	@Override
	public void step(ParticleSystem ps, double dt) {
		// This implementation is from the BDH diff. eq book
		
		// Save the initial state of the particle system so we can rewind
		// back later.
		Vec2[] initialState = ps.getSystemState();
		
		// Calculate the slope at the initial point and use it 
		// to calculate a point halfway along the time interval
		Vec2[] k1 = ps.calculateDerivatives();
		Vec2[] scaledK1 = this.copy(k1); // Make a copy cause we will need the unadulterated k1 in the final step
		ps.scaleVector(scaledK1, dt * 0.5);
		ps.updateSystemState(scaledK1);
		
		// Calculate the slope at the point we just found. Then,
		// rewind the system back to its initial state and calculate
		// another point halfway along the interval with this new slope
		Vec2[] k2 = ps.calculateDerivatives();
		Vec2[] scaledK2 = this.copy(k2);
		ps.scaleVector(scaledK2, dt * 0.5);
		ps.setSystemState(initialState);
		ps.updateSystemState(scaledK2);
		
		// Calculate the slope at the new point. Rewind 
		// back to the initial state and use this slope to
		// find a point at the end of the interval.
		Vec2[] k3 = ps.calculateDerivatives();
		Vec2[] scaledK3 = this.copy(k3);
		ps.scaleVector(scaledK3, dt);
		ps.setSystemState(initialState);
		ps.updateSystemState(scaledK3);
		
		// Calculate the slope at the end of the interval
		Vec2[] k4 = ps.calculateDerivatives();
		
		// Rewind the system back to its initial state to make our
		// final estimation. The slope we will use is a weighted
		// average of k1, k2, k3, and k4. k2 and k3 have double the
		// weight because they are from points in the middle of 
		// the interval.
		ps.setSystemState(initialState);
		ps.scaleVector(k1, 0.1666667);
		ps.scaleVector(k2, 0.3333333);
		ps.scaleVector(k3, 0.3333333);
		ps.scaleVector(k4, 0.1666667);
		Vec2[] weightedAvg = ps.addVectors(k1, k2);
		weightedAvg = ps.addVectors(weightedAvg, k3);
		weightedAvg = ps.addVectors(weightedAvg, k4);
		ps.scaleVector(weightedAvg, dt);
		ps.updateSystemState(weightedAvg);
	}
	
	/**
	 * Copy a state vector
	 * 
	 * @param vec vector to copy
	 * @return copy of vec
	 */
	private Vec2[] copy(Vec2[] vec) {
		Vec2[] copy = new Vec2[vec.length];
		for (int i = 0; i < vec.length; i++) {
			copy[i] = new Vec2(vec[i]);
		}
		return copy;
	}
}
