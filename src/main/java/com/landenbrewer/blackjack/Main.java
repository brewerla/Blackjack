package com.landenbrewer.blackjack;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage stage) {
		//Create a new instance of a deck object.
		Deck deck = new Deck();

		//Create player objects for the dealer and the player
		Player player = new Player();
		player.addCardsToHand(deck.draw(), deck.draw());

		Player dealer = new Player();
		dealer.addCardsToHand(deck.draw(), deck.draw());

		//Create the overarching hbox containing the totals/results and the cards themselves
		HBox tlbox = new HBox(20);
		tlbox.setPadding(new Insets(30));
		tlbox.setAlignment(Pos.CENTER_LEFT);

		//Create a VBox and two HBox objects for the visual element of the player cards.
		VBox vbox = new VBox(10);
		HBox playerVisual = new HBox(5);
		HBox dealerVisual = new HBox(5);

		//Holds the player and the dealer totals
		VBox totalBox = new VBox();
		totalBox.setAlignment(Pos.CENTER_LEFT);

		//create a text object that displays the player total
		Text playerTotal = new Text("Player total is " + player.getTotal());

		//Create a text object that displayes the dealer total minus the value of the first card
		//Because we are not supposed to see that card until the player chooses to stay.
		Text dealerTotal = new Text("Dealer total is ? & " + (dealer.getTotal() - dealer.getHand()[0].getValue()));

		//Add the totals to the totalBox
		totalBox.getChildren().addAll(dealerTotal, playerTotal);

		//Add the totals box and the game to the overarching box
		tlbox.getChildren().addAll(totalBox, vbox);

		//Create stay button
		Button stay = new Button("Stay");

		//Create restart button
		Button restart = new Button("Restart");

		//Create a button that allows the player to hit.
		Button hit = new Button("Hit");

		//Create the box to hold the buttons
		HBox btnBox = new HBox(10);
		//Add all buttons except the restart button.
		btnBox.getChildren().addAll(hit, stay);

		//Go through each of the cards in the hands of the dealer and player and add them to the visual
		for(int i = 0; i < dealer.getHand().length; i++) {
			if(i == 0) {
				dealerVisual.getChildren().add(new ImageView(getPath("b1fv.png")));
			} else {
				dealerVisual.getChildren().add(new ImageView(getPath(dealer.getHand()[i].getImg())));
			}

		}
		for(Deck.Card card : player.getHand()) {
			playerVisual.getChildren().add(new ImageView(getPath(card.getImg())));
		}

		Text score = new Text(player.getHandsWon() + " - " + player.getHandsLost());

		//Add the dealer's visual element and the players visual element to the vbox
		vbox.getChildren().addAll(dealerVisual, playerVisual, btnBox, score);
		//Add the highest level hbox to the pane

		//Set up the scene
		Scene scene = new Scene(tlbox, 600, 300);
		stage.setTitle("Blackjack!");
		stage.setScene(scene);
		stage.show();



		/*
		 * Dealer rules
		 * If the total is greater than or equal to 17 the dealer must stand
		 *
		 * If the total is 16 or fewer the dealer must continue to
		 * take cards until the total is 17 or greater
		 *
		 * */
		//When the player chooses to stay then it is the dealers turn to draw cards.
		stay.setOnAction(e -> {
			//"flip over" the first card in the dealers hand
			dealerVisual.getChildren().set(0, new ImageView(getPath(dealer.getHand()[0].getImg())));
			//Change the text to accurately represent the dealers total.
			dealerTotal.setText("Dealer total is " + dealer.getTotal());

			//While the dealer's total is less than 17 continue to draw cards.
			while(dealer.getTotal() < 17) {
				//Draw a card
				Deck.Card drewCard = deck.draw();

				//Add that card to the dealer visual element
				dealerVisual.getChildren().add(new ImageView(getPath(drewCard.getImg())));
				//Add that card to the dealer's hand
				dealer.addCardToHand(drewCard);
				//Change the text to represent the new dealer total
				dealerTotal.setText("Dealer total is " + dealer.getTotal());
			}

			//Check who won and do something based on that.
			System.out.println(dealer.isBusted());
			if(dealer.getTotal() > player.getTotal() && !dealer.isBusted()) {


				//Add a new text node that shows that the dealer won.
				totalBox.getChildren().add(new Text("Dealer wins!"));
				player.addHandLost();

			} else if(dealer.getTotal() == player.getTotal()) {
				//If the totals are equal it's a draw.
				totalBox.getChildren().add(new Text("It's a draw!"));
			} else {
				//Weather the dealer is busted or not we can make sure the dealer is no longer busted.
				dealer.isBusted(false);

				//Add a new text node that shows that the player won.
				totalBox.getChildren().add(new Text("Player wins!"));
				player.addHandWon();
			}

			//Disable the hit and stay button and add the reset button that resets and draws more cards.
			stay.setDisable(true);
			hit.setDisable(true);
			btnBox.getChildren().add(restart);

			//set the score text
			score.setText(player.getHandsWon() + " - " + player.getHandsLost());
		});

		//If the player presses hit, draw a card and see if they busted
		hit.setOnAction(e -> {
			//Draw a card and add it to the players hand
			Deck.Card card = deck.draw();
			player.addCardToHand(card);

			//Change the player total
			playerTotal.setText("Player total is " + player.getTotal());

			//Add the card to the player visual element.
			playerVisual.getChildren().add(new ImageView(getPath(card.getImg())));

			//Check if the player went over 21.
			if(player.isBusted()) {
				//If they did the dealer wins
				totalBox.getChildren().add(new Text("Dealer wins!"));
				//Disable the play buttons and add the restart button.
				stay.setDisable(true);
				hit.setDisable(true);
				//Change the player to no longer be busted as the game has reset.
				player.isBusted(false);
				btnBox.getChildren().add(restart);

				player.addHandLost();
				score.setText(player.getHandsWon() + " - " + player.getHandsLost());
			}
		});

		restart.setOnAction(e -> {
			//Use the discardHand method to remove all cards from the hand and set the totals back to 0
			player.discardHand();
			dealer.discardHand();

			//Draw two more cards to each player's hand
			dealer.addCardsToHand(deck.draw(), deck.draw());
			player.addCardsToHand(deck.draw(), deck.draw());

			//Clear out the winner statement from the totalBox
			totalBox.getChildren().clear();

			//Re-add the totals to the totalBox
			totalBox.getChildren().addAll(dealerTotal, playerTotal);

			//Set the text to represent the totals of the player and dealer.
			playerTotal.setText("Player total is " + player.getTotal());
			dealerTotal.setText("Dealer total is ? & " + (dealer.getTotal() - dealer.getHand()[0].getValue()));

			//Clear out the old card images from the visual elements of the players.
			playerVisual.getChildren().clear();
			dealerVisual.getChildren().clear();

			//For each of the cards in the hand add a card image to the visual elements.
			for(int i = 0; i < dealer.getHand().length; i++) {
				if(i == 0) {
					dealerVisual.getChildren().add(new ImageView(getPath("b1fv.png")));
				} else {
					dealerVisual.getChildren().add(new ImageView(getPath(dealer.getHand()[i].getImg())));
				}
			}
			for(Deck.Card card : player.getHand()) {
				playerVisual.getChildren().add(new ImageView(getPath(card.getImg())));
			}

			//Re-enable the buttons and remove the restart button
			hit.setDisable(false);
			stay.setDisable(false);
			btnBox.getChildren().remove(restart);
		});
	}

	/**
	 * Get the path to the card in the resource folder if the card exists.
	 * @param cardName
	 * @return
	 */
	public String getPath(String cardName) {
		return getClass().getResource("/com/landenbrewer/blackjack/Cards/" + cardName).toString();
	}

	public static void main(String[] args) {
		launch();
	}
}