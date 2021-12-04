module PingPong {
	requires javafx.controls;
	requires animatednodes;
	
	opens game.pingpong to javafx.graphics, javafx.fxml;
}
