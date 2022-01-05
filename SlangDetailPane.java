import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class SlangDetailPane extends JPanel {
	JTextField slangField;
	ArrayList<JTextField> meaningFields;
	JButton saveButton, editButton, addButton;
	
	SlangDictionary dictionary;

	String editedSlang;

	public SlangDetailPane(SlangDictionary dict, int maxMeaning) {
		dictionary = dict;

		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(5, 5, 5, 5));

		slangField = new JTextField("slang");
		slangField.setEditable(false);
		slangField.setFont(new Font(slangField.getFont().getFontName(), Font.BOLD, 30));

		add(slangField, BorderLayout.NORTH);

		var meaningPane = new JPanel();
		meaningPane.setLayout(new BoxLayout(meaningPane, BoxLayout.Y_AXIS));
		
		meaningFields = new ArrayList<JTextField>();
		for (int i = 0; i < maxMeaning; i++) {
			var meaningField = new JTextField("Meaning " + i);
			meaningField.setEditable(false);
			meaningFields.add(meaningField);
			meaningPane.add(meaningField);
		}

		add(meaningPane, BorderLayout.CENTER);
		
		var buttonPane = new JPanel();

		saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				changeEditMode(false);
				try {
					var slang = slangField.getText();
					dictionary.deleteSlang(editedSlang);
					dictionary.addSlang(slang, SlangDetailPane.this.getMeaningsFromFields());
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		buttonPane.add(saveButton);
		
		editButton = new JButton("Edit");
		editButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				editedSlang = slangField.getText();
				changeEditMode(true);
			}
		});
		
		buttonPane.add(editButton);
		
		addButton = new JButton("New slang");
		
		buttonPane.add(addButton);

		add(buttonPane, BorderLayout.SOUTH);
	}

	public SlangDetailPane(SlangDictionary dict) {
		this(dict, 3);
	}

	void changeEditMode(boolean edit) {
		slangField.setEditable(edit);
		saveButton.setEnabled(edit);
		for (var field: meaningFields)
			field.setEditable(edit);
	}

	List<String> getMeaningsFromFields() {
		var meanings = new ArrayList<String>(meaningFields.size());
		for (var field: meaningFields) {
			String text = field.getText();
			if (text.length() > 0);
				meanings.add(text);
		}
		return meanings;
	}

	public void setSlang(String slang) {
		slangField.setText(slang);
		
		var meanings = dictionary.getMeanings(slang);
		int lim1 = meaningFields.size(), lim2 = meanings.size();
		
		if (lim2 > lim1)
			System.err.println("Warning: Slang " + slang + " has " + lim2 + " meanigs");

		for (int i = 0; i < lim1; i++) {
			String text = "";
			if (i < lim2)
				text = meanings.get(i);
			meaningFields.get(i).setText(text);
		}
	}
}
