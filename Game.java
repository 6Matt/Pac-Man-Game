package application;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

public class Game implements KeyListener{
	// Declaration of GUI components
	static JFrame window;
	static JLayeredPane layers;
	// Declaration of labels
	JLabel background;
	JLabel cage;
	JLabel[] blocks = new JLabel[19];
	static JLabel[] circles = new JLabel[4];
	static JLabel[] squares = new JLabel[240];
	static JLabel pacMan;
	static JLabel live[] = new JLabel[3];
	static JLabel scoreLbl;
	// Declaration of images
	BufferedImage backgroundImg;
	BufferedImage cageImg;
	BufferedImage[] blockImg = new BufferedImage[19];
	BufferedImage squareImg;
	BufferedImage circleImg;
	static BufferedImage[] pacManImg = new BufferedImage[5];
	// Declaration of integers
	static int[] x = new int[240];
	static int[] y = new int[240];
	public static int[][] pixels = new int[450][500];	// 0 = nothing, 1 = square, 2 = circle, 3 = obstacle
	static int currentDirection = 1;					// 1 = left, 2 = up, 3 = right, 4 = down
	static int tentativeDirection = 1;
	static int xPos = 210;
	static int yPos = 360;
	int num = 0;
	static int cycle = -1;
	static int collected = 0;
	static int lives = 3;
	static int score = 0;
	static int ghostsEaten = 0;
	// Declaration of boolean variables
	static boolean firstTimer = true;
	static boolean stop = false;
	// Declaration of movement timer
	static Timer move;
	// Declaration of ghost classes
	static Ghost one = new Ghost("Red", 211, 170, 0, 0);
	static Ghost two = new Ghost("Blue", 179, 222, 15, 4);
	static Ghost three = new Ghost("Purple", 211, 222, 15, 5);
	static Ghost four = new Ghost("Orange", 243, 222, 15, 3);
	
