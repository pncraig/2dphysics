package tests;

import java.util.List;

import java.util.ArrayList;
import java.awt.AlphaComposite;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Component;

import graphics.GameCore;
import graphics.NullRepaintManager;
import graphics.GameAction;
import graphics.InputManager;

import physics.ParticleSystem;
import physics.NumericalSolver;
import physics.Particle;
import physics.RK4Solver;
import physics.Vec2;
import physics.AABB;
import physics.Gravity;

/**
 * I've come to the realization that it might be better
 * if I created a single .java file from which I can create and 
 * edit simulations while it is running instead of creating
 * a new .java file for every simulation. This class does that.
 * @author pncra
 *
 */
public class SimulationEditor extends GameCore {
	public static void main(String[] args) {
		SimulationEditor se = new SimulationEditor();
		se.run();
	}
	
	private EditorFrame ef;
	
	private InputManager im;
	private GameAction leftMouse;
	private boolean leftMouseClicked;
	
	private GameAction spacebar;
	private boolean spacebarTapped;
	
	private boolean paused;
	
	private ParticleSystem ps;
	private NumericalSolver ns;
	
	// mouseOverObject is the object the mouse
	// is currently hovering over. selectedObject
	// is the object the mouse was hovering over when
	// the mouse was last clicked.
	private List<SimObject> objects;
	private SimObject mouseOverObject;
	private SimObject selectedObject;
	
	private List<JInternalFrame> objectEditors;
	
	private Gravity g;
	
	private Vec2 mousePosition;

	@Override
	public void init() {
		super.init();

		NullRepaintManager.install();
		
		this.im = new InputManager(this.screen.getFullScreenWindow());
		
		this.leftMouse = new GameAction("left mouse", GameAction.NORMAL);
		this.leftMouseClicked = false;
		this.im.mapToMouse(this.leftMouse, InputManager.MOUSE_BUTTON_1);
		
		this.spacebar = new GameAction("spacebar", GameAction.NORMAL);
		this.spacebarTapped = false;
		this.im.mapToKey(this.spacebar, KeyEvent.VK_SPACE);
		
		this.mousePosition = new Vec2(this.im.getMouseX(), this.im.getMouseY());
		
		this.paused = true;
		
		this.ps = new ParticleSystem();
		this.ns = new RK4Solver();
		
		this.objects = new ArrayList<>();
		
		this.objectEditors = new ArrayList<>();
		
		this.g = new Gravity(this.ps, 981);
		
		this.ps.addForce(this.g);
		
		this.ef = new EditorFrame(this);
		((JFrame)this.screen.getFullScreenWindow()).getLayeredPane().add(this.ef);
		this.screen.getFullScreenWindow().setFocusable(true);
		
		/*
		((JFrame)this.screen.getFullScreenWindow()).setFocusable(true);
		((JFrame)this.screen.getFullScreenWindow()).getLayeredPane().setFocusable(true);
		((JFrame)this.screen.getFullScreenWindow()).getLayeredPane().setFocusCycleRoot(true);
		
		
		((JFrame)this.screen.getFullScreenWindow()).getLayeredPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "pause");
		((JFrame)this.screen.getFullScreenWindow()).getLayeredPane().getActionMap().put("pause", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				ef.pause();
			}
		});
		*/
	}
	
	/**
	 * Function that is called in update that just processes all the
	 * input that has happened.
	 */
	public void processInput() {
		EditorMode mode = this.ef.getEditorMode();
		
		int amt = this.leftMouse.getAmount();
		
		if (amt >= 1) {
			if (!this.leftMouseClicked) {
				this.leftMouseClicked = true;
				this.selectedObject = this.mouseOverObject;
				mode.mouseClicked(InputManager.MOUSE_BUTTON_1);
				
				if (amt >= 2) {
					mode.mouseDoubleClicked(InputManager.MOUSE_BUTTON_1);
				}
			}
			
			mode.mousePressed(InputManager.MOUSE_BUTTON_1);
		} else {
			if (this.leftMouseClicked) {
				mode.mouseReleased(InputManager.MOUSE_BUTTON_1);
			}
			
			this.leftMouseClicked = false;
		}
	}
	
	@Override
	public void update(long elapsedTime) {
		boolean objectFound = false;
		for (int i = 0; i < this.objects.size(); i++) {
			SimObject o = this.objects.get(i);
			if (o.mouseOver(this.mousePosition)) {
				this.mouseOverObject = o;
				objectFound = true;
				break;
			}
		}
		
		if (!objectFound) {
			this.mouseOverObject = null;
		}
		
		this.processInput();
		
		if (!this.isPaused()) {
			double dt = elapsedTime / 1000.0;
			this.ns.step(this.ps, dt);
		} else {
			
		}
		
		this.mousePosition.setX(this.im.getMouseX());
		this.mousePosition.setY(this.im.getMouseY());
		
		for (int i = 0; i < this.objects.size(); i++) {
			this.objects.get(i).updateFields();
		}
	}
	
	@Override
	public void draw(Graphics2D g) {		
		for (SimObject o : this.objects) {
			o.draw(g);
		}
		
		this.ef.getEditorMode().draw(g);
	}
	
