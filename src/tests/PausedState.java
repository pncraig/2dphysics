package tests;

import java.awt.Container;
import java.util.Stack;
import java.util.List;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Graphics2D;

import graphics.GameAction;
import graphics.InputManager;

import physics.Vec2;
import physics.Spring;
import physics.Particle;
import physics.PivotedSpring;

/**
 * Represents the editor when it is in the paused
 * state
 * @author pncra
 */
public class PausedState implements EditorState {
	
	private final PausedState.SpawnParticleState SPAWN_PARTICLE_STATE;
	private final PausedState.SpawnPivotState SPAWN_PIVOT_STATE;
	private final PausedState.SpawnSpringState SPAWN_SPRING_STATE;
	
	private State state;
	private SimulationEditor se;
	
	private JButton spawnParticleButton;
	private JButton spawnPivotButton;
	private JButton spawnSpringButton;
	
	public PausedState(SimulationEditor se) {
		this.se = se;
		
		this.SPAWN_PARTICLE_STATE = new SpawnParticleState(this);
		this.SPAWN_PIVOT_STATE = new SpawnPivotState(this);
		this.SPAWN_SPRING_STATE = new SpawnSpringState(this);
		
		this.state = this.SPAWN_PARTICLE_STATE;
		this.state.enter();
		
		this.spawnParticleButton = se.createButton("CircleButton.png", "Select 'Spawn Particle' Mode", (ActionEvent e) -> {
			this.state.exit();
			this.state = SPAWN_PARTICLE_STATE;
			this.state.enter();
		});
		
		this.spawnPivotButton = se.createButton("PivotButton.png", "Select 'Spawn Pivot' Mode", (ActionEvent e) -> {
			this.state.exit();
			this.state = SPAWN_PIVOT_STATE;
			this.state.enter();
		});
		
		this.spawnSpringButton = se.createButton("SpringButton.png", "Select 'Spawn Spring' Mode", (ActionEvent e) -> {
			this.state.exit();
			this.state = SPAWN_SPRING_STATE;
			this.state.enter();
		});
	}
	
	@Override
	public EditorState handleInput(GameAction paused) {
		if (paused.isPressed()) {
			return this.se.UNPAUSED_STATE;
		}
		
		this.state.handleInput();
		
		return null;
	}
	
	@Override
	public void render(Graphics2D g) {
		this.state.render(g);
	}
	
	@Override
	public void enter() {
		System.out.println("Enter paused state");
		Container contentPane = this.se.getContentPane();
		contentPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		contentPane.add(this.spawnParticleButton);
		contentPane.add(this.spawnPivotButton);
		contentPane.add(this.spawnSpringButton);
		
		this.state.enter();
	}
	
	@Override
	public void exit() {
		System.out.println("Exit paused state");
		Container contentPane = this.se.getContentPane();
		contentPane.removeAll();
		
		this.state.exit();
	}
	
	/**
	 * Interface that controls the state of the PausedState
	 * @author pncra
	 */
	private interface State {
		public void handleInput();
		public void render(Graphics2D g);
		public void enter();
		public void exit();
	}
	
	private class SpawnParticleState implements State {
		private PausedState ps;
		
		private GameAction spawnParticle;
		
		public SpawnParticleState(PausedState ps) {
			this.ps = ps;
			
			this.spawnParticle = new GameAction("spawn particle", GameAction.DETECT_INITIAL_PRESS_ONLY);
		}
		
		public void handleInput() {
			if (this.spawnParticle.isPressed()) {
				System.out.println("Add particle");
				this.ps.se.addParticle(this.ps.se.getMousePosition(), new Vec2(0, 0), 10);
			}
		}
		
		public void render(Graphics2D g) {
			
		}
		
		public void enter() {
			System.out.println("Entering spawn particle state");
			InputManager im = this.ps.se.getInputManager();
			im.mapToMouse(this.spawnParticle, InputManager.MOUSE_BUTTON_1);
		}
		
		public void exit() {
			System.out.println("Exiting spawn particle state");
			InputManager im = this.ps.se.getInputManager();
			im.mapToMouse(null, InputManager.MOUSE_BUTTON_1);
		}
	}
	
