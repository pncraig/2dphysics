package tests;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

import java.util.ArrayList;

import java.awt.Graphics2D;

import javax.swing.JInternalFrame;

import physics.Vec2;
import physics.ParticleSystem;
import physics.AABB;

/**
 * Everything that can be added into the SimulationEditor is
 * extended from this class. This class contains the methods 
 * that each object in the simulation is expected to be able
 * to perform.
 * 
 * @author pncra
 */
public abstract class SimObject {
	/**
	 * An enumeration of each type of simulation object.
	 * 
	 * @author pncra
	 */
	public enum Types {
		SIM_PARTICLE("Particle"),
		SIM_PIVOT("Pivot"),
		SIM_SPRING("Spring"),
		SIM_PIVOTED_SPRING("Pivoted Spring");
		
		private String name;
		
		private Types(String name) {
			this.name = name;
		}
		
		public String toString() {
			return this.name;
		}
		
		/**
		 * Returns the enum type from a string.
		 * 
		 * @param s the string
		 * @return the enum type
		 */
		public static Types getType(String s) {
			for (Types t : Types.values()) {
				if (t.toString().equals(s)) {
					return t;
				}
			}
			
			return null;
		}
	}

	// A counter for ids
	private static long count = 0;
	
	public static void spawn(Types type, SimulationEditor se) {
		if (type == null) {
			return;
		}
		
		SimObject o;
		switch (type) {
		case SIM_PARTICLE: 
			o = SimParticle.getInstance(se.getMousePosition());
			break;
		case SIM_PIVOT: 
			o = SimPivot.getInstance(se.getMousePosition());
			break;
		case SIM_SPRING: 
			o = SimSpring.getInstance(se.getSelectedObject());
			break;
		case SIM_PIVOTED_SPRING: 
			o = SimPivotedSpring.getInstance(se.getSelectedObject());
			break;
		default: o = null;
		}
		
		if (o != null) {
			se.addSimObject(o);
		}
	}
	
	// Each SimObject has an id used for identification purposes
	private long id;
	
	// Keep track of each object that references this SimObject
	private List<SimObject> referencedObjects;
	
	// Flag which determines whether this SimObject is displaying its ObjectEditor
	private boolean displayingEditor;
	
	// Keep track of the fields on a SimObject that have been modified in an ObjectEditor
	private ConcurrentLinkedDeque<Runnable> updatedFields;
	
	public SimObject() {
		// Get the id for this new SimObject
		this.id = count;
		count++;
		
		this.referencedObjects = new ArrayList<>();
		this.updatedFields = new ConcurrentLinkedDeque<>();
		
		this.displayingEditor = false;
	}
	
	/**
	 * Two SimObjects are equal if they have the same id value.
	 * 
	 * @param o the SimObject we are comparing this SimObject to
	 * @return true if the SimObjects are the same
	 */
	public boolean equals(SimObject o) {
		return this.id == o.id;
	}
	
	/**
	 * Add a SimObject to the list of objects referencing
	 * this SimObject.
	 * 
	 * @param o the SimObject to add to the list
	 */
	public void addReferencedObject(SimObject o) {
		this.referencedObjects.add(o);
	}
	
	/**
	 * Get the list of SimObjects referencing this object.
	 * 
	 * @return the list of SimObjects referencing this SimObject
	 */
	public List<SimObject> getReferencedObjects() {
		return this.referencedObjects;
	}
	
	/**
	 * Returns true if the ObjectEditor is showing, and false
	 * otherwise
	 * 
	 * @return true if the ObjectEditor is showing, false otherwise
	 */
	public boolean getEditorShowing() {
		return this.displayingEditor;
	}
	
	/**
	 * Sets whether the ObjectEditor is showing.
	 * 
	 * @param showing whether the ObjectEditor is showing
	 */
	public void setEditorShowing(boolean showing) {
		this.displayingEditor = showing;
	}
	
