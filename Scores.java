package application;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Scores {
	// Declaration of GUI components
	JFrame window;
	JPanel panel;
	JLabel score;
	JLabel[] namesLbl = new JLabel[5];
	JLabel[] scoresLbl = new JLabel[5];
	JButton exitGame;
	
	String[] names = new String[5];								// Keeps track of names for each score
	int[] scores = new int[5];									// Keeps track of scores for each entry
	
	public Scores(){
		window = new JFrame("Game Over");						// Creates new JFrame
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	// Exits the application when close is clicked
		
		panel = new JPanel(new GridBagLayout());				// Creates new JPanel
		panel.setBackground(Color.black);						// Makes the window's background color black
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));	// Creates a border around the panel
		
		score = new JLabel("Your score was "+Integer.toString(Game.score));	// Calculates the score and places it in a new label
		score.setFont(new Font("", Font.BOLD, 40));				// Enlarges the text in the label and makes it bold
		score.setForeground(Color.white);						// Makes the text white
		score.setHorizontalAlignment(JLabel.CENTER);			// Centers the text in the label
		
		GridBagConstraints t = new GridBagConstraints();		// Creates GridBagConstraints for the label
		t.gridx = 0;											// Sets the label position
		t.gridy = 0;											// Sets the label position
		t.gridheight = 1;										// Sets the size of the label
		t.gridwidth = 2;										// Sets the size of the label
		t.weightx = 0;											// Sets the weight of the label
		t.weighty = 0;											// Sets the weight of the label
		t.insets = new Insets(0, 0, 10, 0);						// Makes spacing below the label
		t.anchor = GridBagConstraints.CENTER;					// Centers the label in the window
		panel.add(score, t);									// Adds the label to the JPanel
		
		GridBagConstraints l = new GridBagConstraints();		// Creates GridBagConstraints for the name labels
		for(int x = 0; x <= 4; x++){							// Creates five name labels and five score labels
			namesLbl[x] = new JLabel("Name");					// Creates labels that show the names
			namesLbl[x].setFont(new Font("", Font.ITALIC, 14));	// Changes the font of the labels
			namesLbl[x].setForeground(Color.LIGHT_GRAY);		// Changes the colour of the font
			namesLbl[x].setHorizontalAlignment(JLabel.LEFT);	// Aligns the text on the left
			
			l.gridx = 0;										// Sets the position of the labels
			l.gridy = x + 1;									// Sets the position of the labels
			l.gridheight = 1;									// Sets the size of the labels
			l.gridwidth = 1;									// Sets the size of the labels
			l.weightx = 0;										// Sets the weight of the labels
			l.weighty = 0;										// Sets the weight of the labels
			l.ipady = 5;										// Creates space above and below the labels
			l.anchor = GridBagConstraints.WEST;					// Aligns the labels on the left
			panel.add(namesLbl[x], l);							// Adds the labels to the panel
			
			scoresLbl[x] = new JLabel("0");						// Creates labels that show the score
			scoresLbl[x].setFont(new Font("", Font.PLAIN, 14));	// Changes the font of the labels
			scoresLbl[x].setForeground(Color.LIGHT_GRAY);		// Changes the colour of the text
			scoresLbl[x].setHorizontalAlignment(JLabel.RIGHT);	// Aligns the text on the right
			
			l.gridx = 1;										// Sets the position of the labels
			l.gridy = x + 1;									// Sets the position of the labels
			l.gridheight = 1;									// Sets the size of the labels
			l.gridwidth = 1;									// Sets the size of the labels
			l.weightx = 0;										// Sets the weight of the labels
			l.weighty = 0;										// Sets the weight of the labels
			l.ipady = 5;										// Creates spacing above and below
			l.anchor = GridBagConstraints.EAST;					// Positions the labels on the left
			panel.add(scoresLbl[x], l);							// Adds the labels to the panel
		}
		
		GridBagConstraints b = new GridBagConstraints();		// Creates GridBagConstraints for the buttons
		exitGame = new JButton();								// Creates the buttons
		b.gridx = 0;											// Sets the position of the buttons
		b.gridy = 6;											// Sets the position of the buttons
		b.gridheight = 1;										// Sets the size of the buttons
		b.gridwidth = 2;										// Sets the size of the buttons
		b.weightx = 0;											// Sets the weight of the buttons
		b.weighty = 0;											// Sets the weight of the buttons
		b.ipadx = 10;											// Makes the button larger lengthwise
		b.ipady = 20;											// Makes the button higher
		b.fill = GridBagConstraints.BOTH;						// Makes the buttons fill the space
		b.insets = new Insets(20, 0, 0, 0);						// Adds space above the buttons
		panel.add(exitGame, b);									// Adds the buttons to the panel
		
		exitGame.setText("Exit");								// Sets the button text
		exitGame.addActionListener(new exit());					// Adds action listener to the button
		
		readScores();											// Calls method to read scores from file
		
		sort();													// Sort the scores that have been read
		
		// Checks if the score for the game is a new high score
		if(Game.score > scores[4]){
			// Puts the new high score into the array
			scores[4] = Game.score;
			// Displays a input dialog getting the player's name
			String name;
			do{name = JOptionPane.showInputDialog(null, "Please enter your name for the high scores.", "New High Score!!!", JOptionPane.QUESTION_MESSAGE);}while(name == null || name.isEmpty() || name.length() > 40);
			names[4] = name;
			// Sorts the scores after the new score is added
			sort();
		}
		
		writeScores();											// Writes the scores to the file
		
		// Displays the names and scores in the labels
		for(int x = 0; x <= 4; x++){
			namesLbl[x].setText(names[x]);
			scoresLbl[x].setText(Integer.toString(scores[x]));
		}
		
		// Finishes creating the GUI
		window.setContentPane(panel);
		window.pack();
		window.setVisible(true);
		
		Game.window.dispose();
	}
	
	class exit implements ActionListener{
		public void actionPerformed(ActionEvent e){System.exit(0);}
	}
	
	public void readScores(){
		File score = new File("Resources/scores.txt");														// Sets file being read
		FileReader read;																					// Creates FileReader
		BufferedReader readFile;																			// Creates BufferedReader
		
		if(score.exists() == true){
			try{
				read = new FileReader(score);																// Creates FileReader with the file
				readFile = new BufferedReader(read);														// Creates Buffered Reader with the FileReader
				for(int x = 0; x <= 4; x++){
					String line;																			// String for the line of text being read
					if((line = readFile.readLine()) != null){names[x] = line;}								// Puts name into array
					else{names[x] = "Name";}																// Fills blank elements
					if((line = readFile.readLine()) != null){scores[x] = Integer.parseInt(line);}			// Puts score into array
					else{scores[x] = 0;}																	// Fills blank elements
				}
				readFile.close();																			// Closes BufferedReader
				read.close();																				// Closes FileReader
			} catch(IOException e){System.err.println("There was a problem reading the high-score file.");}	// Catches exception
		}
		else{
			// Puts default values into array if the file does not exist
			for(int x = 0; x <=4; x++){
				names[x] = "Name";
				scores[x] = 0;
			}
		}
	}
	
	public void writeScores(){
		File score = new File("Resources/scores.txt");														// Sets file being written to
		FileWriter write;																					// Creates FileWriter
		BufferedWriter writeFile;																			// Creates BufferedWriter
		
		try{
			write = new FileWriter(score, false);															// Creates FileWriter and overwrites the file
			writeFile = new BufferedWriter(write);															// Creates BufferedWriter
			for(int x = 0; x <= 4; x++){
				writeFile.write(names[x]);																	// Writes name element to file
				writeFile.newLine();																		// Goes to next line
				writeFile.write(Integer.toString(scores[x]));												// Write score element to file
				writeFile.newLine();																		// Goes to next line
			}
			writeFile.close();																				// Closes the BufferedWriter
			write.close();																					// Closes the FileWriter
		} catch(IOException e){System.err.println("There was a problem writing the high-score file.");}		// Catches error reading file
	}
	
	public void sort(){
		// Switches elements where needed
		for(int index = 0; index <= 3; index++){
			if(scores[index + 1] > scores[index]){switcher(index, index + 1);}
		}
		
		for(int index = 0; index <= 3; index++){
			if(scores[index] < scores[index + 1]){
				sort();
				break;
			}
		}
	}
	
	public void switcher(int num1, int num2){
		int firstNum = scores[num1];				// Makes temporary variable with score
		String firstStr = names[num1];				// Makes temporary variable with name
		int secondNum = scores[num2];				// Makes temporary variable with score
		String secondStr = names[num2];				// Makes temporary variable with name
		
		scores[num1] = secondNum;					// Switches scores
		names[num1] = secondStr;					// Switches names
		scores[num2] = firstNum;					// Switches scores
		names[num2] = firstStr;						// Switches names
	}
}
