package com.landenbrewer.blackjack;

import java.util.Random;

/**
 * The deck class represents a deck of 52 cards or more.
 * Allows for drawing cards and shuffling
 * @author Landen Brewer
 */
public class Deck {
	//SUITS array representing all the suits a card can have
	public final String[] SUITS = {"diamonds", "hearts", "spades", "clubs"};
	//RANKS array representing all the ranks a card can have
	public final String[] RANKS = {"a", "2", "3", "4", "5", "6", "7", "8", "9", "10", "j", "q", "k"};

	//Instance variable representing the number of decks in the whole deck
	public int deckCount;

	//Instance variable for the deck
	public Card[] deck;


	/**
	 * Constructor for the Deck. Initializing, and shuffling the deck
	 * @param deckCount
	 */
	public Deck(int deckCount) {
		this.deckCount = deckCount;
		this.deck = new Card[52 * this.deckCount];
		initialize();
		shuffle();
	}
	public Deck() {
		this(1);
	}

	/**
	 * The initialize function creates a deck of 52 cards based on the SUITS and RANKS arrays
	 */
	public void initialize() {
		//Create temporary deck that has a length of 52 cards * the amount of decks
		Card[] tempDeck = new Card[52 * this.deckCount];

		//counter variable that keeps track of the index in the deck.
		int iDeck = 0;
		//For each of the amount of decks chosen, create a deck
		for(int i = 0; i < deckCount; i++) {
			//For each suit
			for(String suit : SUITS) {
				//For each rank
				for(String rank : RANKS) {
					//Create a new card object with the suit and the rank
					tempDeck[iDeck] = new Card(rank, suit);
					//Increment the deck index counter
					iDeck++;
				}
			}
		}

		//Set the deck instance variable equal to the temporary deck
		this.deck = tempDeck;
	}

	/**
	 * Shuffle shuffles the deck unpredictably using the fisher-yates algorithm for shuffling
	 * arrays
	 */
	public void shuffle() {
		//create a random object
		Random r = new Random();
		//For each index of the deck instance variable
		for(int i = 0; i < this.deck.length; i++) {
			//Get a random index from 0 to i
			int rIndex = r.nextInt(i + 1);

			//Switch the cards at the current index and the random index
			Card temp = this.deck[i];
			this.deck[i] = this.deck[rIndex];
			this.deck[rIndex] = temp;
		}
	}

	/**
	 * The draw method allows for drawing the top card off of the deck of cards.
	 * @return Card
	 */
	public Card draw() {
		//If the deck runs out of cards, re-initialize and shuffle the deck.
		if(this.deck.length - 1 < 0) {
			initialize();
			shuffle();
		}
		//Let the card equal the top card.
		Card card = this.deck[this.deck.length - 1];

		//Create a new temporary card deck with length of one less than that of the deck instance variable
		Card[] tempDeck = new Card[this.deck.length - 1];

		//Copy the deck over to the temporary deck
		for(int i = 0; i < tempDeck.length; i++) {
			tempDeck[i] = this.deck[i];
		}

		//Set the deck instance variable equal to the temporary deck
		this.deck = tempDeck;

		//return the card that was drawn.
		return card;
	}

	/**
	 * The card class represents a single card with a rank, a suit, and an image.
	 * @author Landen Brewer
	 */
	public class Card {
		//Instance variables
		private String rank;
		private String suit;
		private String img;

		//The value of the card.
		private int value;

		/**
		 * The constructor for a card requiring a rank i.e. Ace, Jack, 4; and a suit, i.e. Diamonds, Hearts
		 * @param rank
		 * @param suit
		 */
		public Card(String rank, String suit) {
			this.rank = rank;
			this.suit = suit;

			//If the card is an ace, the img is held at the first character of the suit plus a 1.
			//Else it's just the first character of the suit plus the first character of the rank.
			this.img = (rank == "a") ? suit.charAt(0) + "1.png" : suit.charAt(0) + "" + rank + ".png";

			//Call the determineValue method
			this.value = determineValue();
		}

		/**
		 * Method to determine the numeric value of a given card based on it's rank and suit
		 * @return primitive int
		 */
		private int determineValue() {

			//If the rank is an ace, king, queen, or jack, then the value is going to be either a 10 or 11
			if(this.rank.equals("a")  || this.rank.equals("k") || this.rank.equals("q") || this.rank.equals("j")) {
				//If the rank is an ace the value is 11
				if(this.rank.equals("a")) {
					return 11;
				}
				//If the value is a jack, queen, or king the value is a 10
				return 10;
			}
			//Else the value is going to be the literal card rank
			return Integer.parseInt(this.rank);
		}

		//Getter methods
		public String getImg() {
			return img;
		}

		public String getRank() {
			return rank;
		}

		public String getSuit() {
			return suit;
		}

		public int getValue() {
			return value;
		}

		//toString method of the card.
		public String toString() {
			return this.rank + " of " + this.suit;
		}
	}

	//Getter method
	public Card[] getDeck() {
		return this.deck;
	}

	@Override
	//toString method of the deck
	public String toString() {
		return "Deck of " + this.deckCount + " decks";
	}

	//Main testing function
	public static void main(String[] args) {
		Deck deck = new Deck();
		System.out.println(deck);
		deck.draw();
	}
}
