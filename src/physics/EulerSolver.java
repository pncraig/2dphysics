package physics;

public class EulerSolver implements NumericalSolver {
    public void step(ParticleSystem ps, double dt) {
        Vec2[] derivs = ps.calculateDerivatives();
        ps.scaleVector(derivs, dt);
        ps.updateSystemState(derivs);
        ps.updateClock(dt);
    }
}