	/**
	 * Add a field to update on this SimObject.
	 * 
	 * @param r the Runnable that will update the field
	 */
	public void addUpdatedField(Runnable r) {
		System.out.println("Adding field");
		this.updatedFields.push(r);
	}
	
	/**
	 * Update the fields on this SimObject.
	 */
	public void updateFields() {
		while (!this.updatedFields.isEmpty()) {
			this.updatedFields.pop().run();
		}
	}
	
	/**
	 * This method adds this SimObject to the ParticleSystem
	 * ps. Each class that extends SimObject will have an 
	 * instance variable that represents something that 
	 * can go in the ParticleSystem, however these objects are
	 * each added to the particle system in a different way. This 
	 * method abstracts the difference away. 
	 * 
	 * @param ps the ParticleSystem to add this SimObject to
	 */
	public abstract void addToSystem(ParticleSystem ps);
	
	/**
	 * This method removes this SimObject from the ParticleSystem
	 * ps. If this SimObject isn't in the ParticleSystem, then this
	 * method does nothing.
	 * 
	 * @param ps the ParticleSystem to remove this SimObject from
	 * @return a list of other SimObjects that reference this SimObject
	 * that should be remove
	 */
	public abstract List<SimObject> removeFromSystem(ParticleSystem ps);
	
	/**
	 * This method sets the position of this SimObject to p.
	 * For SimObjects like Particles and Pivots, this method
	 * makes a lot of sense, but for SimObjects like Springs 
	 * or distance constraints, it makes a little less sense. If
	 * I decide to provide something other than no implementation
	 * for those objects, it will probably move the particles/pivots
	 * the spring/distance constraint is attached to
	 * @param p
	 */
	public abstract void setPosition(Vec2 p);
	
	/**
	 * This method returns true if the mouse pointer is hovering
	 * over this SimObject, and false otherwise. This method is
	 * important because it is how SimObjects are selected.
	 * 
	 * @param mousePosition the position of the mouse pointer
	 * @return true if the mouse pointer is hovering over this SimObject, false otherwise
	 */
	public abstract boolean mouseOver(Vec2 mousePosition);
	
	/**
	 * This method draws this SimObject.
	 * 
	 * @param g the graphics context to draw to
	 */
	public abstract void draw(Graphics2D g);
	
	/**
	 * This method draws something extra around this SimObject
	 * to indicate that the mouse is hovering over it. This method
	 * is called in addition to draw().
	 * 
	 * @param g the graphics context to draw to
	 * @param mousePosition the position of the mouse cursor
	 */
	public abstract void drawHighlighted(Graphics2D g, Vec2 mousePosition);
	
	/**
	 * This method draws something extra around this SimObject to indicate
	 * that this is the currently selected SimObject. This method is called in
	 * addition to draw().
	 * 
	 * @param g the graphics context to draw to
	 */
	public abstract void drawSelected(Graphics2D g);
	
	/**
	 * This method returns the axis-aligned bounding-box (AABB)
	 * surrounding this SimObject.
	 * 
	 * @return the AABB around this SimObject
	 */
	public abstract AABB getAABB();
	
	/**
	 * This method returns the type of this SimObject (ex; SimParticle)
	 * 
	 * @return the type of this SimObject
	 */
	public abstract SimObject.Types getType();

	/**
	 * Get the physics object that the SimObject represents. I don't 
	 * really like this implementation because it returns an Object,
	 * but I'm not sure how else to do it.
	 * 
	 * @return the physics object that the SimObject represents
	 */
	public abstract Object getPhysicsObject();
	
	/**
	 * Opens a new ObjectEditor on this SimObject. The ObjectEditor
	 * is just a JInternalFrame that allows the user to modify the 
	 * characteristics of a SimObject.
	 */
	public abstract void openObjectEditor(SimulationEditor se);
	
	/**
	 * Update a field on this SimObject from an ObjectEditorField.
	 * 
	 * @param field the field to update
	 */
	public abstract void updateField(ObjectEditorField field);
}











