package tests;

import java.util.List;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import physics.Vec2;

/**
 * ObjectEditor is a class which allows the user to
 * edit a SimObject while the application is running.
 * 
 * @author pncra
 */
public class ObjectEditor {
	/**
	 * An enum that represents the fields that can be edited
	 * 
	 * @author pncra
	 */
	public enum Fields {
		VECTOR,
		NUMBER,
		BOOLEAN;
	}
	
	/**
	 * A class which makes sure the ObjectEditor for a SimObject
	 * can be opened again after it is closed.
	 * 
	 * @author pncra
	 */
	private class OnCloseListener extends InternalFrameAdapter {
		private SimObject o;
		
		public OnCloseListener(SimObject o) {
			this.o = o;
		}
		
		@Override
		public void internalFrameClosed(InternalFrameEvent e) {
			this.o.setEditorShowing(false);
		}
	}
	
	private String editorName;
	private List<ObjectEditorField> fieldList;
	private SimObject object;
	
	public ObjectEditor(String name, SimObject object) {
		this.editorName = name;
		this.fieldList = new ArrayList<>();
		this.object = object;
	}
	
	/**
	 * Add a new field to this ObjectEditor.
	 * 
	 * @param newField the field to add
	 */
	public void addField(ObjectEditorField newField) {
		this.fieldList.add(newField);
	}
	
	/**
	 * Get the field with a specific name. Returns null
	 * if the name can't be found.
	 * 
	 * @param name the name of the field
	 * @return the field with the name name
	 */
	public ObjectEditorField getField(String name) {
		for (int i = 0; i < this.fieldList.size(); i++) {
			if (this.fieldList.get(i).getFieldName().equals(name)) {
				return this.fieldList.get(i);
			}
		}
		
		return null;
	}
	
	/**
	 * Get the frame of the ObjectEditor.
	 * @param location the point where the editor should be displayed
	 * @return the frame of the ObjectEditor
	 */
	public JInternalFrame build(Vec2 location) {
		JInternalFrame frame = new JInternalFrame(this.editorName, false, true, false, true);
		frame.setIgnoreRepaint(true);

		JPanel panel = new JPanel();
		panel.setIgnoreRepaint(true);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		for (int i = 0; i < this.fieldList.size(); i++) {
			panel.add(this.fieldList.get(i).getGUIComponent(this.object));
		}
		
		frame.addInternalFrameListener(new ObjectEditor.OnCloseListener(this.object));
		frame.add(panel);
		frame.setLocation((int)location.getX(), (int)location.getY());
		frame.pack();
		frame.setVisible(true);
		return frame;
	}
	
}