	/**
	 * Set whether the simulation is paused or not.
	 * 
	 * @param paused if true, pause the simulation. If false, unpause
	 * the simulation
	 */
	public void setPaused(boolean paused) {
		this.paused = paused;
	}
	
	/**
	 * Check whether the simulation is paused or not.
	 * 
	 * @return if true, the simulation is paused. If false, the
	 * simulation is not paused
	 */
	public boolean isPaused() {
		return this.paused;
	}
	
	/**
	 * Add a SimObject to the SimulationEditor
	 * 
	 * @param o the SimObject to add
	 */
	public void addSimObject(SimObject o) {
		o.addToSystem(this.ps);
		this.objects.add(o);
	}
	
	/**
	 * Remove a SimObject from the SimulationEditor.
	 * 
	 * @param o the SimObject to remove
	 */
	public void removeSimObject(SimObject o) {
		List<SimObject> objectsToRemove = o.removeFromSystem(this.ps);
		for (int i = 0; i < this.objects.size(); i++) {
			if (this.objects.get(i).equals(o)) {
				this.objects.remove(i);
				break;
			}
		}
		
		if (objectsToRemove != null) {
			for (int i = 0; i < objectsToRemove.size(); i++) {
				this.removeSimObject(objectsToRemove.get(i));
			}
		}
	}
	
	/**
	 * Create a new JButton.
	 * 
	 * @param image the filename of the image that should go in the button
	 * @param tooltip the tooltip that is displayed when the button is hovered over
	 * @param action the action that the button performs when pressed
	 * @return the button
	 */
	public JButton createButton(String imagePath, String tooltip, ActionListener action) {
		ImageIcon icon = new ImageIcon(imagePath);
		int w = icon.getIconWidth();
		int h = icon.getIconHeight();
		
		Image image = this.screen.createCompatibleImage(w, h, Transparency.TRANSLUCENT);
		Graphics2D g = (Graphics2D)image.getGraphics();
		AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
		g.setComposite(alpha);
		g.drawImage(icon.getImage(), 0, 0, null);
		g.dispose();
		ImageIcon hoverIcon = new ImageIcon(image);
		
		image = this.screen.createCompatibleImage(w, h, Transparency.TRANSLUCENT);
		g = (Graphics2D)image.getGraphics();
		g.drawImage(icon.getImage(), 2, 2, null);
		g.dispose();
		ImageIcon pressedIcon = new ImageIcon(image);
		
		JButton button = new JButton();
		button.addActionListener(action);
		button.setIgnoreRepaint(true);
		button.setFocusable(false);
		button.setToolTipText(tooltip);
		button.setBorder(null);
		button.setContentAreaFilled(false);
		button.setIcon(icon);
		button.setRolloverIcon(hoverIcon);
		button.setPressedIcon(pressedIcon);
		
		return button;
		
	}
	
	/**
	 * Sets the cursor.
	 * 
	 * @param cursor the cursor to set the cursor to
	 */
	public void setCursor(Cursor cursor) {
		this.screen.getFullScreenWindow().setCursor(cursor);
	}

	/**
	 * Gets the position of the mouse as a vector.
	 * 
	 * @return the position of the mouse
	 */
	public Vec2 getMousePosition() {
		return new Vec2(this.mousePosition);
	}
	
	/**
	 * Get the object the mouse is hovering over.
	 * 
	 * @return the object the mouse is hovering over
	 */
	public SimObject getMouseOverObject() {
		return this.mouseOverObject;
	}
	
	/**
	 * Get the object that is currently selected.
	 * 
	 * @return the object that is currently selected
	 */
	public SimObject getSelectedObject() {
		return this.selectedObject;
	}
	
	/**
	 * Add an object editor to this SimulationEditor.
	 * 
	 * @param c the editor to add
	 */
	public void addObjectEditor(JInternalFrame c) {
		this.objectEditors.add(c);
		((JFrame)this.screen.getFullScreenWindow()).getLayeredPane().add(c);
	}
	
	/**
	 * Clear the object editors that are displayed on the screen.
	 */
	public void clearObjectEditors() {
		for (int i = this.objectEditors.size() - 1; i >= 0; i--) {
			try {
				this.objectEditors.get(i).setClosed(true);
			} catch (java.beans.PropertyVetoException e) { }
			
			this.objectEditors.remove(i);
		}		
	}
	
	/**
	 * Get the SimParticle that is closest to the mouse pointer.
	 * 
	 * @return the SimParticle that is closest to the mouse
	 */
	public SimParticle getClosestSimParticle() {
		SimObject closestParticle = null;
		double closestDistance = Double.MAX_VALUE;
		for (int i = 0; i < this.objects.size(); i++) {
			SimObject o = this.objects.get(i);
			if (o.getType() == SimObject.Types.SIM_PARTICLE) {
				Particle p = (Particle)o.getPhysicsObject();
				double dist = Vec2.sub(p.getPosition(), this.getMousePosition()).mag();
				
				if (dist < closestDistance) {
					closestParticle = o;
					closestDistance = dist;
				}
			}
		}
		
		return (SimParticle)closestParticle;
	}
	
}







