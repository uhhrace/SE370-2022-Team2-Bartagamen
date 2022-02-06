
import java.util.Scanner; //imports the scanner

public class TicTacToe       //declares class 
{

	//REQUIREMENTS
	// REQ 1 Private 3x3 2D array for board state
	// REQ 2 Enum for status of game
	// REQ 3 Allow two human players
	// REQ 4 Each move must be to an empty square

	//Declare board
	//REQ 1
	private char[][] Board = new char[3][3];


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

	//REQ 2
	enum GAME_STATUS {
		WIN,
		DRAW,
		CONTINUE
	}

	// TODO
	Pair getMove(boolean isX){

		Scanner input = new Scanner(System.in);

		//TODO THIS IS JUST A DEMO
		//FLESH OUT WITH LOGIC LATER

		/*
		int number;
		char letter;
		String word;
		System.out.print("Enter a whole number: ");
		number = input.nextInt();
		System.out.printf("You entered the number %d%n", number);
		
		System.out.print("Enter a word: ");
		word = input.next();
		System.out.printf("You entered the word %s%n", word);
		
		System.out.print("Enter a single character: ");
		letter = input.next().charAt(0);
		System.out.printf("You entered the character %s%n", letter);
		
		
		String first, last;
		char initial;
		int age;
		
		System.out.print("Enter your first name: ");
		first = input.nextLine();
		System.out.print("Enter your middle initial: ");
		initial = input.next().charAt(0);
		input.nextLine();
		System.out.print("Enter your last name: ");
		last = input.nextLine();
		System.out.print("Enter your age: ");
		age = input.nextInt();
		System.out.printf("My name is %s %s %s, and I am %d years old",first,initial,last,age);
		*/

		// END DEMO LOGIC FOR INPUT/OUTPUT

		int xCoord = null;
		int yCoord = null;

		//Input X coord
		//Input Y coord
		switch (isX) {
			//If X's turn, ask for P1 coordinates
			case true:
			System.out.print("Enter Player 1 X coordinates: ");
			xCoord = input.nextInt();
			System.out.print("Enter Player 1 Y coordinates: ");
			yCoord = input.nextInt();			
				break;
		
			// If O's turn, ask for P2 coordinates
			case false:
			System.out.print("Enter Player 2 X coordinates: ");
			xCoord = input.nextInt();
			System.out.print("Enter Player 2 Y coordinates: ");
			yCoord = input.nextInt();
				break
			default:
				break;
		}
		//Create pair using X,Y
		Pair<Integer, Integer> userMove = new Pair<Integer, Integer>(xCoord, yCoord);
		//Return pair
		return userMove;
	}

	GAME_STATUS isGameOver(){

		// Init to CONTINUE as default state
		gameState = CONTINUE

		// For each row, check XXX or OOO
			// If true, gameState WIN
		// For each column, check XXX or OOO
			// If true, gameState WIN
		// Check diagonals for XXX or OOO
			// If true, gameState WIN
		//Print the winning player
		//Print the final board state
		printBoard();
		
		return gameState;
	}

	void printBoard(){
		//Print board state
	}

	boolean validMove(Pair userMove){
		// Check board state that user move was to empty spot

		// Check value of array at userMove coords
		// If null, return true
		// Else, return false
	}

	public static void main(String[] args){
		// Player 1 starts with X
		// X goes first
		// Initialize isX to true
		// REQ 3
		boolean isX = true;

		//Initialize userMove as null,null
		Pair<Integer, Integer> userMove = new Pair<Integer, Integer>(null, null);

		while(isGameOver() == CONTINUE){
			// Display board state
			printBoard();

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
	
				// Modify board
				pushMove(userMove);
			}
		}
	}
}// End Class TicTacToe

