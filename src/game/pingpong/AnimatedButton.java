package game.pingpong;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.util.Duration;

public class AnimatedButton extends Button {
	private Timeline timeline;
	private KeyFrame mouseEnter, mouseExit;
	private KeyValue mouseEnterValue, mouseExitValue;

	private boolean isPlaying;
	private double maxWidth, minWidth;

	public AnimatedButton(String text) {
		super(text);

		setPrefWidth(text.length() * 20);
		setPrefHeight(30);

		maxWidth = getPrefWidth() + 10;
		minWidth = getPrefWidth();

		timeline = new Timeline();
		mouseEnterValue = new KeyValue(prefWidthProperty(), maxWidth, Interpolator.EASE_IN);
		mouseExitValue = new KeyValue(prefWidthProperty(), minWidth, Interpolator.EASE_IN);
		mouseEnter = new KeyFrame(Duration.seconds(0.05), mouseEnterValue);
		mouseExit = new KeyFrame(Duration.seconds(0.05), mouseExitValue);

		setOnMouseEntered(e -> {
			if (isPlaying) return;

			timeline.getKeyFrames().add(mouseEnter);
			timeline.setOnFinished(e1 -> {
				isPlaying = false;
				timeline.getKeyFrames().clear();
			});
			timeline.play();
		});
		setOnMouseExited(e -> {
			if (isPlaying) return;

			timeline.getKeyFrames().add(mouseExit);
			timeline.setOnFinished(e1 -> {
				isPlaying = false;
				timeline.getKeyFrames().clear();
			});
			timeline.play();
		});
	}
}
