package graphics;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.Toolkit;
import java.awt.Point;
import java.awt.Component;
import java.awt.Robot;
import java.awt.Cursor;
import java.awt.AWTException;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import javax.swing.SwingUtilities;
import java.util.List;
import java.util.ArrayList;

/**
 * InputManager manages input of key and mouse events.
 * Events are mapped to GameActions.
 */
public class InputManager implements
    KeyListener,
    MouseListener,
    MouseMotionListener,
    MouseWheelListener
{
    /**
     * An invisible cursor.
     */
    public static final Cursor INVISIBLE_CURSOR =
        Toolkit.getDefaultToolkit().createCustomCursor(
            Toolkit.getDefaultToolkit().getImage(""),
            new Point(0, 0),
            "invisible"
        );

    // mouse codes
    public static final int MOUSE_MOVE_LEFT = 0;
    public static final int MOUSE_MOVE_RIGHT = 1;
    public static final int MOUSE_MOVE_UP = 2;
    public static final int MOUSE_MOVE_DOWN = 3;
    public static final int MOUSE_WHEEL_UP = 4;
    public static final int MOUSE_WHEEL_DOWN = 5;
    public static final int MOUSE_BUTTON_1 = 6;
    public static final int MOUSE_BUTTON_2 = 7;
    public static final int MOUSE_BUTTON_3 = 8;

    private static final int NUM_MOUSE_CODES = 9;

    private static final int NUM_KEY_CODES = 600;

    private GameAction[] keyActions = new GameAction[NUM_KEY_CODES];
    private GameAction[] mouseActions = new GameAction[NUM_MOUSE_CODES];

    private Point mouseLocation;
    private Point pastMouseLocation;
    private Point centerLocation;
    private Component comp;
    private Robot robot;
    private boolean isRecentering;

    /**
     * Creates a new InputManager that listens to input from the
     * specified component.
     *
     * @param comp the component that events are received from
     */
    public InputManager(Component comp) {
        this.comp = comp;
        this.mouseLocation = new Point();
        this.centerLocation = new Point();

        // register key and mouse listeners
        this.comp.addKeyListener(this);
        this.comp.addMouseListener(this);
        this.comp.addMouseMotionListener(this);
        this.comp.addMouseWheelListener(this);

        // allow input of the TAB key and other keys normally
        // used for focus traversal
        this.comp.setFocusTraversalKeysEnabled(false);
    }

    /**
     * Sets the cursor on this InputManager's input component
     *
     * @param curser the new cursor
     */
    public void setCursor(Cursor cursor) {
        this.comp.setCursor(cursor);
    }

    /**
     * Sets whether relative mouse mode is on or not.
     *
     * @param mode true to turn on relative mouse mode
     */
    public void setRelativeMouseMode(boolean mode) {
        if (mode == this.isRelativeMouseMode()) {
            return;
        }

        if (mode) {
            try {
                this.robot = new Robot();
                this.recenterMouse();
            } catch (AWTException e) {
                // If a robot can't be created
                this.robot = null;
            }
        } else {
            this.robot = null;
        }
    }

    /**
     * Returns whether relative mouse mode is on.
     *
     * @return true if relative mouse mode is on
     */
    public boolean isRelativeMouseMode() {
        return this.robot != null;
    }

    /**
     * Maps a GameAction to a specific key. The key codes are
     * defined in java.awt.KeyEvent. If the key already has a
     * GameAction mapped to it, the new GameAction overwrites
     * it.
     *
     * @param gameAction the GameAction to be mapped to the key
     * @param keyCode the key code specifying the key the
     * GameAction will map to
     */
    public void mapToKey(GameAction gameAction, int keyCode) {
        this.keyActions[keyCode] = gameAction;
    }

    /**
     * Maps a GameAction to a specific mouse action. The mouse codes
     * are defined here in InputManager. Current GameAction's are
     * overwritten.
     *
     * @param gameAction the GameAction
     * @param mouseCode the mouse button the GameAction is mapped to
     */
    public void mapToMouse(GameAction gameAction, int mouseCode) {
        this.mouseActions[mouseCode] = gameAction;
    }

    /**
     * Clears all mapped keys and mouse actions to this GameAction.
     *
     * @param gameAction the GameAction to be cleared
     */
    public void clearMap(GameAction gameAction) {
        for (int i = 0; i < this.keyActions.length; i++) {
            if (this.keyActions[i] == gameAction) {
                this.keyActions[i] = null;
            }
        }

        for (int i = 0; i < this.mouseActions.length; i++) {
            if (this.mouseActions[i] == gameAction) {
                this.mouseActions[i] = null;
            }
        }


        gameAction.reset();
    }

    /**
     * Gets a list of the names of the keys and mouse codes
     * mapped to the GameAction.
     *
     * @param gameAction the gameAction
     * @return a string of all the mouse actions and keys mapped
     * to the GameAction
     */
    public List<String> getMaps(GameAction gameAction) {
        ArrayList<String> list = new ArrayList<>();

        for (int i = 0; i < this.keyActions.length; i++) {
            if (this.keyActions[i] == gameAction) {
                list.add(this.getKeyName(i));
            }
        }

        for (int i = 0; i < this.mouseActions.length; i++) {
            if (this.mouseActions[i] == gameAction) {
                list.add(this.getMouseName(i));
            }
        }

        return list;
    }

    /**
     * Resets all game actions to appear as if they haven't been
     * pressed
     */
    public void resetAllGameActions() {
        for (int i = 0; i < this.keyActions.length; i++) {
            if (this.keyActions[i] != null) {
                this.keyActions[i].reset();
            }
        }

        for (int i = 0; i < this.mouseActions.length; i++) {
            if (this.mouseActions[i] != null) {
                this.mouseActions[i].reset();
            }
        }
    }

    /**
     * Gets the name of a key code.
     *
     * @param keyCode the key code we want to know the name of
     * @return the name of the key code
     */
    public static String getKeyName(int keyCode) {
        return KeyEvent.getKeyText(keyCode);
    }

    /**
     * Gets the name of a mouse code.
     *
     * @param mouseCode the mouse code we want to know the name of
     * @return the name of the mouse code
     */
    public static String getMouseName(int mouseCode) {
        switch (mouseCode) {
        case MOUSE_MOVE_LEFT: return "Mouse Left";
        case MOUSE_MOVE_RIGHT: return "Mouse Right";
        case MOUSE_MOVE_UP: return "Mouse Up";
        case MOUSE_MOVE_DOWN: return "Mouse Down";
        case MOUSE_WHEEL_UP: return "Mouse Wheel Down";
        case MOUSE_WHEEL_DOWN: return "Mouse Wheel Down";
        case MOUSE_BUTTON_1: return "Mouse Button 1";
        case MOUSE_BUTTON_2: return "Mouse Button 2";
        case MOUSE_BUTTON_3: return "Mouse Button 3";
        default: return "Unknown mouse code " + mouseCode;
        }
    }

    /**
     * Gets the x-position of the mouse.
     *
     * @return the x-position of the mouse
     */
    public int getMouseX() {
        return this.mouseLocation.x;
    }

    /**
     * Gets the y-position of the mouse.
     *
     * @return the y-position of the mouse
     */
    public int getMouseY() {
        return this.mouseLocation.y;
    }

    /**
     * Uses the Robot class to try to position the mouse in the
     * center of the screen. Sometimes this doesn't work.
     */
    private synchronized void recenterMouse() {
        if (this.robot != null && this.comp.isShowing()) {
            this.centerLocation.x = this.comp.getWidth() / 2;
            this.centerLocation.y = this.comp.getHeight() / 2;
            SwingUtilities.convertPointToScreen(this.centerLocation, this.comp);
            this.isRecentering = true;
            this.robot.mouseMove(this.centerLocation.x, this.centerLocation.y);
        }
    }

    /**
     * Get the GameAction that should occur when the key
     * specified by the KeyEvent is pressed.
     *
     * @param e the KeyEvent that has happened
     * @return the GameAction that should happen
     */
    private GameAction getKeyAction (KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode < this.keyActions.length) {
            return this.keyActions[keyCode];
        } else {
            return null;
        }
    }

    /**
     * Gets the mouse code for the button specified in this
     * MouseEvent.
     *
     * @param e MouseEvent that has happened
     * @return the mouse code
     */
    public static int getMouseButtonCode(MouseEvent e) {
        switch (e.getButton()) {
        case MouseEvent.BUTTON1:
            return MOUSE_BUTTON_1;
        case MouseEvent.BUTTON2:
            return MOUSE_BUTTON_2;
        case MouseEvent.BUTTON3:
            return MOUSE_BUTTON_3;
        default:
            return -1;
        }
    }

    /**
     * Get the GameAction corresponding to the MouseEvent
     *
     * @param e MouseEvent that has happened
     * @return the GameAction that should happen
     */
    private GameAction getMouseButtonAction (MouseEvent e) {
        int mouseCode = this.getMouseButtonCode(e);

        if (mouseCode != -1) {
            return this.mouseActions[mouseCode];
        } else {
            return null;
        }
    }

    // from the KeyListener interface
    public void keyPressed(KeyEvent e) {
        GameAction gameAction = this.getKeyAction(e);
        
        if (gameAction != null) {
            gameAction.press();
        }
        // make sure the key isn't processed for anything else
        e.consume();
    }

    // from the KeyListener interface
    public void keyReleased(KeyEvent e) {
        GameAction gameAction = this.getKeyAction(e);
        
        if (gameAction != null) {
            gameAction.release();
        }
        e.consume();
    }

    // from the KeyListener interface
    public void keyTyped(KeyEvent e) {
        e.consume();
    }

    // from the MouseListener interface
    public void mousePressed(MouseEvent e) {
        GameAction gameAction = this.getMouseButtonAction(e);
        System.out.println(e.getClickCount());
        if (gameAction != null) {
            gameAction.press();
        }
    }

    // from the MouseListener interface
    public void mouseReleased(MouseEvent e) {
        GameAction gameAction = this.getMouseButtonAction(e);
        if (gameAction != null) {
            gameAction.release();
        }
    }

    // from the MouseListener interface
    public void mouseClicked(MouseEvent e) {
        // do nothing
    }

    // from the MouseListener interface
    public void mouseEntered(MouseEvent e) {
        this.mouseMoved(e);
    }

    // from the MouseListener interface
    public void mouseExited(MouseEvent e) {
        this.mouseMoved(e);
    }

    // from the MouseListener interface
    public void mouseDragged(MouseEvent e) {
        this.mouseMoved(e);
    }

    // from the MouseMotionListener interface
    public synchronized void mouseMoved(MouseEvent e) {
        // this event is from recentering the mouse - ignore it
        if (this.isRecentering &&
            this.centerLocation.x == e.getX() &&
            this.centerLocation.y == e.getY()) {
            this.isRecentering = false;
        } else {
            int dx = e.getX() - this.mouseLocation.x;
            int dy = e.getY() - this.mouseLocation.y;

            this.mouseHelper(MOUSE_MOVE_LEFT, MOUSE_MOVE_RIGHT, dx);
            this.mouseHelper(MOUSE_MOVE_UP, MOUSE_MOVE_DOWN, dy);

            if (this.isRelativeMouseMode()) {
                this.recenterMouse();
            }
        }

        this.mouseLocation.x = e.getX();
        this.mouseLocation.y = e.getY();
    }

    // from the MouseWheelListener interface
    public void mouseWheelMoved(MouseWheelEvent e) {
        this.mouseHelper(MOUSE_WHEEL_UP, MOUSE_WHEEL_DOWN, e.getWheelRotation());
    }

    private void mouseHelper(int codeNeg, int codePos, int amt) {
        GameAction gameAction;
        if (amt < 0) {
            gameAction = this.mouseActions[codeNeg];
        } else {
            gameAction = this.mouseActions[codePos];
        }

        if (gameAction != null) {
            gameAction.press(Math.abs(amt));
            gameAction.release();
        }
    }

}