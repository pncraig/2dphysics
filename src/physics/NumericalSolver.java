package physics;

/**
 * An interface which represents a numerical
 * differential equations solver
 */
public interface NumericalSolver {
    public void step(ParticleSystem ps, double dt);
}