	public Game(){
		window = new JFrame("Pac-Man");								// Creates the window
		window.setBackground(Color.black);							// Makes background black
		window.addKeyListener(this);								// Adds key listener to the window
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		// Exits the application when close is clicked
		
		layers = new JLayeredPane();								// Creates layered pane for components
		layers.setLayout(null);										// Specifies null layout manager
		
		// Reads background image from file and places it in a label
		try {backgroundImg = ImageIO.read(new File("Resources/Background.png"));} catch (IOException e) {e.printStackTrace();}
		background = new JLabel(new ImageIcon(backgroundImg));
		background.setBounds(0, 0, 450, 500);
		layers.add(background, 1);
		
		// Displays block and adds coordinates in array
		displayBlock("R1B1", 0,		40, 40,		50, 30,		0);
		for(int x = 40; x <= 90; x++){for(int y = 40; y <= 70; y++){pixels[x][y] = 3;}}
		// Displays block and adds coordinates in array
		displayBlock("R1B2", 1,		120, 40,	65, 30,		0);
		for(int x = 120; x <= 185; x++){for(int y = 40; y <= 70; y++){pixels[x][y] = 3;}}
		// Displays block and adds coordinates in array
		displayBlock("R1B2", 2,		265, 40,	65, 30,		0);
		for(int x = 265; x <= 330; x++){for(int y = 40; y <= 70; y++){pixels[x][y] = 3;}}
		// Displays block and adds coordinates in array
		displayBlock("R1B1", 3,		360, 40,	50, 30,		0);
		for(int x = 360; x <= 410; x++){for(int y = 40; y <= 70; y++){pixels[x][y] = 3;}}
		// Displays block and adds coordinates in array
		displayBlock("R2B1", 4,		40, 100,	50, 20,		0);
		for(int x = 40; x <= 90; x++){for(int y = 100; y <= 120; y++){pixels[x][y] = 3;}}
		// Displays block and adds coordinates in array
		displayBlock("R2B2", 5,		120, 100,	65, 115,	0);
		for(int x = 120; x <= 140; x++){for(int y = 100; y <= 215; y++){pixels[x][y] = 3;}}
		for(int x = 120; x <= 185; x++){for(int y = 150; y <= 170; y++){pixels[x][y] = 3;}}
		// Displays block and adds coordinates in array
		displayBlock("R2B3", 6,		170, 100,	110, 70,	0);
		for(int x = 170; x <= 280; x++){for(int y = 100; y <= 120; y++){pixels[x][y] = 3;}}
		for(int x = 215; x <= 235; x++){for(int y = 100; y <= 170; y++){pixels[x][y] = 3;}}
		// Displays block and adds coordinates in array
		displayBlock("R2B4", 7,		265, 100,	65, 115,	0);
		for(int x = 310; x <= 330; x++){for(int y = 100; y <= 215; y++){pixels[x][y] = 3;}}
		for(int x = 265; x <= 330; x++){for(int y = 150; y <= 170; y++){pixels[x][y] = 3;}}
		// Displays block and adds coordinates in array
		displayBlock("R2B1", 8,		360, 100,	50, 20,		0);
		for(int x = 360; x <= 410; x++){for(int y = 100; y <= 120; y++){pixels[x][y] = 3;}}
		// Displays block and adds coordinates in array
		displayBlock("R3B1", 9,		120, 245,	20, 65,		0);
		for(int x = 120; x <= 140; x++){for(int y = 245; y <= 310; y++){pixels[x][y] = 3;}}
		// Displays block and adds coordinates in array
		displayBlock("R3B2", 10,	170, 290,	110, 70,	0);
		for(int x = 170; x <= 280; x++){for(int y = 290; y <= 310; y++){pixels[x][y] = 3;}}
		for(int x = 215; x <= 235; x++){for(int y = 290; y <= 360; y++){pixels[x][y] = 3;}}
		// Displays block and adds coordinates in array
		displayBlock("R3B1", 11,	310, 245,	20, 65,		0);
		for(int x = 310; x <= 330; x++){for(int y = 245; y <= 310; y++){pixels[x][y] = 3;}}
		// Displays block and adds coordinates in array
		displayBlock("R4B1", 12,	40, 340,	50, 70,		0);
		for(int x = 40; x <= 90; x++){for(int y = 340; y <= 360; y++){pixels[x][y] = 3;}}
		for(int x = 70; x <= 90; x++){for(int y = 340; y <= 410; y++){pixels[x][y] = 3;}}
		// Displays block and adds coordinates in array
		displayBlock("R4B2", 13,	120, 340,	65, 20,		0);
		for(int x = 120; x <= 185; x++){for(int y = 340; y <= 360; y++){pixels[x][y] = 3;}}
		// Displays block and adds coordinates in array
		displayBlock("R4B2", 14,	265, 340,	65, 20,		0);
		for(int x = 265; x <= 330; x++){for(int y = 340; y <= 360; y++){pixels[x][y] = 3;}}
		// Displays block and adds coordinates in array
		displayBlock("R4B3", 15,	360, 340,	50, 70,		0);
		for(int x = 360; x <= 410; x++){for(int y = 340; y <= 360; y++){pixels[x][y] = 3;}}
		for(int x = 360; x <= 380; x++){for(int y = 340; y <= 410; y++){pixels[x][y] = 3;}}
		// Displays block and adds coordinates in array
		displayBlock("R5B1", 16,	40, 390,	145, 70,	0);
		for(int x = 120; x <= 140; x++){for(int y = 390; y <= 460; y++){pixels[x][y] = 3;}}
		for(int x = 40; x <= 185; x++){for(int y = 440; y <= 460; y++){pixels[x][y] = 3;}}
		// Displays block and adds coordinates in array
		displayBlock("R3B2", 17,	170, 390,	110, 70,	0);
		for(int x = 170; x <= 280; x++){for(int y = 390; y <= 410; y++){pixels[x][y] = 3;}}
		for(int x = 215; x <= 235; x++){for(int y = 390; y <= 460; y++){pixels[x][y] = 3;}}
		// Displays block and adds coordinates in array
		displayBlock("R5B2", 18,	265, 390,	145, 70,	0);
		for(int x = 310; x <= 330; x++){for(int y = 390; y <= 460; y++){pixels[x][y] = 3;}}
		for(int x = 265; x <= 410; x++){for(int y = 440; y <= 460; y++){pixels[x][y] = 3;}}
		
		// Adds coordinates for parts of background into array
		for(int x = 0; x <= 449; x++){for(int y = 0; y <= 9; y++){pixels[x][y] = 3;}}		// Top
		for(int x = 0; x <= 10; x++){for(int y = 0; y <= 499; y++){pixels[x][y] = 3;}}		// Left side
		for(int x = 0; x <= 449; x++){for(int y = 490; y <= 499; y++){pixels[x][y] = 3;}}	// Bottom
		for(int x = 440; x <= 449; x++){for(int y = 0; y <= 499; y++){pixels[x][y] = 3;}}	// Right side
		
		// Allows pacman to leave screen on one side and return on other side
		for(int x = 0; x <= 10; x++){for(int y = 215; y <= 245; y++){pixels[x][y] = 0;}}
		for(int x = 440; x <= 449; x++){for(int y = 215; y <= 245; y++){pixels[x][y] = 0;}}
		
		// Adds coordinates for parts of background into array
		for(int x = 215; x <= 235; x++){for(int y = 0; y <= 70; y++){pixels[x][y] = 3;}}	// Top protrusion
		for(int x = 0; x <= 90; x++){for(int y = 150; y <= 215; y++){pixels[x][y] = 3;}}	// First left protrusion
		for(int x = 0; x <= 90; x++){for(int y = 245; y <= 310; y++){pixels[x][y] = 3;}}	// Second left protrusion
		for(int x = 0; x <= 40; x++){for(int y = 390; y <= 410; y++){pixels[x][y] = 3;}}	// Third left protrusion
		for(int x = 360; x <= 449; x++){for(int y = 150; y <= 215; y++){pixels[x][y] = 3;}}	// First right protrusion
		for(int x = 360; x <= 449; x++){for(int y = 245; y <= 310; y++){pixels[x][y] = 3;}}	// Second right protrusion
		for(int x = 410; x <= 449; x++){for(int y = 390; y <= 410; y++){pixels[x][y] = 3;}}	// Third right protrusion
		
		// Reads image for center cage and places it in a label
		for(int x = 170; x <= 280; x++){for(int y = 200; y <= 260; y++){pixels[x][y] = 3;}}	// Cage
		try {cageImg = ImageIO.read(new File("Resources/Cage.png"));} catch (IOException e) {e.printStackTrace();}
		cage = new JLabel(new ImageIcon(cageImg));
		cage.setBounds(170, 200, 110, 60);
		layers.add(cage, 0);
		
		// Reads image for square
		try {squareImg = ImageIO.read(new File("Resources/Square.png"));} catch (IOException e) {e.printStackTrace();}
		
		// Places all the squares in the game
		displaySquares(10, 215, 23, "X");
		displaySquares(234, 440, 23, "X");
		displaySquares(10, 440, 83, "X");
		displaySquares(10, 120, 133, "X");
		displaySquares(138, 215, 133, "X");
		displaySquares(234, 310, 133, "X");
		displaySquares(330, 440, 133, "X");
		displaySquares(10, 215, 323, "X");
		displaySquares(234, 440, 323, "X");
		displaySquares(26, 70, 373, "X");
		displaySquares(90, 215, 373, "X");
		displaySquares(234, 360, 373, "X");
		displaySquares(378, 420, 373, "X");
		displaySquares(10, 120, 423, "X");
		displaySquares(138, 215, 423, "X");
		displaySquares(234, 310, 423, "X");
		displaySquares(330, 440, 423, "X");
		displaySquares(10, 440, 473, "X");
		displaySquares(24, 45, 23, "Y");
		displaySquares(56, 70, 23, "Y");
		displaySquares(87, 125, 23, "Y");
		displaySquares(326, 365, 23, "Y");
		displaySquares(427, 465, 23, "Y");
		displaySquares(377, 412, 55, "Y");
		displaySquares(24, 80, 103, "Y");
		displaySquares(87, 125, 103, "Y");
		displaySquares(135, 320, 103, "Y");
		displaySquares(326, 366, 103, "Y");
		displaySquares(377, 412, 103, "Y");
		displaySquares(87, 125, 151, "Y");
		displaySquares(377, 412, 151, "Y");
		displaySquares(24, 80, 199, "Y");
		displaySquares(326, 366, 199, "Y");
		displaySquares(427, 465, 199, "Y");
		//middle
		displaySquares(24, 80, 247, "Y");
		displaySquares(326, 366, 247, "Y");
		displaySquares(427, 465, 247, "Y");
		displaySquares(87, 125, 295, "Y");
		displaySquares(377, 412, 295, "Y");
		displaySquares(24, 80, 343, "Y");
		displaySquares(87, 125, 343, "Y");
		displaySquares(135, 320, 343, "Y");
		displaySquares(326, 366, 343, "Y");
		displaySquares(377, 412, 343, "Y");
		displaySquares(377, 412, 391, "Y");
		displaySquares(24, 45, 423, "Y");
		displaySquares(56, 70, 423, "Y");
		displaySquares(87, 125, 423, "Y");
		displaySquares(326, 365, 423, "Y");
		displaySquares(427, 465, 423, "Y");
		
		// Reads circle image and creates labels
		try {circleImg = ImageIO.read(new File("Resources/Circle.png"));} catch (IOException e) {e.printStackTrace();}
		for(int x = 0; x <= 3; x++){circles[x] = new JLabel(new ImageIcon(circleImg));}
		// Places circle in first corner
		circles[0].setBounds(20, 50, 10, 10);
		for(int x = 20; x <= 30; x++){for(int y = 50; y <= 60; y++){pixels[x][y] = 2;}}
		// Places circle in second corner
		circles[1].setBounds(420, 50, 10, 10);
		for(int x = 420; x <= 430; x++){for(int y = 50; y <= 60; y++){pixels[x][y] = 2;}}
		// Places circle in third corner
		circles[2].setBounds(20, 370, 10, 10);
		for(int x = 20; x <= 30; x++){for(int y = 370; y <= 380; y++){pixels[x][y] = 2;}}
		// Places circle in forth corner
		circles[3].setBounds(420, 370, 10, 10);
		for(int x = 420; x <= 430; x++){for(int y = 370; y <= 380; y++){pixels[x][y] = 2;}}
		// Displays the labels
		for(int x = 0; x <= 3; x++){layers.add(circles[x], 1);}
		
		// Finalizes and displays the window
		window.setContentPane(layers);
		window.setSize(460, 585);
		window.setResizable(false);
		window.setVisible(true);
	}
	
