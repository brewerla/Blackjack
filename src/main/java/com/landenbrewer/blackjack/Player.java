package com.landenbrewer.blackjack;

/**
 * The player object represents a single player and holds the hand of the player,
 * weather the player has gone over 21, and what the total their hand adds up to
 */
public class Player {
	//Instance variables
	//The hand is an array of cards initialized at a length of 0
	private Deck.Card[] hand = new Deck.Card[0];

	//Total hands won and lost
	private int handsWon = 0;
	private int handsLost = 0;

	//The total the hand of cards adds up to
	private int total = 0;
	//Weather the player has busted
	private boolean busted = false;
	//How many aces the player has to work with. This is needed because aces can count as either a 1 or 11
	private int usableAces = 0;

	public Player() {

	}

	/**
	 * Method to add a variable amount of cards to the hand of the player using the spread operator
	 * @param cards
	 */
	public void addCardsToHand(Deck.Card ...cards) {
		//For each of the cards passed as arguments
		for(Deck.Card card : cards) {

			//If the card is an ace add one to the usable aces variable
			if(card.getRank().equals("a")) {
				this.usableAces ++;
			}

			//Create a temporary hand that has the length of the current hand plus one
			Deck.Card[] tempHand = new Deck.Card[this.hand.length + 1];

			//Copy the current hand to the new temp hand
			for(int i = 0; i < this.hand.length; i++) {
				tempHand[i] = this.hand[i];
			}
			//Set the last, new index in the tempHand to the current card.
			tempHand[tempHand.length - 1] = card;

			//Set the current hand to the new tempHand
			this.hand = tempHand;

			//Add the value of the card to the total of the player
			this.total += card.getValue();

			//If the total is greater than 21
			if(this.total > 21) {

				//While the player has any aces that they can use, subtract 10 from the total to make one of those aces
				//be worth only one and subtract that usableAce
				while(this.total > 21 && usableAces > 0) {
					this.total -= 10;
					usableAces--;
				}
				//If after that the total is still greater than 21, the player has busted and the variable
				//is set to true.
				if(this.total > 21) {
					this.busted = true;
				}
			}
		}
	}

	//Add a single card to the deck
	public void addCardToHand(Deck.Card card) {
		this.addCardsToHand(card);
	}

	//Getters and setters .
	public boolean isBusted() {
		return busted;
	}
	public void isBusted(boolean isBusted) {
		this.busted = isBusted;
	}

	public int getTotal() {
		return total;
	}

	public Deck.Card[] getHand() {
		return hand;
	}

	public void addHandWon() {
		this.handsWon++;
	}
	public void addHandLost() {
		this.handsLost++;
	}
	public int getHandsWon() {
		return handsWon;
	}
	public int getHandsLost() {
		return handsLost;
	}

	/**
	 * Method to discard all the cards in the current hand
	 */
	public void discardHand() {
		this.hand = new Deck.Card[0];
		this.total = 0;
		this.usableAces = 0;
	}

	//Main testing method
	public static void main(String[] args) {
		Deck deck = new Deck();

		Player player = new Player();
		player.addCardsToHand(deck.draw(), deck.draw());

		System.out.println(player.getHand().length);
		System.out.println(player.getHand()[0]);

		System.out.println(player.getTotal());
	}
}
