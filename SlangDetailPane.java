import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class SlangDetailPane extends JPanel {
	JTextField slangField;
	ArrayList<JTextField> meaningFields;
	JButton saveButton, deleteButton, editButton, addButton;
	
	SlangDictionary dictionary;

	String editedSlang;

	public SlangDetailPane(SlangDictionary dict, int maxMeaning) {
		dictionary = dict;

		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(5, 5, 5, 5));

		slangField = new JTextField();
		slangField.setEditable(false);
		slangField.setFont(new Font(slangField.getFont().getFontName(), Font.BOLD, 30));

		add(slangField, BorderLayout.NORTH);

		var meaningPane = new JPanel();
		meaningPane.setLayout(new BoxLayout(meaningPane, BoxLayout.Y_AXIS));
		
		meaningFields = new ArrayList<JTextField>();
		for (int i = 0; i < maxMeaning; i++) {
			var meaningField = new JTextField();
			meaningField.setEditable(false);
			meaningFields.add(meaningField);
			meaningPane.add(meaningField);
		}

		add(meaningPane, BorderLayout.CENTER);
		
		var buttonPane = new JPanel();

		saveButton = new JButton("Save");
		saveButton.setEnabled(false);
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				changeEditMode(false);
				try {
					var slang = slangField.getText().toUpperCase();
					boolean updateOk = true;
					if (dictionary.getMeanings(slang) != null) {
						int res = JOptionPane.showConfirmDialog(SlangDetailPane.this, "This slang has already existed\nDo you want to update it?");
						updateOk = res == JOptionPane.OK_OPTION;
					}

					if (updateOk) {
						if (editedSlang != null && editedSlang.length() > 0)
							dictionary.deleteSlang(editedSlang);
						dictionary.addSlang(slang, SlangDetailPane.this.getMeaningsFromFields());
						JOptionPane.showMessageDialog(SlangDetailPane.this, "Saved!");
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		buttonPane.add(saveButton);

		deleteButton = new JButton("Delete");
		deleteButton.setEnabled(false);
		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				changeEditMode(false);
				try {
					var slang = slangField.getText().toUpperCase();
					boolean updateOk = true;
					if (dictionary.getMeanings(slang) != null) {
						int res = JOptionPane.showConfirmDialog(SlangDetailPane.this, "Do you want to delete this slang?");
						updateOk = res == JOptionPane.OK_OPTION;
					}
					else 
						JOptionPane.showMessageDialog(SlangDetailPane.this, "This slang doesn't exist");

					if (updateOk) {
						if (slang != null && slang.length() > 0)
							dictionary.deleteSlang(slang);
						clearTextFields();
						JOptionPane.showMessageDialog(SlangDetailPane.this, "Deleted!");
					}
					deleteButton.setEnabled(false);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		buttonPane.add(deleteButton);
		
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
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				clearTextFields();
				changeEditMode(true);
			}
		});
		
		buttonPane.add(addButton);

		var randomButton = new JButton("Random slang");
		randomButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				var slangs = dict.getRandomSlangs(1);
				setSlang(slangs.iterator().next());
			}
		});
		
		buttonPane.add(randomButton);

		add(buttonPane, BorderLayout.SOUTH);
	}

	public SlangDetailPane(SlangDictionary dict) {
		this(dict, 3);
	}

	void clearTextFields() {
		slangField.setText("");
		for (var field: meaningFields) 
			field.setText("");
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
		deleteButton.setEnabled(true);
		
		var meanings = dictionary.getMeanings(slang);
		int lim1 = meaningFields.size(), lim2 = meanings.size();
		
		if (lim2 > lim1)
			System.err.println("Warning: Slang " + slang + " has " + lim2 + " meanings");

		for (int i = 0; i < lim1; i++) {
			String text = "";
			if (i < lim2)
				text = meanings.get(i);
			meaningFields.get(i).setText(text);
		}
	}
}
