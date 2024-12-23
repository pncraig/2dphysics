package tests;

import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JFormattedTextField;
import java.text.NumberFormat;

import physics.Vec2;

public class VectorField extends ObjectEditorField<Vec2> {
	public VectorField(String name, Vec2 defaultValue) {
		super(ObjectEditor.Fields.VECTOR, name, defaultValue);
	}
	
	@Override
	public JPanel getGUIComponent(SimObject o) {
		JPanel panel = new JPanel();
		panel.setIgnoreRepaint(true);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		JLabel name = new JLabel(this.getFieldName() + ": <");
		name.setIgnoreRepaint(true);
		
		JFormattedTextField x = new JFormattedTextField(NumberFormat.getNumberInstance());
		JFormattedTextField y = new JFormattedTextField(NumberFormat.getNumberInstance());
		
		x.setIgnoreRepaint(true);
		y.setIgnoreRepaint(true);
		
		x.setColumns(5);
		y.setColumns(5);
		
		x.setValue(this.getDefaultValue().getX());
		y.setValue(this.getDefaultValue().getY());
		
		x.addPropertyChangeListener("value", e -> {
			this.setValue(new Vec2(((Number)e.getNewValue()).doubleValue(), this.getValue().getY()));

			o.addUpdatedField(() -> {
				o.updateField(this);
			});
		});
		y.addPropertyChangeListener("value", e -> {
			this.setValue(new Vec2(this.getValue().getX(), ((Number)e.getNewValue()).doubleValue()));
			
			o.addUpdatedField(() -> {
				o.updateField(this);
			});
		});
		
		JLabel closingBracket = new JLabel(">");
		closingBracket.setIgnoreRepaint(true);
		
		panel.add(name);
		panel.add(x);
		panel.add(y);
		panel.add(closingBracket);
		return panel;
	}
	
}














