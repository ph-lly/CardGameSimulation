/*
 * Created on: Mar 28, 2024
 * 
 * ULID: pbnguye
 * Class: IT 179
 */
package ilstu.edu;

import java.util.ArrayList;
import java.util.Stack;

import ilstu.edu.Game.Card;

/**
 * Player class
 * @author Phillip
 *
 */
public class Player{

	private String name;
	private ArrayList<Card> cards;
	
	/**
	 * Player constructor
	 * @param name - name to be used
	 */
	public Player(String name) {
		this.name = name;
		this.cards = new ArrayList<Card>();
		for(int i = 0; i < 4; i++) {
			cards.add(Game.faceDownCards.remove(0));
		}
	}
	/**
	 * getName method that returns the name
	 * @return - String
	 */
	public String getName() {
		return this.name;
	}
	/**
	 * getCards method that returns the hand
	 * @return - ArrayList<Card>
	 */
	public ArrayList<Card> getCards(){
		return this.cards;
	}
	/**
	 * addCard method that adds a card to the hand
	 * @param card - card to be used
	 */
	public void addCard(Card card) {
		cards.add(card);
	}
	
	/**
	 * method that will take from the stack or floor (only floor if it makes the player win)
	 */
	public void stackOrFloor() {
		if(Game.faceUpCards != null) {
			this.addCard(Game.faceDownCards.pop());
		}
	}
	
	/**
	 * method that will take from the floor or stack (if floor is losing) and determines if the player is winning or not
	 */
	public boolean downstackOrFloor() {
		Card card = Game.faceUpCards.peek();
		this.addCard(card); //"draw" from floor
		if(!this.isWinner()) {
			this.discard(Game.faceUpCards.peek()); //looked and none make win, so draw from stack
			this.addCard(Game.faceDownCards.pop()); //draw
			System.out.println(this.getName() + " drew " + this.getCards().get(4) + " from the deck.");
			return false;
		}
		else {
			return true; //winning
		}
	}

	/**
	 * throwCard method that will return the largest non-series card
	 * 
	 */
	public void throwCard() {
		ArrayList<Card> highestCard = new ArrayList<Card>();
		int numHearts = 0, numDiamonds = 0, numSpades = 0, numClubs = 0;
		for(int i = 0; i < cards.size(); i++) {
			if(cards.get(i).getSuit().equals("hearts")) {
				numHearts++;
			}
			else if(cards.get(i).getSuit().equals("diamonds")) {
				numDiamonds++;
			}
			else if(cards.get(i).getSuit().equals("spades")) {
				numSpades++;
			}
			else if(cards.get(i).getSuit().equals("clubs")) {
				numClubs++;
			}
		}
		
		Card hearts = new Card(numHearts, "hearts");
		Card diamonds = new Card(numDiamonds, "diamonds");
		Card spades = new Card(numSpades, "spades");
		Card clubs = new Card(numClubs, "clubs");
		
		ArrayList<Card> suitsCount = new ArrayList<Card>(); 
		suitsCount.add(hearts);
		suitsCount.add(diamonds);
		suitsCount.add(spades);
		suitsCount.add(clubs);
		boolean flag = false;
		if(numHearts == 3 || numDiamonds == 3 || numSpades == 3 || numClubs == 3) {
			flag = true;
		}
		for(int i = 0; i < suitsCount.size();i++) {
			if(suitsCount.get(i).getNum() == 3 ) { //no matter what if there are more than 2 of the same card we don't want to discard
				suitsCount.remove(i); //removes it from the list so we don't consider that suit when removing the card
			}
			else if(suitsCount.get(i).getNum() == 2 && flag == false) { //no 3s
				suitsCount.remove(i);
			}
		}
		
		//look at hand and find cards that match the suits not removed from suit arraylist
		for(int i = 0; i < suitsCount.size(); i++) {
			for(int j = 0; j < cards.size(); j++) {
				if(suitsCount.get(i).getSuit().equals(cards.get(j).getSuit())) {
					highestCard.add(cards.remove(j));
				}
			}
		}
		
		//then find the highest number card with least occurence out of the list
		if(!highestCard.isEmpty()) {
			Card largest = highestCard.get(0); //get from hand
			for(Card card : highestCard) {
				if(card.getNum() > largest.getNum()) {
					largest = card;
				}
			}
			Game.faceUpCards.add(discard(largest));
			highestCard.remove(largest);
			System.out.println(this.name + " throws " + largest.toString() + " to the floor.");
			System.out.println("---------------END-OF-TURN---------------");
		}
		else {
			//Game.faceUpCards.add(discard(cards.get(4))); //remove newest card
			
		}
		for(int i = 0; i < highestCard.size(); i++) {
			cards.add(highestCard.get(i));
		}
	}
	
	/**
	 * discard method that discards a card
	 * @return - Card
	 */
	public Card discard(Card card) {
		Card removed = card;
		cards.remove(card);
		return removed;
	}
	
	/**
	 * winner method that returns true if the player has won
	 * @return - boolean
	 */
	public boolean isWinner() {
		if(matchingFour()) {
			System.out.println(this.name + " matched all 4 cards!");
			return true;
		}
		if(addTo23()) {
			System.out.println(this.name + " has 4 cards that add up to 23!");
			return true;
		}
		return false;
	}
	/**
	 * matchingFour method that checks if any of the player's hands match
	 * @return - boolean
	 */
	private boolean matchingFour() {
		Stack<Card> temp = new Stack<Card>();
		Card newCard = cards.get(4);
		temp.push(cards.remove(4));
		int count = 0;
		for(int i = 0; i < 5; i++) {
			String suit = cards.get(0).getSuit();
			for(Card card: this.cards) {
				if(card.getSuit().equals(suit)) {
					count++;
				}
			}
			if(count == 4) {
				if(i > 0) {
				System.out.println(this.name + " swapped " + temp.peek() + " with " + newCard.toString()+ ".");
				}
				Game.faceUpCards.add(temp.pop());
				return true;
			}
			count = 0;
			cards.add(temp.pop());
			temp.push(cards.remove(0));
		}
		cards.add(temp.pop()); // new card back
		return false;
	}
	/**
	 * addTo23 method that returns a boolean if any of the cards add up to 23
	 * @return
	 */
	private boolean addTo23() {
		Stack<Card> temp = new Stack<Card>();
		Card newCard = cards.get(4);
		int total = 0;
		temp.push(cards.remove(4)); //put new card on tempstack
		for(int i = 0; i < 5; i++) {
			for(Card card: this.cards) {
				total+=card.getNum();
			}
			if(total == 23) {
				if(i > 0) {
				System.out.println(this.name + " swapped " + temp.peek() + " with " + newCard.toString()+ ".");
				}
				Game.faceUpCards.add(temp.pop());
				return true;
			}
			total=0;
			cards.add(temp.pop());
			temp.push(cards.remove(0));
		}
		cards.add(temp.pop()); //new card back
		return false;
	}
	/**
	 * print method for hand
	 */
	public void showHand() {
		System.out.print(this.name + "'s hand: ");
		for(int i = 0; i < cards.size();i++ ) {
			System.out.print(cards.get(i).toString());
		}
		System.out.println();
	}
	
}
