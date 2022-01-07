import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class KeywordSearchPane extends JPanel {
	public KeywordSearchPane(Gui parent, SlangDictionary dict) {
		setLayout(new BorderLayout());

		var list = new JList<String>();
		list.addMouseListener(new MouseInputAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				if (event.getClickCount() >= 2 && event.getButton() == MouseEvent.BUTTON1)
					parent.viewSlang(list.getSelectedValue());
			}
		});

		var scrollPane = new JScrollPane(list);
		add(scrollPane, BorderLayout.CENTER);

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
					var meanings = dict.getSlangs(query);
					
					if (meanings != null) {
						var model = new DefaultListModel<String>();
						model.addAll(meanings);
						list.setModel(model);
					}
					else
						JOptionPane.showMessageDialog(null, "Can't find the keyword \"" + query + '"');
				}
			}
		});

		add(searchField, BorderLayout.NORTH);
	}
}
