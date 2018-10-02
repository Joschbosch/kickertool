package zur.koeln.kickertool.uifxml.tools;

import org.springframework.stereotype.Component;

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

@SuppressWarnings("nls")
@Component
public class TournamentStopWatch implements Timer{

	private Timeline timeline;
	private int startMilliSecs;
	
	private final LongProperty timeInSecondsLongProperty = new SimpleLongProperty();
	private final BooleanProperty isRunningProperty = new SimpleBooleanProperty(false);
	
    private final Media media = new Media(this.getClass().getResource("/audio/timeout.wav").toString());
	
	@Override
	public void init(int minutes) {
        setStartMilliSecs(minutes * 60 * 1000);
        getTimeInSecondsLongProperty().set(getStartMilliSecs());
	}

	@Override
	public void start() {

		setTimeline(new Timeline());
		getTimeline().getKeyFrames().add( new KeyFrame(Duration.millis(getStartMilliSecs()), new KeyValue(getTimeInSecondsLongProperty(), Integer.valueOf(0))));
		getTimeline().playFromStart();
		getIsRunningProperty().set(true);
		getTimeline().setOnFinished(event -> {

            MediaPlayer player = new MediaPlayer(getMedia());
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
		getTimeInSecondsLongProperty().set(getStartMilliSecs());
		getIsRunningProperty().set(false);
	}

	public LongProperty getTimeInSecondsLongProperty() {
		return timeInSecondsLongProperty;
	}

	public BooleanProperty getIsRunningProperty() {
		return isRunningProperty;
	}

	private int getStartMilliSecs() {
		return startMilliSecs;
	}

	private void setStartMilliSecs(int startMilliSecs) {
		this.startMilliSecs = startMilliSecs;
	}

	private Timeline getTimeline() {
		return timeline;
	}

	private void setTimeline(Timeline timeline) {
		this.timeline = timeline;
	}

	private Media getMedia() {
		return media;
	}

	
	
}
