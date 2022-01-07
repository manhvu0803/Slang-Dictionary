import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Gui extends JFrame {
	SlangDictionary dict;
	DictionaryPane dictPane;
	JTabbedPane tabPane;

	public Gui() throws Exception {
		dict = SlangDictionary.getInstance();

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());

		tabPane = new JTabbedPane();
		dictPane = new DictionaryPane(dict);
		tabPane.addTab("Dictionary", dictPane);
		tabPane.addTab("Search by keyword", new KeywordSearchPane(this, dict));
		tabPane.addTab("Quiz", new QuizPane(dict));

		add(tabPane, BorderLayout.CENTER);

		var menuBar = new JMenuBar();
		var menu = new JMenu("Options");
		
		var resetOption = new JMenuItem("Reset dictionary");
		resetOption.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				dictPane.setSlang(null);
				try {
					dict.loadDefault();
					JOptionPane.showMessageDialog(null, "Resetting dictionary completed");
				}
				catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Error: " + e.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		menu.add(resetOption);
		
		menuBar.add(menu);

		setJMenuBar(menuBar);

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

	public void viewSlang(String slang) {
		tabPane.setSelectedIndex(0);
		dictPane.setSlang(slang);
	}
	
	public static void main(String[] args) throws Exception {
		new Gui();
	}
}