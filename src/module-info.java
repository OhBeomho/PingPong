module PingPong {
	requires javafx.controls;
	
	opens game.pingpong to javafx.graphics, javafx.fxml;
}
