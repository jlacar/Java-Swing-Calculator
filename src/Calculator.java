
/*
 * Author: Alex Matthews
 * Calculator with Windows 7 calculator functionality
 * College Project
 * Ireland
 */

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.script.ScriptException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;

public class Calculator extends JFrame implements Runnable {

	private MemoryStore memoryStore = new MemoryStore();
	private JTextPane screen;
	private JPanel buttonPanel;
	private JButton[] numberButtons;
	private JButton[] operationButtons;
	private String display = "";
	private boolean shouldOverwrite = false;
	private String[] operationButtonStrings = { "ME", "MR", "MS", "M+", "M-", "←", "C", "+", "-", "*", "/", "√", "x²",
			"±", "%", ".", "1/x", "=" };

	public Calculator() {
		super("Java Calculator");
	}

	@Override
	public void run() {
		makeGUI();
	}

	private void makeGUI() {
		frameSetup();
		screenSetup();
		createNumberButtons();
		createOperationButtons();
		addButtons();
		setVisible(true);
	}

	private void frameSetup() {
		setSize(300, 300);
		setLocationRelativeTo(null); // Open frame in middle of screen
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void screenSetup() {
		screen = new JTextPane();
		screen.setBorder(BorderFactory.createBevelBorder(1));
		screen.setEditable(false);
		screen.setFont(new Font("Segoe UI", Font.BOLD, 18));
		add(screen, BorderLayout.NORTH);
	}

	private void createNumberButtons() {
		MyNumberButtonListener numberButtonListener = new MyNumberButtonListener();
		numberButtons = new JButton[10];
		for (int i = 0; i < 10; i++) {
			numberButtons[i] = new JButton(Integer.toString(i));
			numberButtons[i].addActionListener(numberButtonListener);
			numberButtons[i].setFocusable(false);
		}
	}

	private void createOperationButtons() {
		MyOperationButtonListener operationButtonListener = new MyOperationButtonListener();
		operationButtons = new JButton[18];
		for (int i = 0; i < 18; i++) {
			operationButtons[i] = new JButton(operationButtonStrings[i]);
			operationButtons[i].addActionListener(operationButtonListener);
			operationButtons[i].setFocusable(false);
		}

	}

	private void addButtons() {
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(6, 5, 1, 1));
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		for (int i = 0; i < 10; i++) {
			buttonPanel.add(numberButtons[i]);
		}
		for (int i = 0; i < 18; i++) {
			buttonPanel.add(operationButtons[i]);
		}

		buttonPanel.add(new JButton());
		buttonPanel.add(new JButton());
		add(buttonPanel);

	}

	private class MyNumberButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {

			for (int i = 0; i < 10; i++) {
				if (e.getSource() == numberButtons[i]) {
					displayNumber(i);
					break;
				}
			}

		}

	}

	private void displayNumber(int number) {
		if (shouldOverwrite) {
			screen.setText("" + number);
			shouldOverwrite = false;
		} else {
			display = screen.getText();
			screen.setText(display + number);
		}
	}

	private class MyOperationButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {

			if (e.getSource() == operationButtons[0]) {
				memoryStore.resetStoredValue();
			} else if (e.getSource() == operationButtons[1]) {
				display = screen.getText();
				screen.setText(display + "" + memoryStore.getStoredValue());
			} else if (e.getSource() == operationButtons[2]) {
				if (!screenIsEmpty())
					memoryStore.store(readScreenAsDouble());
			} else if (e.getSource() == operationButtons[3]) {
				if (!screenIsEmpty())
					memoryStore.plusStoredValue(readScreenAsDouble());
			} else if (e.getSource() == operationButtons[4]) {
				if (!screenIsEmpty())
					memoryStore.minusStoredValue(readScreenAsDouble());
			} else if (e.getSource() == operationButtons[5]) {
				backSpace();
			} else if (e.getSource() == operationButtons[6]) {
				clearScreen();
			} else if (e.getSource() == operationButtons[7]) {
				displaySymbol(operationButtons[7].getText());
			} else if (e.getSource() == operationButtons[8]) {
				displaySymbol(operationButtons[8].getText());
			} else if (e.getSource() == operationButtons[9]) {
				displaySymbol(operationButtons[9].getText());
			} else if (e.getSource() == operationButtons[10]) {
				displaySymbol(operationButtons[10].getText());
			} else if (e.getSource() == operationButtons[11]) {
				writeToScreen(CalcUtilities.sqrRoot(readScreenAsDouble()));
				shouldOverwrite = true;
			} else if (e.getSource() == operationButtons[12]) {
				writeToScreen(CalcUtilities.squared(readScreenAsDouble()));
				shouldOverwrite = true;
			} else if (e.getSource() == operationButtons[13]) {
				changeToPlusOrMinus();
			} else if (e.getSource() == operationButtons[14]) {
				displaySymbol(operationButtons[14].getText());
			} else if (e.getSource() == operationButtons[15]) {
				if (!displayHasDecimal())
					displaySymbol(operationButtons[15].getText());
			} else if (e.getSource() == operationButtons[16]) {
				writeToScreen(CalcUtilities.reciprocal(readScreenAsDouble()));
				shouldOverwrite = true;
			} else if (e.getSource() == operationButtons[17]) {
				equalsAction();
			}

		}

	}

	private double readScreenAsDouble() {
		double result;
		display = screen.getText();
		result = parseDisplayToDouble(display);
		return result;
	}

	private void writeToScreen(double numToWrite) {
		screen.setText(Double.toString(numToWrite));
	}

	public double parseDisplayToDouble(String display) {
		double result;
		try {
			result = Double.parseDouble(display);
		} catch (NumberFormatException e) {
			return 0.0;
		}
		return result;
	}

	private void displaySymbol(String symbol) {
		display = screen.getText();
		screen.setText(display + symbol);
		shouldOverwrite = false;
	}

	private void clearScreen() {
		screen.setText("");
	}

	private boolean displayHasDecimal() {
		display = screen.getText();
		if (display.indexOf('.') != -1)
			return true;
		else
			return false;
	}

	private void equalsAction() {
		display = screen.getText();
		try {
			screen.setText(CalcUtilities.evaluate(display) + "");
		} catch (ScriptException e) {
			screen.setText("Invalid");
		}
		shouldOverwrite = true;
	}

	private void backSpace() {
		if (!screenIsEmpty()) {
			display = screen.getText();
			screen.setText(display.substring(0, display.length() - 1));
		}

	}

	private void changeToPlusOrMinus() {
		display = screen.getText().toString();
		if (display.charAt(0) != '-')
			screen.setText("-" + display);
		else
			screen.setText(display.substring(1, display.length()));
		shouldOverwrite = false;
	}

	private boolean screenIsEmpty() {
		display = screen.getText().toString();
		if (display.equals(""))
			return true;
		else
			return false;
	}

}
