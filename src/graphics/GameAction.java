package graphics;

/**
 * The GameAction class is a mapping to a user-initiated
 * action, like jumping or moving. GameActions can be
 * mapped to keys or the mouse with the InputManager
 */
public class GameAction {
    public static final int NORMAL = 0;

    public static final int DETECT_INITIAL_PRESS_ONLY = 1;

    private static final int STATE_RELEASED = 0;
    private static final int STATE_PRESSED = 1;
    private static final int STATE_WAITING_FOR_RELEASE = 2;

    private String name;
    private int behavior;
    private int amount;
    private int state;

    /**
     * Create a new GameAction with NORMAL behavior.
     *
     * @param name name of GameAction
     */
    public GameAction(String name) {
        this(name, NORMAL);
    }

    /**
     * Create a new GameAction with the specified behavior.
     *
     * @param name name of GameAction
     * @param behavior what type of behavior the action has
     */
    public GameAction(String name, int behavior) {
        this.name = name;
        this.behavior = behavior;
        this.reset();
    }

    /**
     * Get the name of this GameAction.
     *
     * @return name of the GameAction
     */
    public String getName() {
        return name;
    }

    /**
     * Resets this GameAction so that it appears like it
     * hasn't been pressed.
     */
    public void reset() {
        this.state = STATE_RELEASED;
        this.amount = 0;
    }

    /**
     * Taps this GameAction.
     */
    public synchronized void tap() {
        this.press();
        this.reset();
    }

    /**
     * Signals that the key was pressed.
     */
    public synchronized void press() {
        this.press(1);
    }

    /**
     * Signals that the key was pressed a specified number of
     * times, or that the mouse moved a specified distance.
     *
     * @param amount the number of times the key was pressed
     */
    public synchronized void press(int amount) {
        if (this.state != STATE_WAITING_FOR_RELEASE) {
            this.amount += amount;
            this.state = STATE_PRESSED;
        }
    }

    /**
     * Signals that the key was released
     */
    public synchronized void release() {
        this.state = STATE_RELEASED;
    }

    /**
     * Returns whether the key was pressed or not since
     * last checked.
     *
     * @return true if the key has been pressed, false otherwise
     */
    public synchronized boolean isPressed() {
        return (this.getAmount() != 0);
    }

    /**
     * For keys, this is the number of times the key was
     * pressed since it was last checked. For mouse movement,
     * this is the distance moved.
     *
     * @return the number of times the key was pressed, or distance the mouse has moved
     */
    public synchronized int getAmount() {
        int retVal = this.amount;
        if (retVal != 0) {
            if (this.state == STATE_RELEASED) {
                this.amount = 0;
            } else if (this.behavior == DETECT_INITIAL_PRESS_ONLY) {
                this.state = STATE_WAITING_FOR_RELEASE;
                this.amount = 0;
            }
        }
        return retVal;
    }
}