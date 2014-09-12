package application;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.util.ArrayList;

public class Ghost {
	// Declaration of images and label
	BufferedImage ghostImg[][] = new BufferedImage[5][3];
	BufferedImage alternateImg[][] = new BufferedImage[5][3];
	JLabel ghost;
	// Declaration of timers
	Timer jump;
	Timer movement;
	Timer horizontal;
	Timer vertical;
	// Declaration of integers
	int currentX;
	int currentY;
	int origX;
	int origY;
	int bottom;
	int ceiling;
	int direction = 1;			// 1 = up, 0 = down
	int space;
	int currentDirection = 0;	// 1 = left, 2 = up, 3 = right, 4 = down
	int cycle = -1;
	int smartness;
	// Declaration of booleans
	boolean beginningOfGame = true;
	boolean onTheMove = false;
	boolean runAwayGhosts = false;
	// Declaration of string
	String gColour;
	
	public Ghost(String colour, int x, int y, int move, int smart){
		// Transfers variables from other class
		currentX = x;
		currentY = y;
		origX = x;
		origY = y;
		bottom = y;
		ceiling = y - move;
		space = move;
		gColour = colour;
		smartness = smart;
		
		// Reads images for ghost colour
		for(int O = 1; O <= 4; O++){for(int N = 1; N <= 2; N++){try{ghostImg[O][N] = ImageIO.read(new File("Resources/"+gColour+"O"+O+"N"+N+".png"));} catch (IOException e) {e.printStackTrace();}}}
		
		// Reads images for fleeing ghosts away
		for(int O = 1; O <= 4; O++){for(int N = 1; N <= 2; N++){try{alternateImg[O][N] = ImageIO.read(new File("Resources/"+"Ghost"+"O"+O+"N"+N+".png"));} catch (IOException e) {e.printStackTrace();}}}
	}
	
	public void display(){
		// Displays the ghost
		ghost = new JLabel(new ImageIcon(ghostImg[1][1]));
		ghost.setBounds(currentX, currentY, 30, 30);
		Game.layers.add(ghost, 1);
	}
	
	public void startJumping(){
		// Creates a timer that moves the ghost up and down
		jump = new Timer();
		jump.scheduleAtFixedRate(new TimerTask(){public void run(){
			if(direction == 1 && currentY > ceiling){
				currentY--;
				ghost.setBounds(currentX, currentY, 30, 30);
			}
			else if(direction == 1 && currentY == ceiling){
				direction = 0;
				ghost.setIcon(new ImageIcon(ghostImg[4][1]));
			}
			else if(direction == 0 && currentY < bottom){
				currentY++;
				ghost.setBounds(currentX, currentY, 30, 30);
			}
			else if(direction == 0 && currentY == bottom){
				direction = 1;
				ghost.setIcon(new ImageIcon(ghostImg[2][1]));
			}
		}}, 0, 30);
	}
	
	public void exit(int wait){
		// Cancels jumping from beginning of game
		if(beginningOfGame == true){
			jump.cancel();
			beginningOfGame = false;
		}
		
		// Moves the ghost out of the "cage" in the center
		horizontal = new Timer();
		horizontal.scheduleAtFixedRate(new TimerTask(){public void run(){
			if(currentX < 211){currentX++;}
			else if(currentX > 211){currentX--;}
			ghost.setBounds(currentX, currentY, 30, 30);
			
			if(currentX == 211){
				horizontal.cancel();
				
				vertical = new Timer();
				vertical.scheduleAtFixedRate(new TimerTask(){public void run(){
					if(currentY != 170){
						currentY--;
						ghost.setBounds(currentX, currentY, 30, 30);
					}
					
					if(currentY == 170){
						vertical.cancel();
						onTheMove = true;
						
						movement = new Timer();										// Creates the timer
						movement.scheduleAtFixedRate(new TimerTask(){public void run(){move();}}, 0, 20);
					}
				}}, 0, 20);
			}
		}}, wait, 20);
	}
	
