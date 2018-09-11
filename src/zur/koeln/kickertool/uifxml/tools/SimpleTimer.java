package zur.koeln.kickertool.uifxml.tools;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.util.Duration;
import lombok.Getter;

@Getter
public class SimpleTimer implements Timer{

	private Timeline timeline;
	private int startMilliSecs;
	
	private LongProperty timeSeconds = new SimpleLongProperty();
	
	private BooleanProperty runningProperty = new SimpleBooleanProperty(false);
	
	@Override
	public void init(int minutes) {
		startMilliSecs = minutes * 60 * 1000;
		getTimeSeconds().set(getStartMilliSecs());
	}

	@Override
	public void start() {
		timeline = new Timeline();
		getTimeline().getKeyFrames().add(
				new KeyFrame(Duration.millis(getStartMilliSecs()), new KeyValue(getTimeSeconds(), Integer.valueOf(0))));
		getTimeline().playFromStart();
		getRunningProperty().set(true);
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