	public void displayBlock(String name, int num, int x, int y, int width, int height, int layer){
		// Reads block image from file and displays it
		try {blockImg[num] = ImageIO.read(new File("Resources/"+name+".png"));} catch (IOException e) {e.printStackTrace();}
		blocks[num] = new JLabel(new ImageIcon(blockImg[num]));
		blocks[num].setBounds(x, y, width, height);
		layers.add(blocks[num], layer);
	}
	
	public void displaySquares(int start, int end, int position, String xOrY){
		// Creates and displays squares in specified position
		int current = start;
		do{
			if(current == start){current = current + 13;}
			else{current = current + 16;}
			
			squares[num] = new JLabel(new ImageIcon(squareImg));
			if(xOrY == "X"){
				squares[num].setBounds(current, position, 5, 5);
				x[num] = current;
				y[num] = position;
				for(int x = current; x <= current + 4; x++){for(int y = position; y <= position + 4; y++){pixels[x][y] = 1;}}
			}
			else{
				squares[num].setBounds(position, current, 5, 5);
				x[num] = position;
				y[num] = current;
				for(int x = position; x <= position + 4; x++){for(int y = current; y <= current + 4; y++){pixels[x][y] = 1;}}
			}
			layers.add(squares[num], 0);
			
			num++;
		}while(current + 21 < end);
	}
	
