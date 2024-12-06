package graphics;

import javax.swing.JFrame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.DisplayMode;
import java.awt.Window;
import java.awt.GraphicsConfiguration;
import java.awt.image.BufferedImage;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.Graphics2D;

/**
 * The ScreenManager class manages initializing and displaying
 * the full screen graphics.
 */
public class ScreenManager {

    private GraphicsDevice graphicsDevice;

    /**
     * Create a new instance of a {@code ScreenManager}.
     */
    public ScreenManager() {
        GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        this.graphicsDevice = environment.getDefaultScreenDevice();
    }

    /**
     * Return a list of the compatible display modes for the
     * default device.
     *
     * @return a list of display modes for the default graphics device
     */
    public DisplayMode[] getCompatibleDisplayModes() {
        return this.graphicsDevice.getDisplayModes();
    }

    /**
     * Return the first compatible display mode in a list of
     * compatible display modes.
     *
     * @param modes a list of possible display modes
     * @return the first compatible display mode
     */
    public DisplayMode findFirstCompatibleMode(DisplayMode[] modes) {
        DisplayMode[] compatibleModes = this.getCompatibleDisplayModes();
        for (DisplayMode unchecked : modes) {
            for (DisplayMode valid : compatibleModes) {
                if (unchecked.equals(valid)) {
                    return unchecked;
                }
            }
        }
        return null;
    }

    /**
     * Return the current display mode.
     *
     * @return the current display mode
     */
    public DisplayMode getCurrentDisplayMode() {
        return this.graphicsDevice.getDisplayMode();
    }

    /**
     * Enters full screen mode and changes the display mode.
     * If the specified display mode is null or not compatible,
     * or if the display mode cannot be changed, the current
     * mode is used.
     * <p>
     * The display uses a {@code BufferStrategy} with 2 buffers
     *
     * @param displayMode the intended display mode for the screen
     */
    public void setFullScreen(DisplayMode displayMode) {
        JFrame frame = new JFrame();
        frame.setUndecorated(true);
        frame.setIgnoreRepaint(true);
        frame.setResizable(false);

        this.graphicsDevice.setFullScreenWindow(frame);
        if (displayMode != null && this.graphicsDevice.isDisplayChangeSupported()) {
            try {
                this.graphicsDevice.setDisplayMode(displayMode);
            } catch (IllegalArgumentException iae) {}
        }
        frame.createBufferStrategy(2);
    }

    /**
     * Gets the graphics context for the display. The {@code ScreenManager}
     * uses double buffering, so applications must call
     * update() to show any graphics drawn.
     * <p>
     * The application must dispose of the graphics object.
     *
     * @return the graphics context of the {@code BufferStrategy}
     */
    public Graphics2D getGraphics() {
        Window w = this.getFullScreenWindow();
        if (w != null) {
            BufferStrategy strat = w.getBufferStrategy();
            return (Graphics2D)strat.getDrawGraphics();
        } else {
            return null;
        }
    }

    /**
     * Update the display.
     */
    public void update() {
        // Returns null if device is not in full screen mode
        Window window = this.graphicsDevice.getFullScreenWindow();

        if (window != null) {
            BufferStrategy strat = window.getBufferStrategy();
            // Only display buffer is contents haven't been lost
            // (the buffers are typically type VolatileImage)
            if (!strat.contentsLost()) {
                strat.show();
            }
        }
        // Sync the display on some systems
        // (necessary because this is on Linux)
        Toolkit.getDefaultToolkit().sync();
    }

    /**
     * Returns the window that is currently in full screen mode.
     * Returns null if no window is in full screen mode.
     *
     * @return the window in full screen mode
     */
    public Window getFullScreenWindow() {
        return this.graphicsDevice.getFullScreenWindow();
    }

    /**
     * Get the width of the window currently in full screen mode.
     * Return 0 if not in full screen mode.
     *
     * @return the width of the screen
     */
    public int getWidth() {
        Window w = this.getFullScreenWindow();
        if (w != null) {
            return w.getWidth();
        } else {
            return 0;
        }
    }

    /**
     * Get the height of the window currently in full screen mode.
     * Return 0 if not in full screen mode.
     *
     * @return the height of the screen
     */
    public int getHeight() {
        Window w = this.getFullScreenWindow();
        if (w != null) {
            return w.getHeight();
        } else {
            return 0;
        }
    }

    /**
     * Restore the screen's display mode.
     */
    public void restoreScreen() {
        Window w = this.getFullScreenWindow();
        if (w != null) {
            w.dispose();
        }
        this.graphicsDevice.setFullScreenWindow(null);
    }

    /**
     * Creates an image compatible with the current display.
     *
     * @param w the width of the image
     * @param h the height of the image
     * @param transparency the transparency of the image
     * @return a compatible image
     */
    public BufferedImage createCompatibleImage(int w, int h, int transparency) {
        Window window = this.getFullScreenWindow();
        if (window != null) {
            GraphicsConfiguration gc = window.getGraphicsConfiguration();
            return gc.createCompatibleImage(w, h, transparency);
        }
        return null;
    }

}
