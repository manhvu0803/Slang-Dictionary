import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

public class DictionaryPane extends JPanel {
	public DictionaryPane(SlangDictionary dict) {
		setLayout(new BorderLayout());

		var meaningLabel = new JLabel();
		
		var list = new JList<String>(dict.getAllSlangs().toArray(new String[0]));
		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				meaningLabel.setText(dict.getMeaning(list.getSelectedValue()).toString());
			}
		});

		add(new JScrollPane(list), BorderLayout.WEST);
		add(meaningLabel);
	}
}
