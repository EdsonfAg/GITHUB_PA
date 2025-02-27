package Ejercicios;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.JFrame;

public class FlashCardMath extends JFrame {
	private JLabel questionLabel;
    private JTextField answerField;
    private JButton submitButton;
    private JButton nextButton;
    private int num1, num2, correctAnswer;
    private Random random;

    public FlashCardMath() {
        setTitle("Flash Card Math Quiz");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        random = new Random();
        generateNewQuestion();

        questionLabel = new JLabel(getQuestionText());
        add(questionLabel);

        answerField = new JTextField(5);
        add(answerField);

        submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkAnswer();
            }
        });
        add(submitButton);

        nextButton = new JButton("Next");
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextQuestion();
            }
        });
        add(nextButton);
    }

    private void checkAnswer() {
        try {
            int userAnswer = Integer.parseInt(answerField.getText());
            if (userAnswer == correctAnswer) {
                JOptionPane.showMessageDialog(this, "Correct!");
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect. Try again.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number.");
        }
    }

    private void nextQuestion() {
        generateNewQuestion();
        questionLabel.setText(getQuestionText());
        answerField.setText("");
    }

    private void generateNewQuestion() {
        num1 = random.nextInt(10);
        num2 = random.nextInt(10);
        correctAnswer = num1 + num2;
    }

    private String getQuestionText() {
        return num1 + " + " + num2 + " = ?";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FlashCardMath().setVisible(true));
    }
}
