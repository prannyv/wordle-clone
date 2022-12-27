import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.*;

public class Main extends JPanel implements ActionListener{

	static JFrame frame;
	static JPanel myPanel;
	static JLabel label;
	static JOptionPane option;

	static JLabel[][]wordBoxes;

	static Color customRed;
	static Color customGreen;
	static Color customYellow;
	static Color customGrey;

	static JLabel winLabel;
	static Font font;

	static JButton button;
	
	static ArrayList<String> allWords;


	public Main() {
		font = new Font("Arial", Font.BOLD,40);

		label = new JLabel();
		frame.add(label);

		customRed = new Color(251,105,98);
		customGreen = new Color(121,222,121);
		customYellow = new Color(252,252,153);
		customGrey = new Color(210,192,184);

		winLabel = new JLabel();
		frame.add(winLabel);
		winLabel.setBounds(800,200,600,300);
		winLabel.setFont(new Font("Arial", Font.BOLD, 75));

		button = new JButton();
		frame.add(button);
		button.setBounds(850,420,300,180);
		button.setFont(font);
		button.setText("Play again?");
		button.setVisible(false);
		button.addActionListener(this);

		//		label.setText("hey");
		//		label.setBounds(480,40,520,360);
		//		label.setAlignmentX(RIGHT_ALIGNMENT);
		//		label.setAlignmentY(CENTER_ALIGNMENT);
		//		label.setBackground(Color.red);
		//		label.setOpaque(true);

		wordBoxes = new JLabel[6][5];
		for (int i = 0; i < wordBoxes.length; i++) {//iterating through the 6 ROWS
			for (int j = 0; j < wordBoxes[i].length; j++) {//iterating through the WORD in this row
				wordBoxes[i][j] = new JLabel();
				frame.add(wordBoxes[i][j]);
				wordBoxes[i][j].setAlignmentX(CENTER_ALIGNMENT);
				wordBoxes[i][j].setAlignmentY(CENTER_ALIGNMENT);
				wordBoxes[i][j].setHorizontalAlignment(JLabel.CENTER);
				wordBoxes[i][j].setFont(font);
				wordBoxes[i][j].setBounds(40+120*j, 40+120*i,80,80);
				wordBoxes[i][j].setBackground(customGrey);
				wordBoxes[i][j].setOpaque(true);
			}
		}



		option = new JOptionPane();
		frame.add(option);

	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub

		frame = new JFrame("Wordle App");
		myPanel = new Main();
		myPanel.setPreferredSize(new Dimension(1920,1080));
		frame.add(myPanel);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		//		frame.setBackground(new Color(212,211,219));
		myPanel.setBackground(new Color(238,237,239));




		Scanner in = new Scanner (System.in);
		allWords = new ArrayList<String>();
		try {
			allWords = inputWords();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}

		runGame();

	}

	public static void runGame() {
		
		String wordToGuess = "smile";
		wordToGuess = generateWord(allWords);
		label.setText(wordToGuess);
		System.out.println(wordToGuess);


		String curGuess = "";
		System.out.println("Guess a word!");
		
		for (int i = 1; i <=6; i++) {

			System.out.println("This is guess number: " + i);

			curGuess = option.showInputDialog("Guess a word! (" + i + ")");
			//			curGuess = in.nextLine().toLowerCase();
			while (curGuess.length()!=5) {
				curGuess = option.showInputDialog("Invalid input!");
			}
			//			System.out.println(curGuess + "—" + wordToGuess );
			changeLabelColors(curGuess,checkWord(curGuess,wordToGuess), wordBoxes, i-1);
			System.out.println(checkWord(curGuess, wordToGuess) + "\n");

			if (curGuess.equals(wordToGuess)) {
				System.out.println("you win");
				winLabel.setText("you win!");
				button.setVisible(true);
				break;
			}
			if (i == 6) {
				winLabel.setText("You lose :(");
				System.out.println("you lose");
				option.showMessageDialog(frame, ("the word was " + wordToGuess));
				button.setVisible(true);
			}
		}
	}

	public static String checkWord(String check, String word) {
		String returnWord = "";

		for (int i = 0; i < 5; i++) {
			if (check.substring(i,i+1).equals(word.substring(i,i+1)))
				returnWord += "1";
			else if (isLetterInWord(check.substring(i,i+1),word))
				returnWord += "2";
			else
				returnWord += "0";
		}


		return returnWord;		
	}

	public static boolean isLetterInWord(String letter, String word) {
		boolean returnBool = false;
		for (int i = 0; i<5; i++) {
			if (word.substring(i,i+1).equals(letter)) {
				returnBool = true;

			}
		}
		return returnBool;

	}

	public static void resetGame () {
		for (int i = 0; i < wordBoxes.length; i++) {
			for (int j = 0; j < wordBoxes[i].length; j++) {
				wordBoxes[i][j].setText("");
				wordBoxes[i][j].setBackground(customGrey);
			}
		}
		button.setVisible(false);
		winLabel.setText("");
		runGame();
	}

	public static void changeLabelColors(String word, String check, JLabel[][] wordBoxes, int guess) {
		for (int j = 0; j < wordBoxes[guess].length; j++) {//iterates through the word at index(guess)
			wordBoxes[guess][j].setText(Character.toString(word.charAt(j)));
			if (Character.toString(check.charAt(j)).equals("0")){
				wordBoxes[guess][j].setBackground(customRed);

			}
			else if (Character.toString(check.charAt(j)).equals("1")){
				wordBoxes[guess][j].setBackground(customGreen);

			}
			else if (Character.toString(check.charAt(j)).equals("2")){
				wordBoxes[guess][j].setBackground(customYellow);
			}
		}
	}

	public static String generateWord(ArrayList<String> wordList) {

		int total = wordList.size();
		int random = (int) Math.floor(Math.random()*(total+1)+0);

		return wordList.get(random);
	}

	public static ArrayList<String> inputWords() throws FileNotFoundException, IOException {

		Scanner fIn = new Scanner (new File("wordList.txt"));

		ArrayList<String> wordList = new ArrayList<String>();

		while (fIn.hasNextLine())
			wordList.add(fIn.nextLine());
		return wordList;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		resetGame();
		
	}

}
