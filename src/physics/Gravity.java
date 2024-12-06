package physics;

import java.util.List;

public class Gravity implements Force {
    private ParticleSystem ps;
    private double g;

    public Gravity(ParticleSystem ps, double g) {
        this.ps = ps;
        this.g = g;
    }

    public void applyForce() {
        List<Particle> particles = this.ps.getParticles();
        for (int i = 0; i < particles.size(); i++) {
        	particles.get(i).addForce((new Vec2(0, this.g)).mult(particles.get(i).getMass()));
        }
    }
}