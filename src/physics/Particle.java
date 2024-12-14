package physics;

/**
 * Class that represents a particle in my
 * physics simulation.
 */
public class Particle {	
    // The position of the particle
    private Vec2 x;
    // The velocity of the particle
    private Vec2 v;
    // The force on the particle at the given moment
    private Vec2 f;
    // The mass of the particle
    private double m;

    public Particle(double x, double y, double vx, double vy, double m) {
        this.x = new Vec2(x, y);
        this.v = new Vec2(vx, vy);
        this.clearForces();
        this.m = m;
    }

    public Particle(Vec2 x, Vec2 v, double m) {
        this(x.getX(), x.getY(), v.getX(), v.getY(), m);
    }
    
    public Particle(Particle p) {
    	this(new Vec2(p.getPosition()), new Vec2(p.getVelocity()), p.getMass());
    }

    /**
     * Set the forces on this particle to 0.
     */
    public void clearForces() {
        this.f = new Vec2();
    }

    /**
     * Return this particle's position.
     *
     * @return the position of this particle
     */
    public Vec2 getPosition() {
        return new Vec2(this.x);
    }

    /**
     * Set this particle's position.
     pp*
     * @param x the new position of this particle
     */
    public void setPosition(Vec2 x) {
        this.x = new Vec2(x);
    }

    /**
     * Return this particle's velocity.
     *
     * @return the velocity of this particle
     */
    public Vec2 getVelocity() {
        return new Vec2(this.v);
    }

    /**
     * Set this particle's velocity.
     *
     * @param the new velocity of this particle
     */
    public void setVelocity(Vec2 v) {
        this.v = new Vec2(v);
    }

    /**
     * Get this particle's forces.
     *
     * @return this particle's forces
     */
    public Vec2 getForce() {
        return new Vec2(this.f);
    }

    /**
     * Add a force to this particle's forces
     *
     * @param f force to add
     */
    public void addForce(Vec2 f) {
        this.f.add(f);
    }

    /**
     * Get the particle's mass.
     *
     * @return the mass of the particle
     */
    public double getMass() {
        return this.m;
    }

    /**
     * Calculate the kinetic energy of this particle
     * The formula for kinetic energy is KE = 0.5*m*v^2
     * where m is the particle's mass and v is the
     * velocity of the particle.
     *
     * @return the kinetic energy of the particle
     */
    public double calculateEnergy() {
        return 0.5 * this.m * this.v.mag() * this.v.mag();
    }
}