/*
 * Created on: Mar 28, 2024
 * 
 * ULID: pbnguye
 * Class: IT 179
 */
package ilstu.edu;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Main class
 * @author Phillip
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		System.out.println("Welcome to Match 4 or Add 23 Card Game!"); //is this a real card game idk
		int numPlayers = 1;	
		String input = "";

		while(numPlayers < 3 || numPlayers > 6){
			try {
				System.out.print("Enter the number of players: (3-6) ");
				numPlayers = scan.nextInt();
				scan.nextLine(); //consume.
				if(numPlayers < 3 || numPlayers > 6) {
					System.out.println("\nEnter a number between 3 and 6.");
				}
			}
			catch(NumberFormatException nfe) {
				System.out.println("Please enter a valid integer.");
			}
			catch(InputMismatchException ime) {
				System.out.println("Please enter a valid input.");
			}
			catch(IllegalArgumentException i) {
				System.out.println(i.getMessage());
			}
		}
		String[] playerNames = new String[numPlayers];
		
		for(int i = 0; i < numPlayers; i++) {
			System.out.print("Enter Player " + (i+1) + "'s name: ");
			input = scan.nextLine();
			playerNames[i] = input;
		}
		
		System.out.println("\n                GAME START                ");
		Game game = new Game(playerNames);
		game.simulate();
		scan.close();
	}
}
