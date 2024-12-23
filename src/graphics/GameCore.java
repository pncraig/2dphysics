package graphics;

import java.awt.Window;

import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Graphics2D;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.awt.Container;
import javax.swing.JComponent;

import java.awt.Color;

/**
 * Simple abstract class used for testing. Subclasses should
 * implement the draw() method.
 */
public abstract class GameCore {
    protected static final int FONT_SIZE = 24;

    private boolean isRunning;
    protected ScreenManager screen;

    /**
     * Signals to the game loop that it's time to quit.
     */
    public void stop() {
        isRunning = false;
    }

    /**
     * Calls init() and gameLoop().
     */
    public void run() {
        try {
            this.init();
            this.gameLoop();
        } finally {
            this.screen.restoreScreen();
        }
    }

    /**
     * Sets full screen mode and initializes objects.
     */
    public synchronized void init() {
        this.screen = new ScreenManager();
        this.screen.setFullScreen(null); // Remember, null uses the default display mode

        JFrame w = (JFrame)this.screen.getFullScreenWindow();
        w.setFont(new Font("Dialog", Font.PLAIN, FONT_SIZE));
        w.setBackground(Color.BLUE);
        w.setForeground(Color.WHITE);
        
        // We need to make sure the contentPane doesn't draw its background
        Container contentPane = w.getContentPane();
        
        if (contentPane instanceof JComponent) {
        	((JComponent)contentPane).setOpaque(false);
        }
        
        this.isRunning = true;
    }

    /**
     * Load an image.
     *
     * @param fileName the file name of the image
     *
     * @return an Image object
     */
    public Image loadImage(String fileName) {
        return new ImageIcon(fileName).getImage();
    }

    /**
     * Runs through the game loop until stop() is called.
     */
    public void gameLoop() {
        long startTime = System.currentTimeMillis();
        long currTime = startTime;

        SynchronizedEventQueue.use();
        
        while (isRunning) {
	        long elapsedTime = System.currentTimeMillis() - currTime;
	        currTime += elapsedTime;
	
	        this.update(elapsedTime);

	        synchronized (SynchronizedEventQueue.MUTEX) {
	            // Draw to the screen
	            Graphics2D g = this.screen.getGraphics();
	            g.clearRect(0, 0, this.screen.getWidth(), this.screen.getHeight());
	            
	            // Print the FPS in the top left of the screen
	            double fps = 1000.0 / elapsedTime;
	            g.drawString(String.format("FPS: %.2f", fps), 0, 20);
	            
	            this.draw(g);
	            
	            // draw Swing components
	            JFrame frame = (JFrame)this.screen.getFullScreenWindow();
	            frame.getLayeredPane().paintComponents(g);
	            
	            g.dispose();
	            this.screen.update();
        	}
        }
    }

    /**
     * Updates the state of the game based on the amount
     * of time that has passed since the last frame was rendered.
     *
     * @param elapsedTime abount of time that has passed since the last frame was drawn
     */
    public void update(long elapsedTime) {
        // do nothing, subclasses must override this method if they have anything to update
    }

    /**
     * Draws to the screen. Subclasses must override this method.
     *
     * @param g the graphics context to draw to
     */
    public abstract void draw(Graphics2D g);
}