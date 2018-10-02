package zur.koeln.kickertool.uifxml.vm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.LongProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import zur.koeln.kickertool.api.BackendController;
import zur.koeln.kickertool.api.tournament.PlayerTournamentStatistics;
import zur.koeln.kickertool.api.tournament.Round;
import zur.koeln.kickertool.uifxml.tools.TournamentStopWatch;

@Component
public class TournamentViewModel {

	@Autowired
	private TournamentStopWatch stopWatch;
	@Autowired
	private BackendController backendController;
	
	private final ObservableList<Round> rounds = FXCollections.observableArrayList();
	private final ObservableList<PlayerTournamentStatistics> statistics = FXCollections.observableArrayList();
	
	public void init() {
		getStopWatch().init(getBackendController().getCurrentTournament().getSettings().getMinutesPerMatch());
		
		getStatistics().clear();
		getStatistics().addAll(getBackendController().getCurrentTable());
	}
	
	private TournamentStopWatch getStopWatch() {
		return stopWatch;
	}
	private BackendController getBackendController() {
		return backendController;
	}
	public ObservableList<Round> getRounds() {
		return rounds;
	}
	public ObservableList<PlayerTournamentStatistics> getStatistics() {
		return statistics;
	}
	
	public LongProperty getTimePropertyFromStopWatch() {
		return getStopWatch().getTimeInSecondsLongProperty();
	}
	
	public BooleanProperty getIsRunningPropertyFromStopWatch() {
		return getStopWatch().getIsRunningProperty();
	}
}
