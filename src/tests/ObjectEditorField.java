package tests;

import javax.swing.JPanel;

/**
 * This class represents a field in an
 * ObjectEditor.
 * 
 * @author pncra
 */
public abstract class ObjectEditorField<T>{
	private ObjectEditor.Fields fieldType;
	private String fieldName;
	private T defaultValue;
	private T value;
	
	public ObjectEditorField(ObjectEditor.Fields fieldType, String fieldName, T defaultValue) {
		this.fieldType = fieldType;
		this.fieldName = fieldName;
		this.defaultValue = defaultValue;
		this.value = defaultValue;
	}
	
	public ObjectEditor.Fields getFieldType() {
		return this.fieldType;
	}
	
	public String getFieldName() {
		return this.fieldName;
	}
	
	public T getDefaultValue() {
		return this.defaultValue;
	}
	
	public T getValue() {
		return this.value;
	}
	
	public void setValue(T value) {
		this.value = value;
	}
	
	public abstract JPanel getGUIComponent(SimObject o);
}
