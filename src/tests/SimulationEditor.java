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
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JComponent; 

import graphics.GameCore;
import graphics.InputManager;
import graphics.NullRepaintManager;
import graphics.GameAction;

import physics.ParticleSystem;
import physics.NumericalSolver;
import physics.RK4Solver;
import physics.EulerSolver;
import physics.Particle;
import physics.Force;
import physics.Vec2;
import physics.Spring;
import physics.Gravity;
import physics.PivotedSpring;

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
	
	public PausedState PAUSED_STATE;
	public UnpausedState UNPAUSED_STATE;
	private EditorState state;
	
	private InputManager im;
	private GameAction exit;
	private GameAction pause;
	
	private boolean paused;
	
	private ParticleSystem ps;
	private NumericalSolver ns;
	private List<GUIPivot> pivots;
	
	private Gravity g;
	
	private Vec2 mousePosition;
	
	@Override
	public void init() {
		super.init();

		NullRepaintManager.install();
		
		this.im = new InputManager(this.screen.getFullScreenWindow());
		this.exit = new GameAction("exit", GameAction.DETECT_INITIAL_PRESS_ONLY);
		this.pause = new GameAction("pause", GameAction.DETECT_INITIAL_PRESS_ONLY);
		
		this.im.mapToKey(this.exit, KeyEvent.VK_ESCAPE);
		this.im.mapToKey(this.pause, KeyEvent.VK_SPACE);
		
		this.paused = true;
		
		this.ps = new ParticleSystem();
		this.ns = new RK4Solver();
		
		this.pivots = new ArrayList<>();
		
		this.mousePosition = new Vec2(this.im.getMouseX(), this.im.getMouseY());
		
		this.g = new Gravity(this.ps, 98.1);
		
		this.ps.addForce(this.g);
		
		this.PAUSED_STATE = new PausedState(this);
		this.UNPAUSED_STATE = new UnpausedState(this);
		this.state = this.PAUSED_STATE;
		this.state.enter();
		
	}
	
	/**
	 * Function that is called in update that just processes all the
	 * input that has happened.
	 */
	public void processInput() {
		if (this.exit.isPressed()) {
			this.stop();
		}
		
		EditorState state = this.state.handleInput(this.pause);
		if (state != null) {
			this.paused = !this.paused;
			this.state.exit();
			
			this.state = state;
			this.state.enter();
		}
	}
	
	@Override
	public void update(long elapsedTime) {
		this.processInput();
		
		if (!this.paused) {
			double dt = elapsedTime / 1000.0;
			this.ns.step(this.ps, dt);
		} else {
			
		}
		
		this.mousePosition.setX(this.im.getMouseX());
		this.mousePosition.setY(this.im.getMouseY());
	}
	
	@Override
	public void draw(Graphics2D g) {
		List<Particle> particles = this.ps.getParticles();
		for (Particle p : particles) {
			((GUIParticle)p).draw(g);
		}
		
		for (GUIPivot p : pivots) {
			p.draw(g);
		}
		
		List<Force> forces = this.ps.getForces();
		for (Force f : forces ) {
			if (f instanceof GUISpring || f instanceof GUIPivotedSpring) {
				((Drawable)f).draw(g);
			}
		}
		
		this.state.render(g);
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
	 * Returns the content pane of this window, but sets
	 * the background of the content pane to be transparent first
	 * 
	 * @return the content pane for this window
	 */
	public Container getContentPane() {
		JFrame frame = (JFrame)this.screen.getFullScreenWindow();
		Container contentPane = frame.getContentPane();
		
		if (contentPane instanceof JComponent) {
			((JComponent)contentPane).setOpaque(false);
		}
		
		return contentPane;
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
	 * Get the list of the particles in the ParticleSystem.
	 * 
	 * @return a list of particles in the ParticleSystem
	 */
	public List<Particle> getParticles() {
		return this.ps.getParticles();
	}
	
	/**
	 * Get the list of pivots.
	 * 
	 * @return list of pivots
	 */
	public List<GUIPivot> getPivots() {
		return this.pivots;
	}
	
	/**
	 * Get the forces in the system.
	 * 
	 * @return list of forces
	 */
	public List<Force> getForces() {
		return this.ps.getForces();
	}
	
	/**
	 * Add a new GUIParticle to the system.
	 * 
	 * @param x the position vector to add the new particle at
	 * @param v the velocity vector to add the new particle with
	 * @param m the mass of the new particle
	 */
	public void addParticle(Vec2 x, Vec2 v, double m) {
		this.ps.addParticle(new GUIParticle(new Particle(x, v, m)));
	}
	
	/**
	 * Add a new Pivot to the system (a pivot is basically just a 
	 * Vec2).
	 * 
	 * @param p the position of the pivot
	 */
	public void addPivot(Vec2 p) {
		this.pivots.add(new GUIPivot(p));
	}
	
	/**
	 * Add a new Force to the particle system.
	 * 
	 * @param f the force to add
	 */
	public void addForce(Force f) {
		this.ps.addForce(f);
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
	 * Gets the InputManager that handles input for the 
	 * simulation editor.
	 * 
	 * @return the InputManager
	 */
	public InputManager getInputManager() {
		return this.im;
	}
}







