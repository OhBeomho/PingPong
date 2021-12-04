package game.pingpong;

import java.util.Random;

import javafx.animatednodes.AnimatedButton;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class Main extends Application {
	private static final int WIDTH = 700, HEIGHT = 500;
	private static final int PLAYER_SPEED = 5, BALL_SPEED = 4;

	private Scene scene;
	private Pane gamePane;
	private StackPane mainPane;
	private Rectangle ball, player1, player2;
	private Label scoreLabel, startLabel, titleLabel, controlKeysLabel;
	private AnimationTimer moveObjects, controlObjects;
	private AnimatedButton startButton, quitButton;

	private boolean up1, down1, up2, down2, started;
	// up, down, left, right
	private boolean[] ballMoves = new boolean[4];
	private int player1Score, player2Score;
	private Label winLabel;

	public Main() {
		gamePane = new Pane();
		mainPane = new StackPane();
		scene = new Scene(mainPane, WIDTH, HEIGHT);
		ball = new Rectangle(30, 30);
		player1 = new Rectangle(8, 70);
		player2 = new Rectangle(8, 70);
		scoreLabel = new Label("1P   0 : 0   2P");
		startLabel = new Label("PRESS ANY KEY TO START");
		titleLabel = new Label("PING PONG GAME");
		controlKeysLabel = new Label("--CONTROL KEYS--\n1P: W, S\n2P: UP_ARROW, DOWN_ARROW");
		startButton = new AnimatedButton("START", 20);
		quitButton = new AnimatedButton("QUIT", 20);
		winLabel = new Label();

		moveObjects = new AnimationTimer() {
			@Override
			public void handle(long now) {
				if (up1) player1.setTranslateY(player1.getTranslateY() - PLAYER_SPEED);
				if (down1) player1.setTranslateY(player1.getTranslateY() + PLAYER_SPEED);
				if (up2) player2.setTranslateY(player2.getTranslateY() - PLAYER_SPEED);
				if (down2) player2.setTranslateY(player2.getTranslateY() + PLAYER_SPEED);

				if (ballMoves[0]) ball.setTranslateY(ball.getTranslateY() - BALL_SPEED);
				if (ballMoves[1]) ball.setTranslateY(ball.getTranslateY() + BALL_SPEED);
				if (ballMoves[2]) ball.setTranslateX(ball.getTranslateX() - BALL_SPEED);
				if (ballMoves[3]) ball.setTranslateX(ball.getTranslateX() + BALL_SPEED);
			}
		};
		controlObjects = new AnimationTimer() {
			@Override
			public void handle(long now) {
				boolean isNewGame = false;

				if (ball.getTranslateY() + ball.getHeight() >= HEIGHT) {
					ballMoves[1] = false;
					ballMoves[0] = true;
				} else if (ball.getTranslateY() <= 0) {
					ballMoves[0] = false;
					ballMoves[1] = true;
				}

				if (player1.getTranslateX() + player1.getWidth() > ball.getTranslateX()
					&& player1.getTranslateY() + player1.getHeight() > ball.getTranslateY()
					&& ball.getTranslateX() + ball.getWidth() > player1.getTranslateX()
					&& ball.getTranslateY() + ball.getHeight() > player1.getTranslateY()) {
					if (down1 == true) {
						ballMoves[0] = false;
						ballMoves[1] = true;
					} else if (up1 == true) {
						ballMoves[0] = true;
						ballMoves[1] = false;
					}

					ballMoves[2] = false;
					ballMoves[3] = true;
				} else if (player2.getTranslateX() + player2.getWidth() > ball.getTranslateX()
					&& player2.getTranslateY() + player2.getHeight() > ball.getTranslateY()
					&& ball.getTranslateX() + ball.getWidth() > player2.getTranslateX()
					&& ball.getTranslateY() + ball.getHeight() > player2.getTranslateY()) {
						if (down2 == true) {
							ballMoves[0] = false;
							ballMoves[1] = true;
						} else if (up2 == true) {
							ballMoves[0] = true;
							ballMoves[1] = false;
						}

						ballMoves[3] = false;
						ballMoves[2] = true;
					}

				if (ball.getTranslateX() >= WIDTH) {
					player1Score++;
					winLabel.setText("1P WINS!");
					started = true;
					newGame();
					isNewGame = true;
				} else if (ball.getTranslateX() + ball.getWidth() <= 0) {
					player2Score++;
					winLabel.setText("2P WINS!");
					started = true;
					newGame();
					isNewGame = true;
				}

				if (isNewGame) {
					scoreLabel.setText("1P   " + player1Score + " : " + player2Score + "   2P");

					Platform.runLater(() -> {
						try {
							Thread.sleep(2000);
							winLabel.setText("");
							gamePane.getChildren().add(startLabel);
							new AnimationTimer() {
								private int count = 10;

								@Override
								public void handle(long now) {
									count--;

									if (count <= 0) {
										count = 10;
										started = false;
										stop();
									}
								}
							}.start();
						} catch (Exception e) {
							e.printStackTrace();
						}
					});
				}
			}
		};
	}

	@Override
	public void start(Stage stage) {
		HBox buttons = new HBox();
		VBox vbox = new VBox();

		buttons.getChildren().addAll(startButton, quitButton);
		vbox.getChildren().addAll(titleLabel, buttons);

		buttons.setAlignment(Pos.CENTER);
		vbox.setAlignment(Pos.CENTER);

		buttons.setSpacing(10);
		vbox.setSpacing(40);

		controlKeysLabel.setTextAlignment(TextAlignment.CENTER);

		startButton.setOnAction(e -> {
			scene.setRoot(gamePane);
			gamePane.requestFocus();
		});
		quitButton.setOnAction(e -> System.exit(0));

		mainPane.getChildren().addAll(vbox, controlKeysLabel);
		gamePane.getChildren().addAll(ball, player1, player2, scoreLabel, startLabel, winLabel);

		StackPane.setAlignment(controlKeysLabel, Pos.BOTTOM_CENTER);

		gamePane.setId("GAMEPANE");
		mainPane.setId("MAINPANE");
		titleLabel.setId("TITLELABEL");
		winLabel.setId("WINLABEL");
		startLabel.setId("STARTLABEL");
		controlKeysLabel.setId("CKLABEL");

		ball.setTranslateX(WIDTH / 2 - ball.getWidth() / 2);
		ball.setTranslateY(HEIGHT / 2 - ball.getHeight() / 2);

		player1.setTranslateX(WIDTH - WIDTH / 1.2);
		player2.setTranslateX(WIDTH - WIDTH / 4.8);
		player1.setTranslateY(HEIGHT / 2 - player1.getHeight() / 2);
		player2.setTranslateY(HEIGHT / 2 - player2.getHeight() / 2);

		startLabel.setTranslateX(WIDTH / 2 - (startLabel.getText().length() * 18) / 2);
		startLabel.setTranslateY(100);

		// 21 is text length. line 73 and 68
		winLabel.setTranslateX(WIDTH / 2 - (8 * 18) / 2);
		winLabel.setTranslateY(100);

		scoreLabel.translateXProperty().bind(stage.widthProperty().divide(2).subtract((scoreLabel.getText().length() * 10) / 2));

		gamePane.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.W) up1 = true;
			if (e.getCode() == KeyCode.S) down1 = true;
			if (e.getCode() == KeyCode.UP) up2 = true;
			if (e.getCode() == KeyCode.DOWN) down2 = true;

			if (!started) startGame();
		});
		gamePane.setOnKeyReleased(e -> {
			if (e.getCode() == KeyCode.W) up1 = false;
			if (e.getCode() == KeyCode.S) down1 = false;
			if (e.getCode() == KeyCode.UP) up2 = false;
			if (e.getCode() == KeyCode.DOWN) down2 = false;
		});

		scene.getStylesheets().add(Main.class.getResource("style.css").toString());

		stage.setScene(scene);
		stage.setTitle("PING PONG");
		stage.setResizable(false);
		stage.show();
	}

	private void startGame() {
		gamePane.getChildren().remove(startLabel);

		Random random = new Random();
		int randomNumber = random.nextInt(2);

		if (randomNumber == 0) {
			ballMoves[0] = true;
			ballMoves[2] = true;
		} else {
			ballMoves[1] = true;
			ballMoves[3] = true;
		}

		moveObjects.start();
		controlObjects.start();
		started = true;
	}

	private void newGame() {
		moveObjects.stop();
		controlObjects.stop();

		ball.setTranslateX(WIDTH / 2 - ball.getWidth() / 2);
		ball.setTranslateY(HEIGHT / 2 - ball.getHeight() / 2);

		player1.setTranslateX(WIDTH - WIDTH / 1.2);
		player2.setTranslateX(WIDTH - WIDTH / 4.8);
		player1.setTranslateY(HEIGHT / 2 - player1.getHeight() / 2);
		player2.setTranslateY(HEIGHT / 2 - player2.getHeight() / 2);

		for (int i = 0; i < ballMoves.length; i++) ballMoves[i] = false;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
