//imports the scanner
import java.util.Scanner; 
import java.lang.Math;

public class TicTacToe       //declares class 
{
	//REQUIREMENTS
	// REQ 1 Private 3x3 2D array for board state
	// REQ 2 Enum for status of game
	// REQ 3 Allow two human players
	// REQ 4 Each move must be to an empty square

	//Declare board
	//REQ 1
	static final int BOARD_SIZE = 3;
	static final char SYMBOL_X = 'X';
	static final char SYMBOL_O = 'O';
	static Scanner input = new Scanner(System.in);
	
	int boardSize = BOARD_SIZE;
	static private char[][] Board = new char[BOARD_SIZE][BOARD_SIZE];

	//REQ 2
	enum GAME_STATUS {
		WIN,
		DRAW,
		CONTINUE
	}

	/*
	 _______________________ 
	|       |       |       |
	|  0,0  |  1,0  |  2,0  |
	|_______|_______|_______|
	|       |       |       |
	|  0,1  |  1,1  |  2,1  |
	|_______|_______|_______|
	|       |       |       |
	|  0,2  |  1,2  |  2,2  |
	|_______|_______|_______|
	*/

	static int[] getMove(boolean isX){

		int xCoord;
		int yCoord;

		//Input X coord
		//Input Y coord
		if(isX){
			try{
				//If X's turn, ask for P1 coordinates
				System.out.print("Enter Player 1 X coordinates: ");
				xCoord = input.nextInt();
				System.out.print("Enter Player 1 Y coordinates: ");
				yCoord = input.nextInt();
			}catch(Exception e){
				// Make user choice invalid to force the loop to reiterate the question
				xCoord = 5;
				yCoord = 5;
			}
		}else{
			try{
				// If O's turn, ask for P2 coordinates
				System.out.print("Enter Player 2 X coordinates: ");
				xCoord = input.nextInt();
				System.out.print("Enter Player 2 Y coordinates: ");
				yCoord = input.nextInt();
			}catch(Exception e){
				// Make user choice invalid to force the loop to reiterate the question
				xCoord = 5;
				yCoord = 5;
			}
		}
		
		//Create pair using X,Y
		int[] userMove = new int[]{xCoord, yCoord};
		
		//Flush buffer
		input.nextLine();

		//Return pair
		return userMove;
	}

	static GAME_STATUS isGameOver(){

		// Init to CONTINUE as default state
		GAME_STATUS gameState = GAME_STATUS.CONTINUE;

		int score = 0;
		int[] position = new int[2];
		char positionChar;

		// For each row, check XXX or OOO
		for(int j = 0; j <= 2; j++){
			for(int k = 0; k <= 2; k++){
				position[1] = j;
				position[0] = k;

				positionChar = getBoard(position);

				if(positionChar == SYMBOL_X){
					score++;
				}else if(positionChar == SYMBOL_O){
					score--;
				}else{
					//Do nothing
				}

				if(3 == score){
					//X wins
					System.out.println("Player 1 wins!");
					gameState = GAME_STATUS.WIN;
					//Print the final board state
					printBoard();
					// Early return
					return gameState;
				}else if(-3 == score){
					//O wins
					System.out.println("Player 2 wins!");
					gameState = GAME_STATUS.WIN;
					//Print the final board state
					printBoard();
					// Early return
					return gameState;
				}else{
					//nothing
				}
			}
			// Reset score to count next row
			score = 0;
			position[1] = 0;
			position[0] = 0;
		}

		// For each column, check XXX or OOO
		for(int j = 0; j <= 2; j++){
			for(int k = 0; k <= 2; k++){
				position[0] = j;
				position[1] = k;

				positionChar = getBoard(position);

				if(positionChar == SYMBOL_X){
					score++;
				}else if(positionChar == SYMBOL_O){
					score--;
				}else{
					//Do nothing
				}

				if(3 == score){
					//X wins
					System.out.println("Player 1 wins!");
					gameState = GAME_STATUS.WIN;
					//Print the final board state
					printBoard();
					// Early return
					return gameState;
				}else if(-3 == score){
					//O wins
					System.out.println("Player 2 wins!");
					gameState = GAME_STATUS.WIN;
					//Print the final board state
					printBoard();
					// Early return
					return gameState;
				}else{
					//nothing
				}
			}
			// Reset score to count next row
			score = 0;
			position[1] = 0;
			position[0] = 0;
		}

		// Check diagonals for XXX or OOO
		position[0] = 0;
		position[1] = 0;

		positionChar = getBoard(position);
		if(SYMBOL_X == positionChar){
			score++;
		}else if(SYMBOL_O == positionChar){
			score--;
		}

		position[0] = 1;
		position[1] = 1;

		positionChar = getBoard(position);
		if(SYMBOL_X == positionChar){
			score++;
		}else if(SYMBOL_O == positionChar){
			score--;
		}

		position[0] = 2;
		position[1] = 2;

		positionChar = getBoard(position);
		if(SYMBOL_X == positionChar){
			score++;
		}else if(SYMBOL_O == positionChar){
			score--;
		}

		if(3 == score){
			//X wins
			System.out.println("Player 1 wins!");
			gameState = GAME_STATUS.WIN;
			//Print the final board state
			printBoard();
			// Early return
			return gameState;
		}else if(-3 == score){
			//O wins
			System.out.println("Player 2 wins!");
			gameState = GAME_STATUS.WIN;
			//Print the final board state
			printBoard();
			// Early return
			return gameState;
		}

		// Check diagonals for XXX or OOO
		position[0] = 2;
		position[1] = 0;
		score = 0;

		positionChar = getBoard(position);
		if(SYMBOL_X == positionChar){
			score++;
		}else if(SYMBOL_O == positionChar){
			score--;
		}

		position[0] = 1;
		position[1] = 1;

		positionChar = getBoard(position);
		if(SYMBOL_X == positionChar){
			score++;
		}else if(SYMBOL_O == positionChar){
			score--;
		}

		position[0] = 0;
		position[1] = 2;

		positionChar = getBoard(position);
		if(SYMBOL_X == positionChar){
			score++;
		}else if(SYMBOL_O == positionChar){
			score--;
		}

		if(3 == score){
			//X wins
			System.out.println("Player 1 wins!");
			gameState = GAME_STATUS.WIN;
			//Print the final board state
			printBoard();
			// Early return
			return gameState;
		}else if(-3 == score){
			//O wins
			System.out.println("Player 2 wins!");
			gameState = GAME_STATUS.WIN;
			//Print the final board state
			printBoard();
			// Early return
			return gameState;
		}
		
		return gameState;
	}
	
