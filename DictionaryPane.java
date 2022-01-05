import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class DictionaryPane extends JPanel {
	public DictionaryPane(SlangDictionary dict) {
		setLayout(new BorderLayout());

		var slangDetailPane = new SlangDetailPane(dict);

		var searchField = new JTextField("Search...");
		searchField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent event) {
				searchField.setText("");
			}
		});
		searchField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_ENTER) {
					var query = searchField.getText().toUpperCase();
					searchField.setText("");
					var meanings = dict.getMeanings(query);
					if (meanings != null) 
						slangDetailPane.setSlang(query);
					else
						JOptionPane.showMessageDialog(null, "Can't find the slang \"" + query + '"');
				}
			}
		});

		add(searchField, BorderLayout.NORTH);

		var list = new JList<String>(dict.getAllSlangs().toArray(new String[0]));
		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				slangDetailPane.setSlang(list.getSelectedValue());
			}
		});
		
		add(new JScrollPane(list), BorderLayout.WEST);
		add(slangDetailPane, BorderLayout.CENTER);
	}
}
