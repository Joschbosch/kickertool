package zur.koeln.kickertool.ui.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXButton;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import zur.koeln.kickertool.application.handler.dtos.TournamentDTO;
import zur.koeln.kickertool.ui.controller.base.AbstractController;
import zur.koeln.kickertool.ui.controller.base.BackgroundTask;
import zur.koeln.kickertool.ui.controller.shared.vms.MatchDTOViewModel;
import zur.koeln.kickertool.ui.controller.shared.vms.PlayerRankingRowViewModel;
import zur.koeln.kickertool.ui.controller.shared.vms.TournamentDTOViewModel;
import zur.koeln.kickertool.ui.controller.vms.TournamentMainViewModel;
import zur.koeln.kickertool.ui.service.FXMLGuiService;
import zur.koeln.kickertool.ui.shared.GUIEvents;
import zur.koeln.kickertool.ui.shared.ListContentDefinition;

@Component
@Getter(value = AccessLevel.PRIVATE)
public class TournamentMainController extends AbstractController<TournamentDTO> {

	@Autowired
	TournamentMainViewModel tournamentMainViewModel;

	@Setter(value = AccessLevel.PRIVATE)
	TournamentDTOViewModel tournamentDtoViewModel;

	@FXML
	JFXButton btnNewRound;

	@FXML VBox vboxMatches;

	@FXML TableView tblRankings;

	@FXML TableColumn colRank;

	@FXML TableColumn colName;

	@FXML TableColumn colPoints;

	@Override
	public void setupControls() {
//		getBtnStartPauseStopwatch().setGraphic(IconDefinition.PLAY.createIconImageView());
//		getBtnResetStopwatch().setGraphic(IconDefinition.RESET.createIconImageView());
	}

	@Override
	public void doAfterInitializationCompleted(TournamentDTO payload) {
		setTournamentDtoViewModel(getTournamentMainViewModel().mapFromTournamentDTO(payload));
		setupTable();
		startBackgroundTask(fillRankingsTableTask());
	}

	private void setupTable() {

		getColName().setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PlayerRankingRowViewModel, String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<PlayerRankingRowViewModel, String> param) {

				return param.getValue().getNameProperty();
			}
		});

		getColRank().setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PlayerRankingRowViewModel, Number>, ObservableValue<Number>>() {

			@Override
			public ObservableValue<Number> call(CellDataFeatures<PlayerRankingRowViewModel, Number> param) {

				return param.getValue().getRankProperty();
			}
		});

		getColPoints().setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PlayerRankingRowViewModel, Number>, ObservableValue<Number>>() {

			@Override
			public ObservableValue<Number> call(CellDataFeatures<PlayerRankingRowViewModel, Number> param) {

				return param.getValue().getScoreProperty();
			}
		});

	}

	@FXML
	public void onNewRoundClicked() {
		startBackgroundTask(createNewRoundTask());
	}

	private BackgroundTask createNewRoundTask() {

		return new BackgroundTask<TournamentDTOViewModel>() {

			@Override
			public TournamentDTOViewModel performTask() throws Exception {

				return getTournamentMainViewModel().startNewTournamentRound(getTournamentDtoViewModel().getUid());
			}

			@Override
			public void doOnSuccess(TournamentDTOViewModel result) {
				setTournamentDtoViewModel(result);
				fillMatchesList();
			}

			@Override
			public void doOnFailure(Throwable exception) {
				showError(exception);
			}
		};
	}

	private void fillMatchesList() {

		getVboxMatches().getChildren().clear();

		for (MatchDTOViewModel matchDTOViewModel : getTournamentDtoViewModel().getMatchesForCurrentRound()) {
			FXMLLoader loadedFXMLLoader = FXMLGuiService.getInstance().getLoadedFXMLLoader(ListContentDefinition.MATCH, matchDTOViewModel);
			getVboxMatches().getChildren().add(loadedFXMLLoader.getRoot());
		}

	}

	private BackgroundTask fillRankingsTableTask() {
		return new BackgroundTask<List<PlayerRankingRowViewModel>>() {

			@Override
			public List<PlayerRankingRowViewModel> performTask() throws Exception {
				return getTournamentMainViewModel().getPlayerRankings(getTournamentDtoViewModel().getUid(), getTournamentDtoViewModel().getCurrentRoundIndex());
			}

			@Override
			public void doOnSuccess(List<PlayerRankingRowViewModel> result) {
                getTournamentDtoViewModel().getPlayerRankings().clear();
				getTournamentDtoViewModel().getPlayerRankings().addAll(result);
                getTblRankings().setItems(getTournamentDtoViewModel().getPlayerRankings());
			}

			@Override
			public void doOnFailure(Throwable exception) {
				showError(exception);
			}
		};
	}

	@Override
	public void handleEvent(GUIEvents guiEvents, Object content) {
		if (guiEvents == GUIEvents.MATCH_RESULT_ENTERED) {
			MatchDTOViewModel matchDTOViewModel = (MatchDTOViewModel) content;
			getTournamentMainViewModel().upateMatchResult(matchDTOViewModel);
			startBackgroundTask(fillRankingsTableTask());
		}
	}

	@Override
	protected void registerEvents() {
		registerEvent(GUIEvents.MATCH_RESULT_ENTERED);
    }
}