	public static void movePacMan(){
		// Changes the direction of the pacman
		if(currentDirection != tentativeDirection && xPos > 9 && xPos < 412){
			if(tentativeDirection == 1 && pixels[xPos - 3][yPos + 2] != 3 && pixels[xPos - 3][yPos + 15] != 3 && pixels[xPos - 3][yPos + 28] != 3){currentDirection = 1;}
			else if(tentativeDirection == 2 && pixels[xPos + 2][yPos - 3] != 3 && pixels[xPos + 15][yPos - 3] != 3 && pixels[xPos + 28][yPos - 3] != 3){currentDirection = 2;}
			else if(tentativeDirection == 3 && pixels[xPos + 33][yPos + 2] != 3 && pixels[xPos + 33][yPos + 15] != 3 && pixels[xPos + 33][yPos + 28] != 3){currentDirection = 3;}
			else if(tentativeDirection == 4 && pixels[xPos + 2][yPos + 33] != 3 && pixels[xPos + 15][yPos + 33] != 3 && pixels[xPos + 28][yPos + 33] != 3){currentDirection = 4;}
		}
		
		boolean moved = false;
		// Checks if possible to move in current direction, and moves if it is
		if(currentDirection == 1 && (xPos <= 0 || xPos >= 449 || pixels[xPos - 1][yPos + 15] != 3) ){
			if(xPos == -30){xPos = 479;}	// Moves pacman from left side to right
			xPos--;							// Moves left
			moved = true;
		}
		else if(currentDirection == 2 && (xPos <= 0 || xPos >= 449 || pixels[xPos + 15][yPos - 1] != 3) ){
			yPos--;							// Moves up
			moved = true;
		}
		else if(currentDirection == 3 && (xPos <= 0 || xPos >= 418 || pixels[xPos + 31][yPos + 15] != 3) ){
			if(xPos == 449){xPos = -30;}	// Moves pacman from right side to left
			xPos++;							// Moves right
			moved = true;
		}
		else if(currentDirection == 4 && (xPos <= 0 || xPos >= 449 || pixels[xPos + 15][yPos + 31] != 3) ){
			yPos++;							// Moves down
			moved = true;
		}
		
		// Checks if square is being collected
		if( (xPos > 9 && xPos < 430) && (pixels[xPos + 15][yPos + 15] == 1 || pixels[xPos + 15 + 3][yPos + 15] == 1 || pixels[xPos + 15 - 3][yPos + 15] == 1) ){
			int e = 0;
			boolean found = false;
			
			// Finds the square that is being collected based on coordinates
			do{
				if( ((xPos + 15) - x[e] > -8 && (xPos + 15) - x[e] < 8) && ((yPos + 15) - y[e] > -8 && (yPos + 15) - y[e] < 8) ){found = true;}
				else{e++;}
			}while(found == false && e < 240);
			
			if(found == true){
				// Makes square not visible and changes integers for pixels back to 0
				squares[e].setVisible(false);
				for(int xVal = x[e]; xVal <= x[e] + 4; xVal++){for(int yVal = y[e] - 4; yVal <= y[e] + 4; yVal++){pixels[xVal][yVal] = 0;}}
				
				// Adds to and displays score
				score = score + 10;
				scoreLbl.setText(Integer.toString(score));
				
				// Counts number of squares collected and ends game if all have been collected
				collected++;
				if(collected == 240){winning();}
			}
		}
		
		// Checks if special circle is being collected
		if( (xPos > 9 && xPos < 430) && (pixels[xPos + 15][yPos + 15] == 2) ){
			// Makes the appropriate circle not visible
			if(xPos < 200 && yPos < 200){for(int x = 20; x <= 30; x++){
				for(int y = 50; y <= 60; y++){pixels[x][y] = 0;}}
				circles[0].setVisible(false);
			}
			else if(xPos > 200 && yPos < 200){
				for(int x = 420; x <= 430; x++){for(int y = 50; y <= 60; y++){pixels[x][y] = 0;}}
				circles[1].setVisible(false);
			}
			else if(xPos < 200 && yPos > 200){
				for(int x = 20; x <= 30; x++){for(int y = 370; y <= 380; y++){pixels[x][y] = 0;}}
				circles[2].setVisible(false);
			}
			else if(xPos > 200 && yPos > 200){
				for(int x = 420; x <= 430; x++){for(int y = 370; y <= 380; y++){pixels[x][y] = 0;}}
				circles[3].setVisible(false);
			}
			
			// Makes the ghosts run away
			one.runAwayGhosts = true;
			two.runAwayGhosts = true;
			three.runAwayGhosts = true;
			four.runAwayGhosts = true;
			
			// Returns ghosts back to normal after 10 seconds
			Timer runAway = new Timer();
			runAway.schedule(new TimerTask(){public void run(){
				ghostsEaten = 0;
				one.runAwayGhosts = false;
				two.runAwayGhosts = false;
				three.runAwayGhosts = false;
				four.runAwayGhosts = false;
			}}, 10000);
		}
		
		if(moved == true){
			// Makes mouth open or close after moving 10 pixels
			if(cycle == 19){cycle = 0;}
			else{cycle++;}
			
			if(cycle == 0){pacMan.setIcon(new ImageIcon(pacManImg[currentDirection]));}
			else if(cycle == 9){pacMan.setIcon(new ImageIcon(pacManImg[0]));}
			
			// Displays pacman in new location
			pacMan.setBounds(xPos, yPos, 30, 30);
		}
		
		// Cancels last timer to prevent too many timers from running at once
		if(firstTimer == true){firstTimer = false;}
		else{move.cancel();}
		
		// Creates new timer and recursively calls this method
		if(stop == false){
			move = new Timer();										// Creates the timer
			move.schedule(new TimerTask() {public void run(){movePacMan();}}, 10);
		}
	}
	
