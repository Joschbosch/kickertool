package zur.koeln.kickertool.uifxml.vm;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import zur.koeln.kickertool.api.BackendController;
import zur.koeln.kickertool.api.player.Player;
import zur.koeln.kickertool.api.tournament.PlayerTournamentStatistics;
import zur.koeln.kickertool.api.tournament.Round;
import zur.koeln.kickertool.tournament.PlayerTournamentStatisticsImpl;
import zur.koeln.kickertool.uifxml.FXMLMatchEntryController;
import zur.koeln.kickertool.uifxml.dialog.AddPlayerDialog;
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
	private final BooleanProperty btnPausePlayerDisableProperty = new SimpleBooleanProperty(false);
	private final BooleanProperty btnResumePlayerDisableProperty = new SimpleBooleanProperty(false);
	
	public void init() {
		getRounds().clear();
		getStopWatch().init(getBackendController().getCurrentTournament().getSettings().getMinutesPerMatch());
		
		updateStatisticsTable();
	}
	
	private void updateStatisticsTable() {
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
	
	public BooleanProperty getBtnPausePlayerDisableProperty() {
		return btnPausePlayerDisableProperty;
	}

	public BooleanProperty getBtnResumePlayerDisableProperty() {
		return btnResumePlayerDisableProperty;
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
	
	public List<Pane> loadMatchesForRound(final Round selectedRound) {
		
		getMatchEntryControllerList().clear();
		final List<Pane> matchPanes = new ArrayList<>();
		
		getBackendController().getMatchesForRound(selectedRound.getRoundNo()).forEach(eMatch -> {
			FXMLLoader fxmlLoader = getFxmlGUIService().getFXMLLoader(FXMLGUI.MATCH_ENTRY);
			matchPanes.add(fxmlLoader.getRoot());
			FXMLMatchEntryController matchEntryController = fxmlLoader.getController();
			matchEntryController.init(eMatch, selectedRound);
			getMatchEntryControllerList().add(matchEntryController);
		});
		
		return matchPanes;
	}

	public void updateFXMLMatchEntryController() {
		getMatchEntryControllerList().forEach(FXMLMatchEntryController::update);
		getBtnCreateRoundsDisableProperty().set(!getBackendController().getCurrentTournament().isCurrentRoundComplete());
		updateStatisticsTable();
	}

	private void addLatePlayerToTournament(Player player) {
		getBackendController().addParticipantToTournament(player);
		updateStatisticsTable();
	}

	public void openAddLatePlayerDialog() {
		AddPlayerDialog<List<Player>> addPlayerDialog = new AddPlayerDialog(getFxmlGUIService());
		addPlayerDialog.init();
		Optional<List<Player>> selectedPlayer = addPlayerDialog.showAndWait();
		
		if (selectedPlayer.isPresent()) {
			selectedPlayer.get().forEach(this::addLatePlayerToTournament);
		}
	}

	public void pausePlayer(Player selectedPlayer) {
		getBackendController().pausePlayer(selectedPlayer.getUid());
		updateStatisticsTable();
	}

	public void resumePlayer(Player selectedPlayer) {
		getBackendController().unpausePlayer(selectedPlayer.getUid());
		updateStatisticsTable();
	}

	public void enableDisablePauseResumePlayer(PlayerTournamentStatisticsImpl playerTournamentStatisticsImpl) {
		getBtnPausePlayerDisableProperty().set(playerTournamentStatisticsImpl == null || playerTournamentStatisticsImpl.isPlayerPausing());
		getBtnResumePlayerDisableProperty().set(playerTournamentStatisticsImpl == null || !playerTournamentStatisticsImpl.isPlayerPausing());
	}
}
