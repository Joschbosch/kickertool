package zur.koeln.kickertool.uifxml.vm;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import zur.koeln.kickertool.api.BackendController;
import zur.koeln.kickertool.api.tournament.PlayerTournamentStatistics;
import zur.koeln.kickertool.api.tournament.Round;
import zur.koeln.kickertool.uifxml.FXMLMatchEntryController;
import zur.koeln.kickertool.uifxml.service.FXMLGUI;
import zur.koeln.kickertool.uifxml.service.FXMLGUIservice;
import zur.koeln.kickertool.uifxml.tools.TournamentStopWatch;

@Component
public class TournamentViewModel {

	@Autowired
	private TournamentStopWatch stopWatch;
	@Autowired
	private BackendController backendController;
	@Autowired
	private FXMLGUIservice fxmlGUIService;
	
	private final List<FXMLMatchEntryController> matchEntryControllerList = new ArrayList<>();
	private final ObservableList<Round> rounds = FXCollections.observableArrayList();
	private final ObservableList<PlayerTournamentStatistics> statistics = FXCollections.observableArrayList();
	private final BooleanProperty stopwatchToggleButtonSelectedProperty = new SimpleBooleanProperty();
	private final BooleanProperty btnCreateRoundsDisableProperty = new SimpleBooleanProperty(false);
	
	public void init() {
		getRounds().clear();
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
	private FXMLGUIservice getFxmlGUIService() {
		return fxmlGUIService;
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

	private List<FXMLMatchEntryController> getMatchEntryControllerList() {
		return matchEntryControllerList;
	}

	public void resetStopwatch() {
		getStopWatch().reset();
		getStopwatchToggleButtonSelectedProperty().set(false);
	}

	public void startStopwatch() {
		getStopWatch().start();
	}

	public void pauseResumeStopwatch() {
		if (getStopwatchToggleButtonSelectedProperty().get()) {
			getStopWatch().pause();
		} else {
			getStopWatch().resume();
		}
	}

	public BooleanProperty getStopwatchToggleButtonSelectedProperty() {
		return stopwatchToggleButtonSelectedProperty;
	}

	public BooleanProperty getBtnCreateRoundsDisableProperty() {
		return btnCreateRoundsDisableProperty;
	}

	public void createNewRound() {
		getBtnCreateRoundsDisableProperty().set(true);
		Round nextRound = getBackendController().nextRound();
		getRounds().add(nextRound);
	}
	
	public int getLastRoundIndex() {
		return getRounds().size() - 1;
	}
	
	public List<Pane> loadMatchesForRound(Round selectedRound) {
		
		getMatchEntryControllerList().clear();
		final List<Pane> matchPanes = new ArrayList<>();
		
		getBackendController().getMatchesForRound(selectedRound.getRoundNo()).forEach(eMatch -> {
			FXMLLoader fxmlLoader = getFxmlGUIService().getFXMLLoader(FXMLGUI.MATCH_ENTRY);
			matchPanes.add(fxmlLoader.getRoot());
			FXMLMatchEntryController matchEntryController = fxmlLoader.getController();
			matchEntryController.init(eMatch);
			getMatchEntryControllerList().add(matchEntryController);
		});
		
		return matchPanes;
	}
}
