package tests;

import javax.swing.JInternalFrame;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.GridLayout;

public class EditorFrame extends JInternalFrame {
	private final EditorSpawnMode EDITOR_SPAWN_MODE;
	private final EditorInspectMode EDITOR_INSPECT_MODE;
	private final EditorRemoveMode EDITOR_REMOVE_MODE;
	private final EditorUnpausedMode EDITOR_UNPAUSED_MODE;
	
	private SimulationEditor se;
	private EditorMode em;
	
	JButton pausePlayButton;
	JButton stepButton;
	JButton quitButton;
	
	JRadioButton inspectButton;
	JRadioButton spawnButton;
	JRadioButton removeButton;
	
	JComboBox<String> spawnOptions;
	
	public EditorFrame(SimulationEditor se) {
		super("Editor Menu");
		this.setMaximizable(false);
		this.setResizable(false);
		this.setClosable(false);
		this.setIconifiable(true);
		
		EDITOR_SPAWN_MODE = new EditorSpawnMode(se, this);
		EDITOR_INSPECT_MODE = new EditorInspectMode(se);
		EDITOR_REMOVE_MODE = new EditorRemoveMode(se, this);
		EDITOR_UNPAUSED_MODE = new EditorUnpausedMode(se, this);
		
		this.se = se;
		
		this.em = EDITOR_INSPECT_MODE;
		this.em.enter();
		
		// The panel that holds all the buttons
		JPanel editorPanel = new JPanel();
		editorPanel.setIgnoreRepaint(true);
		editorPanel.setLayout(new BoxLayout(editorPanel, BoxLayout.Y_AXIS));
		
		// The panel that holds options that control the simulation,
		// like pause/play, step, and quit
		JPanel controlPanel = new JPanel();
		editorPanel.setIgnoreRepaint(true);
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));
		
		this.pausePlayButton = new JButton(" Play");
		this.stepButton = new JButton("Step");
		this.quitButton = new JButton("Quit");
		
		this.pausePlayButton.setIgnoreRepaint(true);
		this.stepButton.setIgnoreRepaint(true);
		this.quitButton.setIgnoreRepaint(true);
		
		this.pausePlayButton.setFocusable(false);
		this.stepButton.setFocusable(false);
		this.quitButton.setFocusable(false);
		
		this.pausePlayButton.addActionListener(e -> this.pause());
		this.quitButton.addActionListener(e -> this.quit());
		
		controlPanel.add(this.pausePlayButton);
		controlPanel.add(this.stepButton);
		controlPanel.add(this.quitButton);
		
		editorPanel.add(controlPanel);
		JSeparator separator = new JSeparator();
		separator.setIgnoreRepaint(true);
		editorPanel.add(separator);
		
		// The panel that holds the options available to you
		// to create a simulation
		JPanel optionsPanel = new JPanel();
		editorPanel.setIgnoreRepaint(true);
		optionsPanel.setLayout(new GridLayout(4, 1));
		
		this.inspectButton = new JRadioButton("Inspect");
		this.inspectButton.setSelected(true);
		
		this.spawnButton = new JRadioButton("Spawn");
		this.removeButton = new JRadioButton("Remove");
		
		this.inspectButton.setIgnoreRepaint(true);
		this.spawnButton.setIgnoreRepaint(true);
		this.removeButton.setIgnoreRepaint(true);
		
		this.inspectButton.setFocusable(false);
		this.spawnButton.setFocusable(false);
		this.removeButton.setFocusable(false);
		
		this.inspectButton.addActionListener(e -> this.switchModes(EDITOR_INSPECT_MODE));
		this.spawnButton.addActionListener(e -> this.switchModes(EDITOR_SPAWN_MODE));
		this.removeButton.addActionListener(e -> this.switchModes(EDITOR_REMOVE_MODE));
		
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(this.inspectButton);
		buttonGroup.add(this.spawnButton);
		buttonGroup.add(this.removeButton);
		
		this.spawnOptions = new JComboBox<>();
		this.spawnOptions.setIgnoreRepaint(true);
		for (SimObject.Types type : SimObject.Types.values()) {
			this.spawnOptions.addItem(type.toString());
		}
		this.spawnOptions.setEnabled(false);
		this.spawnOptions.setFocusable(false);
		
		optionsPanel.add(this.inspectButton);
		optionsPanel.add(this.spawnButton);
		optionsPanel.add(this.spawnOptions);
		optionsPanel.add(this.removeButton);
		
		editorPanel.add(optionsPanel);
		
		this.add(editorPanel);
		
		this.pack();

		this.setFocusCycleRoot(false);
		this.setFocusTraversalKeysEnabled(false);
		this.setFocusable(false);
		this.setIgnoreRepaint(true);
		this.setVisible(true);
		this.setOpaque(false);
	}
	
	/**
	 * Method that is called by the quit button. Ends the program. 
	 */
	private void quit() {
		this.dispose();
		this.se.stop();
	}
	
	/**
	 * Method that is called by the pause button. Pauses or unpauses the
	 * simulation.
	 */
	public void pause() {
		this.se.setPaused(!this.se.isPaused());
		
		if (!this.se.isPaused()) {
			this.switchModes(EDITOR_UNPAUSED_MODE);
		} else {
			this.switchModes(EDITOR_INSPECT_MODE);
		}
	}
	
	/**
	 * Switch to a different editor mode
	 * 
	 * @param newMode the mode being switched to
	 */
	private void switchModes(EditorMode newMode) {
		this.em.exit();
		this.em = newMode;
		this.em.enter();
	}
	
	/**
	 * Returns the EditorMode this EditorFrame is 
	 * in.
	 * 
	 * @return the current EditorMode
	 */
	public EditorMode getEditorMode() {
		return this.em;
	}
	
	/**
	 * Get the current spawn type.
	 * 
	 * @return the current spawn type
	 */
	public SimObject.Types getSpawnType() {
		return SimObject.Types.getType((String)this.spawnOptions.getSelectedItem());
	}
}
















