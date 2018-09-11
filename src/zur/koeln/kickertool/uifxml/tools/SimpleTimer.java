package zur.koeln.kickertool.uifxml.tools;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.util.Duration;
import lombok.AccessLevel;
import lombok.Getter;

@Getter(value=AccessLevel.PUBLIC)
public class SimpleTimer implements Timer{

	private Timeline timeline;
	private int startMilliSecs;
	
	private LongProperty timeSeconds = new SimpleLongProperty();
	
	private BooleanProperty runningProperty = new SimpleBooleanProperty(false);
	
	@Override
	public void init(int minutes) {
		startMilliSecs = minutes * 60 * 1000;
		timeSeconds.set(startMilliSecs);
	}

	@Override
	public void start() {
		timeline = new Timeline();
		timeline.getKeyFrames().add(
				new KeyFrame(Duration.millis(startMilliSecs), new KeyValue(timeSeconds, Integer.valueOf(0))));
		timeline.playFromStart();
		runningProperty.set(true);
	}
	
	public void pause() {
		if (timeline != null) {
			timeline.pause();
		}
	}
	
	@Override
	public void resume() {
		if (timeline != null) {
			timeline.play();
		}
	}

	@Override
	public void stop() {
		if (timeline != null) {
			timeline.stop();
		}
	}

	@Override
	public void reset() {
		stop();
		timeSeconds.set(startMilliSecs);
		runningProperty.set(false);
	}

}
