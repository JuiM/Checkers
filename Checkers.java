package Checkers;

import Zen.* ;
/*
 * NEEDED TO CHANGE TO GRIDWORLD
 * ALSO HOW MANY CLASSES?
 * need a class that implements MosueListener
 * 
 * for a circle use a GIF
 * 
 * for the game, players take turns playing
 * 
 * get rid of all the Zen - setColor() methods already there
 * so that means create a getMouseClickX() and getMouseClickY()
 * using a mouse listener
 * 
 * get a checker to EXTEND ACTOR(OR SOMETHING) CLASS
 * 
 * use removeSelfFromGrid()
 *getLocation() used for the move() method
 *
 *HARD STUFF:
 *have you lost the game? (any active squares left... keep using isActive)
 *We're not using trriple jumps
 *or the fact that if you can jump over something, you have to move
 */

public class Checkers extends ZenGame{
	int[][] board = new int[10][10];
	int activeX;
	int activeY;
	int current = 1;

	public static void main(String[] args) {
		Checkers game = new Checkers();
		game.run();
	}

	@Override
	public void setup() {
		reset();
	}

	@Override
	public void loop() {
		drawBoard();
		drawActive();
		drawPieces();
		checkClick();
	}

	public void move(int x1, int y1, int x2, int y2){
		// ANIMATED!!!! :)
		int piece = board[x1][y1];
		board[x1][y1] = 0;
		Circle c = new Circle(40);
		c.setColor((piece == 1 ) ? "red" : "black");
		c.set(x1 * 50 +25,  y1 *50 +25);
		int y = y1 * 50 +25;
		for(int x = x1*50 +25; x !=x2* 50 +25; x= x+ ((x1<x2) ? 1 : -1)){
			y = y+((y1 < y2) ? 1 : -1);
			c.set(x,y);
			//Draw the board and buffer
			drawBoard();
			drawPieces();
			//use Zen.fillOval.. which uses top left corner instead of center
			Zen.draw(c);
			Zen.buffer(2);
		}
		board[x2][y2] = piece;
		drawBoard();
		drawPieces();
		Zen.buffer(2);
// if you single jumped over a piece, then take it over
		if(Math.abs(x2-x1) == 2){
			board[(x2+x1)/2][(y2 + y1) /2 ] =0;
		}
	}
	private void drawActive() {
		for(int x=0; x<10 ; x++){
			for(int y=0;y<10;y++){		
				if(isActive(x,y)){
					Zen.setColor("light gray");
					Zen.fillRect(x*50, y*50, 50, 50);
				}
			}
		}
	}

	public boolean isActive(int x, int y){
		//active means is you can jump to it 
		if(!isEmpty(x,y))
			return false;
		if(isEmpty(x,y) && isRed(activeX,activeY)){
			if(!isWhiteSquare(activeX, activeY)){
				return false;
			}
			// to make sure goes on the right turn
			if(board[activeX][activeY] != current){
				return false;
			}
			if(x + 1 == activeX && y+1 == activeY){
				return true;
			}
			//single jump
			if( x+2 == activeX && y+2 == activeY && isBlack(x+1, y+1)){
				return true;
			}
			if(x - 1 == activeX && y+1 == activeY){
				return true;
			}
			//single jump in other direction
			if(x - 2 == activeX && y+2 == activeY && isBlack(x-1,y+1)){
				return true;
			}
			else
				return false;
		}
		if(isEmpty(x,y) && isBlack(activeX,activeY)){
			if(!isWhiteSquare(activeX, activeY)){
				return false;
			}
			// to make sure goes on the right turn 
			if(board[activeX][activeY] != current){
				return false;
			}
			if(x - 1 == activeX && y-1 == activeY){
				return true;
			}
			//single jump 
			if(x-2 == activeX && y-2 == activeY && isRed(x-1,y-1)){
				return true;
			}
			if(x + 1 == activeX && y - 1 == activeY){
				return true;
			}
			//single jump in other direction
			if(x+2 == activeX && y-2 == activeY && isRed(x+1, y-1)){
				return true;
			}
			else
				return false;
		}
		return false;
	}

	private void checkClick() {
		int x = Zen.getMouseClickX()-1;
		int y = Zen.getMouseClickY()-1;
		if(x>= 0 && y >= 0){
			//actual position that you clicked on
			int newX = x / 50;
			int newY = y / 50;
			// if it is an active square, than move it
			if(isActive(newX, newY)){
				move(activeX, activeY, newX, newY);
				current = (current == 2) ? 1 : 2;
			}
			activeX = x / 50;
			activeY = y / 50 ; 
		}

	}

	private void drawPieces() {
		for(int x=0; x<10 ;x++){
			for(int y=0; y< 10; y++){
				if(isRed(x,y)){
					Zen.setColor("red");
					Zen.fillOval(x*50 +5, y*50 +5, 40, 40);
				}
				if(isBlack(x,y)){
					Zen.setColor("black");
					Zen.fillOval(x*50 +5, y*50 +5, 40, 40);
				}
			}
		}
	}
	private void reset(){
		board = new int[10][10];
		//placing the black pieces
		for(int x =0; x <10; x++){
			for(int y=0; y < 4; y++){

				if(isWhiteSquare(x,y)){
					board[x][y] =2;
				}

			}
		}
		// placing the white pieces
		for(int x =0; x <10; x++){
			for(int y=6;y<10; y++){
				if(isWhiteSquare(x,y)){
					board[x][y] =1;
				}

			}
		}
	}

	public boolean isEmpty(int x, int y){
		if(board[x][y] == 0){
			return true;
		}
		else
			return false;
	}
	public boolean isWhiteSquare(int x, int y){
		if(x%2 == y%2)
			return true;
		else
			return false;
	}

	public boolean isRed(int x, int y){
		return board[x][y] == 1;
	}
	public boolean isBlack(int x, int y){
		return board[x][y] == 2;
	}
	private void drawBoard() {
		Zen.setBackground("chocolate");
		Zen.setColor("tan");
		// Make the background
		for(int x =0; x< 10; x++){
			for(int y=0;y <10; y++){
				// Make the background of the square
				if(isWhiteSquare(x,y)){
					Zen.fillRect(x*50,y*50,50,50);
				}
			}
		}
	}

}
