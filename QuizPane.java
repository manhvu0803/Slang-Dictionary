import javax.swing.*;
import javax.swing.border.Border;

import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.event.*;

public class QuizPane extends JPanel {
	SlangDictionary dictionary;

	JLabel questionLabel;
	List<JButton> answerButtons;

	int currentAnswer;

	class AnswerListener implements ActionListener {
		int answer;

		AnswerListener(int answer) {
			this.answer = answer;
		}
		
		@Override
		public void actionPerformed(ActionEvent event) {
			String res = "Correct!";
			if (answer != currentAnswer)
				res = "Sorry, the correct answer is:\n" + answerButtons.get(currentAnswer).getText();
			JOptionPane.showMessageDialog(null, res);
		}
	};

	public QuizPane(SlangDictionary dict) {
		dictionary = dict;

		setLayout(new BorderLayout());

		var quizOptionPane = new JPanel();

		//#region Question pane
		var questionPane = new JPanel(new GridLayout(2, 1));

		questionLabel = new JLabel("", SwingConstants.CENTER);
		questionPane.add(questionLabel);

		var answerPane = new JPanel(new GridLayout(2, 2));

		answerButtons = new ArrayList<JButton>(4);
		for (int i = 0; i < 4; i++) {
			var button = new JButton();
			button.addActionListener(new AnswerListener(i));
			answerButtons.add(button);
			answerPane.add(button);
		}

		questionPane.add(answerPane);

		add(questionPane, BorderLayout.CENTER);
		//#endregion
	}

	void checkAnswer(int answer) {
		String res = "Correct!";
		if (answer != currentAnswer)
			res = "Sorry, the correct answer is:\n" + answerButtons.get(currentAnswer).getText();
		JOptionPane.showMessageDialog(null, res);
	}
}
