import javax.swing.*;
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
			JOptionPane.showMessageDialog(QuizPane.this, res);
			newQuiz();
		}
	};

	public QuizPane(SlangDictionary dict) {
		dictionary = dict;

		setLayout(new BorderLayout());

		//#region Quiz option pane
		var quizOptionPane = new JPanel();
		
		var newSlangQuizButton = new JButton("New slang quiz");
		newSlangQuizButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				newQuiz(1);
			}
		});

		quizOptionPane.add(newSlangQuizButton);

		var newMeaningQuizButton = new JButton("New meaning quiz");
		newMeaningQuizButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				newQuiz(0);
			}
		});

		quizOptionPane.add(newMeaningQuizButton);

		add(quizOptionPane, BorderLayout.NORTH);
		//#endregion

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

		newQuiz();
	}

	/**
	 * 
	 * @param mode 0 for meaning, 1 for slang
	 */
	void newQuiz(int mode) {
		var quiz = dictionary.getRandomSlangMeaning(4);
		if (quiz.size() < 4) 
			return;
			
		var generator = new Random();
		currentAnswer = generator.nextInt(4);

		if (mode == 0) {
			questionLabel.setText("What is the meaning of \"" + quiz.get(currentAnswer)[0] + "\"");
			for (int i = 0; i < 4; i++)
				answerButtons.get(i).setText(quiz.get(i)[1]);
		}
		else {
			questionLabel.setText("What is the slang for \"" + quiz.get(currentAnswer)[1] + "\"");
			for (int i = 0; i < 4; i++)
				answerButtons.get(i).setText(quiz.get(i)[0]);
		}
	}
	
	void newQuiz() {
		newQuiz(new Random().nextInt(2));
	}

	void checkAnswer(int answer) {
		String res = "Correct!";
		if (answer != currentAnswer)
			res = "Sorry, the correct answer is:\n" + answerButtons.get(currentAnswer).getText();
		JOptionPane.showMessageDialog(null, res);
	}
}
