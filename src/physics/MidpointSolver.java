package physics;

/**
 * Implements the numerical solver using
 * the midpoint method.
 */
public class MidpointSolver implements NumericalSolver {
    public void step(ParticleSystem ps, double dt) {
        Vec2[] initialState = ps.getSystemState();
        Vec2[] derivs1 = ps.calculateDerivatives();
        ps.scaleVector(derivs1, dt * 0.5);
        ps.updateSystemState(derivs1);
        Vec2[] derivs2 = ps.calculateDerivatives();
        ps.scaleVector(derivs2, dt);
        ps.setSystemState(initialState);
        ps.updateSystemState(derivs2);
    }
}