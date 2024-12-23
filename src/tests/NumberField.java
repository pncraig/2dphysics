package tests;

import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import java.text.NumberFormat;

public class NumberField extends ObjectEditorField<Double> {
	
	public NumberField(String name, double defaultValue) {
		super(ObjectEditor.Fields.NUMBER, name, defaultValue);
	}
	
	@Override
	public JPanel getGUIComponent(SimObject o) {
		JPanel panel = new JPanel();
		panel.setIgnoreRepaint(true);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		JLabel name = new JLabel(this.getFieldName() + ":");
		name.setIgnoreRepaint(true);
		
		JFormattedTextField field = new JFormattedTextField(NumberFormat.getNumberInstance());
		field.setIgnoreRepaint(true);
		field.setColumns(5);
		field.setValue(this.getDefaultValue());
		
		field.addPropertyChangeListener("value", e -> {
			this.setValue(((Number)e.getNewValue()).doubleValue());
			o.addUpdatedField(() -> {
				o.updateField(this);
			});
		});
		
		panel.add(name);
		panel.add(field);
		
		return panel;
	}
}