	public static void main(String[] args){
		// Makes all the integers representing pixels 0
		for(int x = 0; x <= 449; x++){for(int y = 0; y <= 499; y++){pixels[x][y] = 0;}}
		
		JFrame.setDefaultLookAndFeelDecorated(true);				// Gives the GUI Java characteristics
		new Game();													// Runs the class
		
		// Reads the pacman images
		for(int x = 0; x <= 4; x++){try {pacManImg[x] = ImageIO.read(new File("Resources/Pacman"+x+".png"));} catch (IOException e) {e.printStackTrace();}}
		
		// Creates and places a label with the pacman image
		pacMan  = new JLabel(new ImageIcon(pacManImg[currentDirection]));
		pacMan.setBounds(xPos, yPos, 30, 30);
		layers.add(pacMan, 1);
		
		// Creates and places pictures representing remaining lives
		for(int l = 0; l <= lives - 1; l++){
			live[l] = new JLabel(new ImageIcon(pacManImg[3]));
			if(l == 0){live[l].setBounds(10, 510, 30, 30);}
			else if(l == 1){live[l].setBounds(50, 510, 30, 30);}
			else if(l == 2){live[l].setBounds(90, 510, 30, 30);}
			layers.add(live[l], 0);
		}
		
		// Creates and places a label for displaying score
		scoreLbl = new JLabel("0");
		scoreLbl.setFont(new Font("", Font.BOLD, 20));
		scoreLbl.setHorizontalAlignment(JLabel.RIGHT);
		scoreLbl.setBounds(200, 510, 240, 30);
		scoreLbl.setForeground(Color.WHITE);
		layers.add(scoreLbl, 0);
		
		// Calls the method that moves the pacman
		movePacMan();
		
		// Displays the ghosts
		one.display();
		two.display();
		three.display();
		four.display();
		
		// Makes the ghosts start jumping
		one.startJumping();
		two.startJumping();
		three.startJumping();
		four.startJumping();
		
		// Makes the first ghost start moving
		one.exit(0);
		
		// Makes the next ghost start moving
		Timer wait = new Timer();
		wait.schedule(new TimerTask(){public void run(){three.exit(0);}}, 5000);
		
		// Makes the next ghost start moving
		Timer wait2 = new Timer();
		wait2.schedule(new TimerTask(){public void run(){two.exit(0);}}, 15000);
		
		// Makes the next ghost start moving
		Timer wait3 = new Timer();
		wait3.schedule(new TimerTask(){public void run(){four.exit(0);}}, 25000);
	}

