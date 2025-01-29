/*
 * Created on: Mar 28, 2024
 * 
 * ULID: pbnguye
 * Class: IT 179
 */
package ilstu.edu;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

/**
 * Game class
 * @author Phillip
 *
 */
public class Game {

	private LinkedList<Card> unshuffledDeck;
	public static Stack<Card> faceDownCards;
	public static Stack<Card> faceUpCards;
	public Queue<Player> players;
	
	/**
	 * Card inner class
	 * @author Phillip
	 *
	 */
	public static class Card { //card data as a class
		private int num;
		private String suit;
		
		/**
		 * Card constructor
		 * @param num - num to be set
		 * @param suit - suit to be set
		 */
		public Card(int num, String suit) {
			this.num = num;
			this.suit = suit;
		}
		
		/**
		 * getter for card number
		 * @return - int
		 */
		public int getNum() {
			return num;
		}
		/**
		 * getter for suit
		 * @return - String
		 */
		public String getSuit() {
			return suit;
		}
		/**
		 * toString formatted as a card
		 */
		public String toString() {
			String suit = "";
			if(getSuit().equals("spades")) {
				suit = "♠";
			}
			else if(getSuit().equals("hearts")) {
				suit = "♥";
			}
			else if(getSuit().equals("clubs")) {
				suit = "♣";
			}
			else {
				suit = "♦";
			}
			return "[" + this.getNum() + suit + "]";
		}
	}
	/**
	 * Game constructor that takes a string array and initializes fields
	 * @param playerNames - playerNames to be used
	 */
	public Game(String[] playerNames) {
		unshuffledDeck = new LinkedList<Card>();
		faceDownCards = new Stack<Card>();
		faceUpCards = new Stack<Card>();
		players = new LinkedList<Player>();
		
		for(int i = 1; i <= 10; i++) { //make cards
			unshuffledDeck.add(new Card(i, "diamonds"));
			unshuffledDeck.add(new Card(i, "clubs"));
			unshuffledDeck.add(new Card(i, "hearts"));
			unshuffledDeck.add(new Card(i, "spades"));
		}
		shuffle(); //shuffle
		for(String name : playerNames) {
			players.add(new Player(name)); //add players to queue
		}
	}
	/**
	 * shuffle method that shuffles the unshuffled cards
	 */
	public void shuffle() {
		int size = unshuffledDeck.size();
		int card = 0;
		Random rand = new Random();
		while(unshuffledDeck != null && size != 0) {
			card = rand.nextInt(size); // "index" to remove because I'm using java's linkedlist
			faceDownCards.push(unshuffledDeck.remove(card));
			size--;
		}
	}
	/**
	 * simulation method that simulates the entire game.
	 */
	public void simulate() {
		boolean end = false;
		Player currPlayer = players.peek();
		boolean floor = false;
		while(!end) {
			System.out.println("=========================================");
			currPlayer.showHand();
			if(faceDownCards.empty()) {
				System.out.println("There are no more cards to draw. There are no winners!");
				end = true;
				break;
			}	
			if(faceUpCards.empty()) { //checks if any card on floor
				currPlayer.stackOrFloor(); //draw
				System.out.println(currPlayer.getName() + " drew " + currPlayer.getCards().get(4) + " from the deck.");
			}
			else if(currPlayer.downstackOrFloor()){
				floor = true; //short circuit I guess
			}
			
			if(floor || currPlayer.isWinner()) { 
				System.out.println(currPlayer.getName() + " is the winner!");
				System.out.println("Winning hand: " + currPlayer.getCards());
				System.out.println("=========================================");
				end = true;
				break;
			}
			else { //we want to throw the highest non matching card on the floor (upstack)
				currPlayer.throwCard();
			}
			currPlayer.showHand();
			System.out.print("Floor:" + faceUpCards.peek().toString()+ "\n"); 
			Player next = players.remove();
			players.add(next); //requeue
			currPlayer = players.peek();
		}	
	}
}

