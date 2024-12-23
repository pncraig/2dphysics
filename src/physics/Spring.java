package physics;

public class Spring extends Force {
    private Particle a;
    private Particle b;
    private double k;
    private double l;

    public Spring(Particle a, Particle b, double k, double l) {
        this.a = a;
        this.b = b;
        this.k = k;
        this.l = l;
    }
    
    public Spring(Spring s) {
    	this.a = s.getParticles()[0];
    	this.b = s.getParticles()[1];
    	this.k = s.getSpringConstant();
    	this.l = s.getLength();
    }

    /**
     * Calculate the potential energy stored in
     * the spring.
     *
     * @return the potential energy stored in the spring
     */
    public double calculateEnergy() {
        // Formula for spring potential energy: PE = 0.5*k*x^2
        Vec2 r = Vec2.sub(this.b.getPosition(), this.a.getPosition());
        double x = this.l - r.mag();
        return 0.5 * this.k * x * x;
    }
    
    /**
     * Get the particles that are attached to each other by this
     * spring in an array with two elements
     * 
     * @return an array of Particle objects containing two elements
     */
    public Particle[] getParticles() {
    	Particle[] particles = new Particle[2];
    	particles[0] = this.a;
    	particles[1] = this.b;
    	return particles;
    }
    
    /**
     * Get the resting length of this spring.
     * 
     * @return the resting length of this spring
     */
    public double getLength() {
    	return this.l;
    }
    
    /**
     * Set the resting length of this spring.
     * 
     * @param l the new resting length of this spring
     */
    public void setLength(double l) {
    	this.l = l;
    }
    
    /**
     * Get the spring constant of this spring.
     * 
     * @return the spring constant of this spring
     */
    public double getSpringConstant() {
    	return this.k;
    }
    
    /**
     * Set the spring constant of this spring.
     * 
     * @param k the new spring constant
     */
    public void setSpringConstant(double k) {
    	this.k = k;
    }

    @Override
    public void applyForce() {
        Vec2 r = Vec2.sub(this.b.getPosition(), this.a.getPosition());
        double f1mag = -this.k * (this.l - r.mag());
        Vec2 f1 = Vec2.mult(r.unit(), f1mag);
        Vec2 f2 = Vec2.mult(f1, -1);
        this.a.addForce(f1);
        this.b.addForce(f2);
    }
}
