module com.landenbrewer.blackjack {
	requires javafx.controls;
	requires javafx.fxml;

	requires org.controlsfx.controls;

	opens com.landenbrewer.blackjack to javafx.fxml;
	exports com.landenbrewer.blackjack;
}