	static void pushMove(int[] userMove, boolean isX){

		// Take in coords
		// We don't need to check valid move, this func is only accessible when move is valid
		// Push symbol to coordinates
		char userSymbol;

		if(isX){
			userSymbol = SYMBOL_X;
		}else{
			userSymbol = SYMBOL_O;
		}

		setBoard(userMove, userSymbol);
	}

	static void printBoard(){
		//Print board state
		// Fill a temp board object to be used for displaying
		char[][] tempBoard = new char[3][3];
		int[] cords = new int[2];
		
		for(int y = 0; y <= 2; y++){
			for(int x = 0; x <= 2; x++){
				cords[0] = x;
				cords[1] = y;
				tempBoard[x][y] = getBoard(cords);
			}
		}

		System.out.printf(" _______________________\n");
		System.out.printf("|       |       |       |\n");
		System.out.printf("|   %c   |   %c   |   %c   |\n", getBoard(new int[]{0,0}), getBoard(new int[]{1,0}), getBoard(new int[]{2,0}));
		System.out.printf("|_______|_______|_______|\n");
		System.out.printf("|       |       |       |\n");
		System.out.printf("|   %c   |   %c   |   %c   |\n", getBoard(new int[]{0,1}), getBoard(new int[]{1,1}), getBoard(new int[]{2,1}));
		System.out.printf("|_______|_______|_______|\n");
		System.out.printf("|       |       |       |\n");
		System.out.printf("|   %c   |   %c   |   %c   |\n", getBoard(new int[]{0,2}), getBoard(new int[]{1,2}), getBoard(new int[]{2,2}));
		System.out.printf("|_______|_______|_______|\n");
	}

	static char getBoard(int[] coordinates){
		return Board[coordinates[0]][coordinates[1]];
	};

	static void setBoard(int[] coordinates, char userSymbol){
		Board[coordinates[0]][coordinates[1]] = userSymbol;
	};

	static boolean validMove(int[] userMove){
		// Check board state that user move was to empty spot
		// Use userMove
		// Invalid char 
		char value = '!';
		
		if(Math.abs(userMove[0]) < 3 && Math.abs(userMove[1]) < 3){
			if(userMove[0] >= 0 && userMove[1] >= 0){
				value = getBoard(userMove);
		}}
		
		System.out.println(value);

		if(value == ' '){
			return true;
		}else{
			return false;
		}
	}

	public static void main(String[] args){
		// Player 1 starts with X
		// X goes first
		// Initialize isX to true
		// REQ 3
		boolean isX = true;

		int moveCount = 0;

		// Initialize the board to be empty with spaces instead of null char
		int[] cords = new int[2];
		for(int j = 0; j <= 2; j++){
			for(int k = 0; k <= 2; k++){
				cords[0] = j;
				cords[1] = k;
				setBoard(cords, ' ');
			}
		}

		//Initialize userMove as null,null
		int[] userMove = new int[2];

		while(isGameOver() == GAME_STATUS.CONTINUE){


			// Display board state
			printBoard();

			if(moveCount > 8){
				System.out.println("It's a draw");
				break;
			}

			// Get FirstPlayerMove
			// X goes
			// X should be true here
			userMove = getMove(isX);

			// Check if player made valid move
			// REQ 4
			if(validMove(userMove)){

				// Invert isX
				// Next player goes on the next loop
				isX = !isX;

				moveCount++;
	
				// Modify board
				pushMove(userMove, isX);
			}
		}
	}
}// End Class TicTacToe