	private class SpawnPivotState implements State {
		private PausedState ps;
		
		private GameAction spawnPivot;
		
		public SpawnPivotState(PausedState ps) {
			this.ps = ps;
			
			this.spawnPivot= new GameAction("spawn pivot", GameAction.DETECT_INITIAL_PRESS_ONLY);
		}
		
		public void handleInput() {
			if (this.spawnPivot.isPressed()) {
				System.out.println("Add pivot");
				this.ps.se.addPivot(this.ps.se.getMousePosition());
			}
		}
		
		public void render(Graphics2D g) {
			
		}
		
		public void enter() {
			System.out.println("Entering spawn pivot state");
			InputManager im = this.ps.se.getInputManager();
			im.mapToMouse(this.spawnPivot, InputManager.MOUSE_BUTTON_1);
		}
		
		public void exit() {
			System.out.println("Exiting spawn pivot state");
			InputManager im = this.ps.se.getInputManager();
			im.mapToMouse(null, InputManager.MOUSE_BUTTON_1);
		}
	}
	
	private class SpawnSpringState implements State {
		private PausedState ps;
		
		private GameAction select;
		private Stack<Hoverable> selections;
		
		public SpawnSpringState(PausedState ps) {
			this.ps = ps;
			
			this.select= new GameAction("spawn spring", GameAction.DETECT_INITIAL_PRESS_ONLY);
			this.selections = new Stack<>();
		}
		
		public void handleInput() {
			if (this.select.isPressed()) {
				List<Particle> particles = this.ps.se.getParticles();
				List<GUIPivot> pivots = this.ps.se.getPivots();
				
				for (Particle p : particles) {
					if (((GUIParticle)p).mouseOver(this.ps.se.getMousePosition())) {
						this.selections.push((GUIParticle)p);
					}
				}
				
				for (GUIPivot p : pivots) {
					if (p.mouseOver(this.ps.se.getMousePosition())) {
						this.selections.push(p);
					}
				}
				
				if (this.selections.size() >= 2) {
					Hoverable a = this.selections.pop();
					Hoverable b = this.selections.pop();
					
					if (a instanceof GUIParticle && b instanceof GUIParticle) {
						this.ps.se.addForce(new GUISpring(new Spring((GUIParticle)a, (GUIParticle)b, 400, 100)));
					} else if (a instanceof GUIParticle && b instanceof GUIPivot) {
						this.ps.se.addForce(new GUIPivotedSpring(new PivotedSpring((GUIParticle)a, ((GUIPivot)b).getPosition(), 400, 100)));
					} else if (a instanceof GUIPivot && b instanceof GUIParticle) {
						this.ps.se.addForce(new GUIPivotedSpring(new PivotedSpring((GUIParticle)b, ((GUIPivot)a).getPosition(), 400, 100)));
					}
					
					this.selections.clear();
				}
			}
		}
		
		public void render(Graphics2D g) {
			if (this.selections.size() == 1) {
				Hoverable p = this.selections.peek();
				if (p instanceof GUIParticle) {
					double l = Vec2.sub(((GUIParticle)p).getPosition(), this.ps.se.getMousePosition()).mag();
					if (l > ((GUIParticle)p).getRadius()) {
						GraphicsUtils.drawSpring(g, ((GUIParticle)p).getPosition(), this.ps.se.getMousePosition(), 150, l);
					}
				} else if (p instanceof GUIPivot) {
					double l = Vec2.sub(((GUIPivot)p).getPosition(), this.ps.se.getMousePosition()).mag();
					if (l > GUIPivot.PIVOT_RADIUS) {
						GraphicsUtils.drawSpring(g, ((GUIPivot)p).getPosition(), this.ps.se.getMousePosition(), 150, l);
					}
				}
			}
		}
		
		public void enter() {
			System.out.println("Entering spawn spring state");
			InputManager im = this.ps.se.getInputManager();
			im.mapToMouse(this.select, InputManager.MOUSE_BUTTON_1);
		}
		
		public void exit() {
			System.out.println("Exiting spawn spring state");
			InputManager im = this.ps.se.getInputManager();
			im.mapToMouse(null, InputManager.MOUSE_BUTTON_1);
		}
	}
}








