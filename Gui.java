import java.awt.*;
import javax.swing.*;

public class Gui extends JFrame {
	public Gui(SlangDictionary dict) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		JTabbedPane tabPane = new JTabbedPane();
		tabPane.addTab("Dictionary", new DictionaryPane(dict));
		tabPane.addTab("Quiz", new JPanel());

		add(tabPane, BorderLayout.CENTER);

		pack();
		setMinimumSize(new Dimension(200, getHeight()));
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
}