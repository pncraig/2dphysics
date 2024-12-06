package tests;

import java.awt.Graphics2D;

import java.awt.Color;
import java.awt.event.KeyEvent;

import graphics.GameCore;
import graphics.InputManager;
import graphics.GameAction;


import physics.*;

public class Test1 extends GameCore {
    public static void main(String[] args) {
        Test1 t = new Test1();
        t.run();
    }

    private long startTime = System.currentTimeMillis();
    private ParticleSystem ps = new ParticleSystem();
    private NumericalSolver ns = new RK4Solver();
    private Particle p = new Particle(200, 200, 0, 0, 20);
    private Particle p2 = new Particle(300, 300, 0, 0, 10);
    private Particle p3 = new Particle(400, 100, 0, 0, 10);
    private Spring s = new Spring(this.p, this.p2, 100, 100);
    private Spring s2 = new Spring(this.p2, this.p3, 100, 100);
    private Spring s3 = new Spring(this.p3, this.p, 200, 250);
    private Force g = new Gravity(this.ps, 98.1);

    private GameAction exit;
    private InputManager im;

    private double initialEnergy;

    @Override
    public void init() {
        super.init();
        this.ps.addParticle(this.p);
        this.ps.addParticle(this.p2);
        this.ps.addParticle(this.p3);
        //this.ps.addForce(this.g);
        this.ps.addForce(this.s);
        this.ps.addForce(this.s2);
        this.ps.addForce(this.s3);

        this.im = new InputManager(this.screen.getFullScreenWindow());
        this.exit = new GameAction("exit", GameAction.DETECT_INITIAL_PRESS_ONLY);
        this.im.mapToKey(this.exit, KeyEvent.VK_ESCAPE);

        //this.initialEnergy = this.s.calculateEnergy();
        this.initialEnergy = this.s.calculateEnergy() + this.s2.calculateEnergy() + this.s3.calculateEnergy();
    }

    @Override
    public void update(long elapsedTime) {

        if (exit.isPressed()) {
            this.stop();
        }

        double dt = elapsedTime / 1000.0;
        this.ns.step(this.ps, dt);
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.WHITE);

        GraphicsUtils.drawParticle(g, this.p);
        GraphicsUtils.drawParticle(g, this.p2);
        GraphicsUtils.drawParticle(g, this.p3);
        GraphicsUtils.drawSpring(g, this.s);
        GraphicsUtils.drawSpring(g, this.s2);
        GraphicsUtils.drawSpring(g, this.s3);

        double currentEnergy = this.p.calculateEnergy() + this.p2.calculateEnergy() + this.p3.calculateEnergy() + this.s.calculateEnergy() + this.s2.calculateEnergy() + this.s3.calculateEnergy();
        //double currentEnergy = this.p.calculateEnergy() + this.p2.calculateEnergy() + this.s.calculateEnergy();
        g.drawString(String.format("Energy: %.2f/%.2f", currentEnergy, this.initialEnergy), 0, 40);
    }
}