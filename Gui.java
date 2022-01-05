import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Gui extends JFrame {
	SlangDictionary dict;

	public Gui() throws Exception {
		dict = SlangDictionary.getInstance();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		JTabbedPane tabPane = new JTabbedPane();
		tabPane.addTab("Dictionary", new DictionaryPane(dict));
		tabPane.addTab("Quiz", new QuizPane(dict));

		add(tabPane, BorderLayout.CENTER);

		pack();
		setMinimumSize(new Dimension(500, getHeight() + 150));
		setLocationRelativeTo(null);
		setVisible(true);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							dict.close();
						}
						catch (Exception e) {
							e.printStackTrace();
						}
					}
				}).start();
			}
		});
	}
	
	public static void main(String[] args) throws Exception {
		new Gui();
	}
}