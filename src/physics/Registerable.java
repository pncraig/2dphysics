package physics;

public class Registerable {
	private static long generator = 0;
	
	private long id;
	
	public Registerable() {
		this.id = generator;
		generator++;
	}
	
	/**
	 * Get the id of this registerable.
	 * 
	 * @return the id
	 */
	public long getId() {
		return this.id;
	}
	
	/**
	 * Check if two registerables are the same.
	 * 
	 * @param r the other registerable
	 * @return If true, the registerables are the same
	 */
	public boolean equals(Registerable r) {
		return this.id == r.id;
	}
}
