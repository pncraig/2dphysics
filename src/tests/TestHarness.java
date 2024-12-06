package tests;

import graphics.GameCore;

import graphics.GameAction;
import graphics.InputManager;

import java.awt.event.KeyEvent;

import physics.Particle;
import physics.PivotedSpring;
import physics.Vec2;
import physics.ParticleSystem;

import java.util.List;

/**
 * The TestHarness class is designed to take care of user
 * input so that I can easily create different simulation
 * setups
 * @author pncra
 *
 */
public abstract class TestHarness extends GameCore {
	protected InputManager im;
	private GameAction exit;
	private GameAction spring;
	private boolean drawSpring;
	private Vec2 mousePosition;
	@Override
	public void init() {
		super.init();
		
		this.im = new InputManager(this.screen.getFullScreenWindow());
		this.exit = new GameAction("exit", GameAction.DETECT_INITIAL_PRESS_ONLY);
		this.spring = new GameAction("spring", GameAction.NORMAL);
		this.im.mapToKey(this.exit, KeyEvent.VK_ESCAPE);
		this.im.mapToMouse(this.spring, InputManager.MOUSE_BUTTON_1);
		
		this.drawSpring = false;
		this.mousePosition = new Vec2(this.im.getMouseX(), this.im.getMouseY());
	}
	
	@Override
	public void update(long elapsedTime) {
		if (this.exit.isPressed()) {
			this.stop();
		}
		
		if (this.spring.isPressed()) {
			this.spring.press();
		} else {
			this.spring.reset();
		}
		
		this.mousePosition.setX(this.im.getMouseX());
		this.mousePosition.setY(this.im.getMouseY());
	}
	
	/**
	 * In my simulations, you can interact with the particles by
	 * clicking and dragging with the mouse. This action creates a spring
	 * between the nearest particle to the mouse and the mouse pointer. This function
	 * handles that interaction.
	 * 
	 * @param ps the particle system that is being run
	 */
	protected void manageMouseInput(ParticleSystem ps, double k) {
		if (this.spring.isPressed()) {
			List<Particle> particles = ps.getParticles();
			Particle closestParticle = particles.get(0);
			if (closestParticle == null) {
				return;
			}
			
			for (int i = 1; i < particles.size(); i++) {
				Particle p = particles.get(i);
				if (Vec2.sub(p.getPosition(), this.mousePosition).mag() < Vec2.sub(closestParticle.getPosition(), this.mousePosition).mag()) {
					closestParticle = p;
				}
			}
			
			double l = Vec2.sub(closestParticle.getPosition(), this.mousePosition).mag();
			
			PivotedSpring spring = new PivotedSpring(closestParticle, this.mousePosition, k, l);
		}
	}
}