	public static void winning(){
		// Stops the pacman
		stop = true;
		// Stops the ghosts
		if(one.onTheMove == true){one.movement.cancel();}
		if(two.onTheMove == true){two.movement.cancel();}
		if(three.onTheMove == true){three.movement.cancel();}
		if(four.onTheMove == true){four.movement.cancel();}
		// Displays high scores
		new Scores();
	}
	
	public static void collision(){
		// Stops the ghosts
		if(one.onTheMove == true){one.movement.cancel();}
		if(two.onTheMove == true){two.movement.cancel();}
		if(three.onTheMove == true){three.movement.cancel();}
		if(four.onTheMove == true){four.movement.cancel();}
		// Stops the pacman
		stop = true;
		
		// Takes away a life and ends the game if none are left
		lives--;
		if(lives == 2){live[2].setVisible(false);}
		else if(lives == 1){live[1].setVisible(false);}
		else if(lives == 0){
			live[0].setVisible(false);
			new Scores();
		}
		
		// Returns the ghosts to their original locations
		Timer pause = new Timer();
		pause.schedule(new TimerTask(){public void run(){
			if(one.onTheMove == true){one.movement.cancel();}
			if(two.onTheMove == true){two.movement.cancel();}
			if(three.onTheMove == true){three.movement.cancel();}
			if(four.onTheMove == true){four.movement.cancel();}
			
			if(one.onTheMove == true){
				one.onTheMove = false;
				one.currentX = 211;
				one.currentY = 170;
				one.ghost.setBounds(one.currentX, one.currentY, 30, 30);
				one.exit(1000);
			}
			if(two.onTheMove == true){
				two.onTheMove = false;
				two.currentX = 179;
				two.currentY = 222;
				two.ghost.setBounds(two.currentX, two.currentY, 30, 30);
				two.exit(3000);
			}
			if(three.onTheMove == true){
				three.onTheMove = false;
				three.currentX = 211;
				three.currentY = 222;
				three.ghost.setBounds(three.currentX, three.currentY, 30, 30);
				three.exit(2000);
			}
			if(four.onTheMove == true){
				four.onTheMove = false;
				four.currentX = 243;
				four.currentY = 222;
				four.ghost.setBounds(four.currentX, four.currentY, 30, 30);
				four.exit(4000);
			}
			
			xPos = 210;
			yPos = 360;
			stop = false;
			pacMan.setBounds(xPos, yPos, 30, 30);
			movePacMan();
		}}, 4000);	
	}
	
	public void keyPressed(KeyEvent e) {
		// Begins the process of changing the pacman's direction
		if(e.getKeyCode() == 37){tentativeDirection = 1;}
		else if(e.getKeyCode() == 38){tentativeDirection = 2;}
		else if(e.getKeyCode() == 39){tentativeDirection = 3;}
		else if(e.getKeyCode() == 40){tentativeDirection = 4;}
	}

	public void keyReleased(KeyEvent arg0) {}	// Not used
	public void keyTyped(KeyEvent arg0) {}		// Not used
}
