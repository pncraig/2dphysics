package physics;

public class Gravity implements Force {
    private Particle p;
    private double g;
    private Vec2 field;

    public Gravity(Particle p, double g) {
        this.p = p;
        this.g = g;
        this.field = new Vec2(0.0, this.g);
    }

    public void applyForce() {
        this.p.addForce(this.field);
    }
}