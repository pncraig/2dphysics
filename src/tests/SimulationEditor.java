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
	
	private InputManager im;
	private GameAction exit;
	private GameAction pause;
	private GameAction spring;
	private GameAction spawn;
	
	private JPanel buttonSidebar;
	private JButton spawnParticleButton;
	private JButton gravityButton;
	private JButton mouseSpringButton;
	
	private boolean paused;
	private boolean spawnParticleMode;
	private boolean mouseSpringMode;
	
	private ParticleSystem ps;
	private NumericalSolver ns;
	private List<Particle> particles;
	private List<Force> forces;
	private List<Vec2> pivots;
	
	private Gravity g;
	private PivotedSpring mouseSpring;
	
	private Vec2 mousePosition;
	
	@Override
	public void init() {
		super.init();
		
		NullRepaintManager.install();
		
		
		this.im = new InputManager(this.screen.getFullScreenWindow());
		this.exit = new GameAction("exit", GameAction.DETECT_INITIAL_PRESS_ONLY);
		this.pause = new GameAction("pause", GameAction.DETECT_INITIAL_PRESS_ONLY);
		this.spawn = new GameAction("spawn", GameAction.DETECT_INITIAL_PRESS_ONLY);
		this.spring = new GameAction("spring", GameAction.NORMAL);
		
		this.im.mapToKey(this.exit, KeyEvent.VK_ESCAPE);
		this.im.mapToKey(this.pause, KeyEvent.VK_SPACE);
		this.im.mapToMouse(this.spawn, InputManager.MOUSE_BUTTON_1);
		
		ActionListener spawnParticle = (ActionEvent e) -> {
			this.mouseSpringMode = false;
			this.spawnParticleMode = true;
			
			this.mouseSpring.setSpringConstant(0);
			this.im.mapToMouse(this.spawn, InputManager.MOUSE_BUTTON_1);
		};
		
		ActionListener toggleGravity = (ActionEvent e) -> {
			if (this.g.getConstant() == 0.0) {
				this.g.setConstant(98.1);
			} else {
				this.g.setConstant(0.0);
			}	
		};
		
		ActionListener manipulateSpring = (ActionEvent e) -> {
			this.spawnParticleMode = false;
			this.mouseSpringMode = true;
			
			this.mouseSpring.setSpringConstant(150);
			this.im.mapToMouse(this.spring, InputManager.MOUSE_BUTTON_1);
		};
		
		this.spawnParticleButton = this.createButton("CircleButton.png", "Select 'Spawn Particle' Mode", spawnParticle);
		this.gravityButton= this.createButton("GravityButton.png", "Toggle gravity", toggleGravity);
		this.mouseSpringButton = this.createButton("SpringButton.png", "Manipulate particles with a spring attached to the mouse", manipulateSpring);
		
		this.buttonSidebar = new JPanel();
		this.buttonSidebar.setOpaque(false);
		this.buttonSidebar.add(this.spawnParticleButton);
		this.buttonSidebar.add(this.gravityButton);
		this.buttonSidebar.add(this.mouseSpringButton);
		
		JFrame frame = (JFrame)this.screen.getFullScreenWindow();
		Container contentPane = frame.getContentPane();
		
		if (contentPane instanceof JComponent) {
			((JComponent)contentPane).setOpaque(false);
		}
		
		contentPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		contentPane.add(this.spawnParticleButton);
		contentPane.add(this.gravityButton);
		contentPane.add(this.mouseSpringButton);
		
		this.paused = false;
		this.spawnParticleMode = true;
		this.mouseSpringMode = false;
		
		this.ps = new ParticleSystem();
		this.ns = new RK4Solver();
		
		this.particles = new ArrayList<>();
		this.forces = new ArrayList<>();
		this.pivots = new ArrayList<>();
		
		this.mousePosition = new Vec2(this.im.getMouseX(), this.im.getMouseY());
		
		this.g = new Gravity(this.ps, 98.1);
		this.mouseSpring = new PivotedSpring(null, this.mousePosition, 0, 0);
		
		this.forces.add(this.g);
		this.forces.add(this.mouseSpring);
		
		this.ps.addForces(this.forces);
	}
	
	/**
	 * Function that is called in update that just processes all the
	 * input that has happened.
	 */
	public void processInput() {
		if (this.exit.isPressed()) {
			this.stop();
		}
		
		if (this.pause.isPressed()) {
			this.paused = !this.paused;
		}
		
		if (this.spawnParticleMode && this.spawn.isPressed()) {
			Particle p = new Particle(this.mousePosition, new Vec2(), 10);
			this.particles.add(p);
			this.ps.addParticle(p);
		}
		
		if (this.spring.isPressed()) {
			if (!this.particles.isEmpty()) {
				Particle closestParticle = this.particles.get(0);
				double closestDistance = Vec2.sub(this.mousePosition, closestParticle.getPosition()).mag();
				for (int i = 1; i < this.particles.size(); i++) {
					Vec2 r = Vec2.sub(this.mousePosition, this.particles.get(i).getPosition());
					if (r.mag() < closestDistance) {
						closestDistance = r.mag();
						closestParticle = this.particles.get(i);
					}
				}
				
				this.mouseSpring.setParticle(closestParticle);
				this.mouseSpring.setLength(closestDistance);
			}
		} else {
			this.mouseSpring.setParticle(null);
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
		for (Particle p : this.particles) {
			GraphicsUtils.drawParticle(g, p);
		}
		
		if (this.mouseSpringMode) {
			GraphicsUtils.drawPivotedSpring(g, this.mouseSpring);
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
	private JButton createButton(String imagePath, String tooltip, ActionListener action) {
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
}
