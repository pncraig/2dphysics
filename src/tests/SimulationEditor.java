package tests;

import java.util.List;
import java.util.ArrayList;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import graphics.GameCore;
import graphics.InputManager;
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
	
	private boolean paused;
	
	private ParticleSystem ps;
	private NumericalSolver ns;
	private List<Particle> particles;
	private List<Force> forces;
	private List<Vec2> pivots;
	
	@Override
	public void init() {
		super.init();
		
		this.im = new InputManager(this.screen.getFullScreenWindow());
		this.exit = new GameAction("exit", GameAction.DETECT_INITIAL_PRESS_ONLY);
		this.pause = new GameAction("pause", GameAction.DETECT_INITIAL_PRESS_ONLY);
		this.spawn = new GameAction("spawn", GameAction.NORMAL);
		
		this.im.mapToKey(this.exit, KeyEvent.VK_ESCAPE);
		this.im.mapToKey(this.pause, KeyEvent.VK_SPACE);
		this.im.mapToMouse(this.spawn, InputManager.MOUSE_BUTTON_1);
		
		this.paused = false;
		
		this.ps = new ParticleSystem();
		this.ns = new RK4Solver();
		
		this.particles = new ArrayList<>();
		this.forces = new ArrayList<>();
		this.pivots = new ArrayList<>();
		
		this.forces.add(new Gravity(this.ps, 98.1));
		
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
		
		if (this.spawn.isPressed()) {
			Particle p = new Particle(this.im.getMouseX(), this.im.getMouseY(), Math.random() * 400 - 200, Math.random() * -200, 10);
			this.particles.add(p);
			this.ps.addParticle(p);
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
	}
	
	@Override
	public void draw(Graphics2D g) {
		for (Particle p : this.particles) {
			GraphicsUtils.drawParticle(g, p);
		}
	}
}
