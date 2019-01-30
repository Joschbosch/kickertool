package zur.koeln.kickertool.deprecated.uifxml.tools;


import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import lombok.Getter;

@Getter
public class SimpleTimer implements Timer{

	private Timeline timeline;
	private int startMilliSecs;
	
	private final LongProperty timeSeconds = new SimpleLongProperty();
	
	private final BooleanProperty runningProperty = new SimpleBooleanProperty(false);
    private Media media;
	
	@Override
	public void init(int minutes) {
        startMilliSecs = minutes * 60 * 1000;
        getTimeSeconds().set(getStartMilliSecs());
        media = new Media(this.getClass().getResource("/audio/timeout.wav").toString());

	}

	@Override
	public void start() {

		timeline = new Timeline();
		getTimeline().getKeyFrames().add(
				new KeyFrame(Duration.millis(getStartMilliSecs()), new KeyValue(getTimeSeconds(), Integer.valueOf(0))));
		getTimeline().playFromStart();
		getRunningProperty().set(true);
        timeline.setOnFinished(event -> {

            MediaPlayer player = new MediaPlayer(media);
            player.play();

        });
	}
	
	public void pause() {
		if (getTimeline() != null) {
			getTimeline().pause();
		}
	}
	
	@Override
	public void resume() {
		if (getTimeline() != null) {
			getTimeline().play();
		}
	}

	@Override
	public void stop() {
		if (getTimeline() != null) {
			getTimeline().stop();
		}
	}

	@Override
	public void reset() {
		stop();
		getTimeSeconds().set(getStartMilliSecs());
		getRunningProperty().set(false);
	}
	
}