	public void move(){
		// Creates an array list for possible direction
		ArrayList<Integer> possibleDirections = new ArrayList<Integer>();
		
		// Checks for possible directions
		if(currentX - 1 > 9 && currentX - 1 < 412 && Game.pixels[currentX - 1][currentY + 2] != 3 && Game.pixels[currentX - 1][currentY + 15] != 3 && Game.pixels[currentX - 1][currentY + 28] != 3){possibleDirections.add(1);}
		if(Game.pixels[currentX + 2][currentY - 1] != 3 && Game.pixels[currentX + 15][currentY - 1] != 3 && Game.pixels[currentX + 28][currentY - 1] != 3){possibleDirections.add(2);}
		if(currentX + 1 > 9 && currentX + 1 < 412 && Game.pixels[currentX + 31][currentY + 2] != 3 && Game.pixels[currentX + 31][currentY + 15] != 3 && Game.pixels[currentX + 31][currentY + 28] != 3){possibleDirections.add(3);}
		if(Game.pixels[currentX + 2][currentY + 31] != 3 && Game.pixels[currentX + 15][currentY + 31] != 3 && Game.pixels[currentX + 28][currentY + 31] != 3){possibleDirections.add(4);}
		
		// Randomly selects a direction; certain directions are made more probable
		if(runAwayGhosts == false){
			if((currentDirection == 1 && possibleDirections.contains(1) == false) || (currentDirection == 2 && possibleDirections.contains(2) == false) || (currentDirection == 3 && possibleDirections.contains(3) == false) || (currentDirection == 4 && possibleDirections.contains(4) == false) || possibleDirections.size() > 2 || currentDirection == 0){
				if(possibleDirections.contains(1) && Game.xPos < currentX){for(int n = 1; n <= smartness; n++){possibleDirections.add(1);}}
				if(currentDirection == 1 && possibleDirections.contains(1)){for(int n = 1; n <= 10; n++){possibleDirections.add(1);}}
				
				if(possibleDirections.contains(2) && Game.yPos < currentY){for(int n = 1; n <= smartness; n++){possibleDirections.add(2);}}
				if(currentDirection == 2 && possibleDirections.contains(2)){for(int n = 1; n <= 10; n++){possibleDirections.add(2);}}
				
				if(possibleDirections.contains(3) && Game.xPos > currentX){for(int n = 1; n <= smartness; n++){possibleDirections.add(3);}}
				if(currentDirection == 3 && possibleDirections.contains(3)){for(int n = 1; n <= 10; n++){possibleDirections.add(3);}}
				
				if(possibleDirections.contains(4) && Game.yPos > currentY){for(int n = 1; n <= smartness; n++){possibleDirections.add(4);}}
				if(currentDirection == 4 && possibleDirections.contains(4)){for(int n = 1; n <= 10; n++){possibleDirections.add(4);}}
			
				if(possibleDirections.size() != 0){
					int element = 0;
					do{element = new Random().nextInt(possibleDirections.size());}while(element > possibleDirections.size() - 1);
					currentDirection = possibleDirections.get(element);
				}
			}
		}
		else if(runAwayGhosts == true){
			if((currentDirection == 1 && possibleDirections.contains(1) == false) || (currentDirection == 2 && possibleDirections.contains(2) == false) || (currentDirection == 3 && possibleDirections.contains(3) == false) || (currentDirection == 4 && possibleDirections.contains(4) == false) || possibleDirections.size() > 2 || currentDirection == 0){
				if(possibleDirections.contains(1) && Game.xPos > currentX){for(int n = 1; n <= 50; n++){possibleDirections.add(1);}}
				if(currentDirection == 1 && possibleDirections.contains(1)){for(int n = 1; n <= 2; n++){possibleDirections.add(1);}}
				
				if(possibleDirections.contains(2) && Game.yPos > currentY){for(int n = 1; n <= 50; n++){possibleDirections.add(2);}}
				if(currentDirection == 2 && possibleDirections.contains(2)){for(int n = 1; n <= 2; n++){possibleDirections.add(2);}}
				
				if(possibleDirections.contains(3) && Game.xPos < currentX){for(int n = 1; n <= 50; n++){possibleDirections.add(3);}}
				if(currentDirection == 3 && possibleDirections.contains(3)){for(int n = 1; n <= 2; n++){possibleDirections.add(3);}}
				
				if(possibleDirections.contains(4) && Game.yPos < currentY){for(int n = 1; n <= 50; n++){possibleDirections.add(4);}}
				if(currentDirection == 4 && possibleDirections.contains(4)){for(int n = 1; n <= 2; n++){possibleDirections.add(4);}}
			
				if(possibleDirections.size() != 0){
					int element = 0;
					do{element = new Random().nextInt(possibleDirections.size());}while(element > possibleDirections.size() - 1);
					currentDirection = possibleDirections.get(element);
				}
			}
		}
		
		// Changes the ghost's coordinates
		switch(currentDirection){
		case 1: currentX--;	break;
		case 2: currentY--;	break;
		case 3: currentX++;	break;
		case 4: currentY++;	break;
		}
		
		// Alternates between images every five pixels
		if(cycle == 9){cycle = 0;}
		else{cycle++;}
		
		// Sets the label to the appropriate image
		if(runAwayGhosts == false){
			if(cycle >= 0 && cycle <= 4){ghost.setIcon(new ImageIcon(ghostImg[currentDirection][1]));}
			else{ghost.setIcon(new ImageIcon(ghostImg[currentDirection][2]));}
		}
		else{
			if(cycle >= 0 && cycle <= 4){ghost.setIcon(new ImageIcon(alternateImg[currentDirection][1]));}
			else{ghost.setIcon(new ImageIcon(alternateImg[currentDirection][2]));}
		}
		
		// Checks for a collision and takes the appropriate action
		if(Game.xPos - currentX > -20 && Game.xPos - currentX < 20 && Game.yPos - currentY > -20 && Game.yPos - currentY < 20){
			if(runAwayGhosts == false){Game.collision();}
			else if(runAwayGhosts == true){
				movement.cancel();
				
				Game.ghostsEaten++;
				
				switch(Game.ghostsEaten){
				case 1: Game.score = Game.score + 100;	break;
				case 2: Game.score = Game.score + 200;	break;
				case 3: Game.score = Game.score + 400;	break;
				case 4: Game.score = Game.score + 800;	break;
				}
				
				Game.scoreLbl.setText(Integer.toString(Game.score));
				
				runAwayGhosts = false;
				ghost.setBounds(origX, origY, 30, 30);
				currentX = origX;
				currentY = origY;
				exit(1000);
			}
		}
		else{ghost.setBounds(currentX, currentY, 30, 30);}	
	}
}
