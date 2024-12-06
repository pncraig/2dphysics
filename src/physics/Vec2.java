package physics;

/**
 *  A class that represents a two dimensional vector.
 */
public class Vec2 {
    private double x;
    private double y;

    /**
     * Constructing a vector without parameters
     * creates a zero magnitude vector.
     */
    public Vec2() {
        this(0.0, 0.0);
    }

    /**
     * Create a new vector.
     *
     * @param x the x-component of the vector
     * @param y the y-component of the vector
     */
    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Create a new vector from a pre-existing vector
     *
     * @param vec the vector that is being copied
     */
    public Vec2(Vec2 vec) {
        this(vec.x, vec.y);
    }
    
    /**
     * Create a new vector from a magnitude and 
     * an angle
     * 
     * @param r the magnitude of the new vector
     * @param theta the angle in radians of the new vector
     */
    public static Vec2 polarVector(double r, double theta) {
    	return new Vec2(r * Math.cos(theta), r * Math.sin(theta));
    }

    /**
     * Calculates the magnitude of the vector.
     *
     * @return the magnitude of the vector
     */
    public double mag() {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }
    
    /**
     * Calculates the direction of this vector. Using this
     * function and mag, the vector can be converted to 
     * polar coordinates.
     * 
     * @return the direction of this vector
     */
    public double dir() {
    	return Math.atan2(this.y,  this.x);
    }
    
    /**
     * Rotates this vector by the given angle. The angle must be 
     * in radians.
     * 
     * @param angle the angle to rotate this vector by in radians
     */
    public void rotate(double angle) {
    	double r = this.mag();
    	double theta = this.dir();
    	this.x = r * (Math.cos(theta) * Math.cos(angle) - Math.sin(theta) * Math.sin(angle));
    	this.y = r * (Math.sin(theta) * Math.cos(angle) + Math.cos(theta) * Math.sin(angle));
    }

    /**
     * Calculate and returns a new vector
     * representing the unit vector for this vector.
     *
     * @return the unit vector for this vector
     */
    public Vec2 unit() {
        return Vec2.mult(this, 1 / this.mag());
    }

    /**
     * Return the x-component of this vector.
     *
     * @return the x-component of this vector
     */
    public double getX() {
        return this.x;
    }

    /**
     * Return the y-component of this vector.
     *
     * @return the y-component of this vector
     */
    public double getY() {
        return this.y;
    }

    /**
     * Set the x-component of this vector.
     *
     * @param x the new x-component for this vector
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Set the y-component of this vector.
     *
     * @param y the new y-component for this vector
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Set both components of this vector to the
     * components of another vector.
     *
     * @param vec the other vector
     */
    public void set(Vec2 vec) {
        this.x = vec.x;
        this.y = vec.y;
    }

    /**
     * Add another vector to this vector.
     *
     * @param vec the vector to be added to this vector
     * @return this vector to chain operations
     */
    public Vec2 add(Vec2 vec) {
        this.x += vec.x;
        this.y += vec.y;
        return this;
    }

    /**
     * Multiply this vector by a scalar.
     *
     * @param s the scalar to multiply this vector by
     * @return this vector to chain operations
     */
    public Vec2 mult(double s) {
        this.x *= s;
        this.y *= s;
        return this;
    }

    /**
     * Subtract another vector from this vector.
     *
     * @param vec the vector to be subtracted
     * @return this vector to chain operations
     */
    public Vec2 sub(Vec2 vec) {
        this.x -= vec.x;
        this.y -= vec.y;
        return this;
    }

    /**
     * Return the vector represented as a string.
     *
     * @return a string representing the vector
     */
    public String toString() {
        return "<" + this.x + ", " + this.y + ">";
    }

    /**
     * Adds two vectors together and returns a new
     * vector.
     *
     * @param v1 the first vector
     * @param v2 the second vector
     * @return the sum of v1 and v2
     */
    public static Vec2 add(Vec2 v1, Vec2 v2) {
        return new Vec2(v1.x + v2.x, v1.y + v2.y);
    }

    /**
     * Subtracts two vectors and returns a new
     * vector.
     *
     * @param v1 the first vector
     * @param v2 the second vector
     * @return the difference of v1 and v2
     */
    public static Vec2 sub(Vec2 v1, Vec2 v2) {
        return new Vec2(v1.x - v2.x, v1.y - v2.y);
    }

    /**
     * Multiplies a vector by a scalar and returns a new
     * vector.
     *
     * @param v the vector
     * @param s the scalar
     * @return vector v scaled by s
     */
    public static Vec2 mult(Vec2 v, double s) {
        return new Vec2(v.x * s, v.y * s);
    }
}