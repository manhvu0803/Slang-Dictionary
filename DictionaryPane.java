import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.util.List;

public class DictionaryPane extends JPanel {
	List<String> searchHistory;
	SlangDetailPane slangDetailPane;

	public DictionaryPane(SlangDictionary dict) {
		searchHistory = new ArrayList<>();

		setLayout(new BorderLayout());

		slangDetailPane = new SlangDetailPane(dict);

		var searchPane = new JPanel(new BorderLayout());

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
					searchHistory.add(query);
					searchField.setText("");
					var meanings = dict.getMeanings(query);
					if (meanings != null)
						slangDetailPane.setSlang(query);
					else
						JOptionPane.showMessageDialog(DictionaryPane.this, "Can't find the slang \"" + query + '"');
				}
			}
		});

		searchPane.add(searchField, BorderLayout.CENTER);

		var historyButton = new JButton("History");
		historyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				var history = new JList<String>(searchHistory.toArray(new String[0]));
				JOptionPane.showMessageDialog(DictionaryPane.this, new JScrollPane(history));
			}
		});
		
		searchPane.add(historyButton, BorderLayout.EAST);

		add(searchPane, BorderLayout.NORTH);

		// var list = new JList<String>(dict.getAllSlangs().toArray(new String[0]));
		// list.addListSelectionListener(new ListSelectionListener() {
		// 	@Override
		// 	public void valueChanged(ListSelectionEvent e) {
		// 		slangDetailPane.setSlang(list.getSelectedValue());
		// 	}
		// });

		// add(new JScrollPane(list), BorderLayout.WEST);
		
		add(slangDetailPane, BorderLayout.CENTER);
	}

	public void setSlang(String slang) {
		slangDetailPane.setSlang(slang);
	}
}
