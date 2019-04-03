package zur.koeln.kickertool.ui.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import zur.koeln.kickertool.application.api.dtos.TournamentDTO;
import zur.koeln.kickertool.ui.controller.base.AbstractController;
import zur.koeln.kickertool.ui.controller.base.BackgroundTask;
import zur.koeln.kickertool.ui.controller.shared.vms.TournamentDTOViewModel;
import zur.koeln.kickertool.ui.controller.vms.TournamentMainViewModel;
import zur.koeln.kickertool.ui.shared.IconDefinition;

@Component
@Getter(value = AccessLevel.PRIVATE)
public class TournamentMainController extends AbstractController<TournamentDTO> {

	@Autowired
	TournamentMainViewModel tournamentMainViewModel;

	@Setter(value = AccessLevel.PRIVATE)
	TournamentDTOViewModel tournamentDtoViewModel;
	
	@FXML
	JFXButton btnStartPauseStopwatch;
	@FXML
	JFXButton btnResetStopwatch;
	@FXML
	JFXButton btnNewRound;

	@FXML
	ScrollPane pnlMatches;

	@Override
	public void setupControls() {
		getBtnStartPauseStopwatch().setGraphic(IconDefinition.PLAY.createIconImageView());
		getBtnResetStopwatch().setGraphic(IconDefinition.RESET.createIconImageView());
	}
	
	@Override
	public void doAfterInitializationCompleted() {
		setTournamentDtoViewModel(getTournamentMainViewModel().mapFromTournamentDTO(getPayload()));
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
			}

			@Override
			public void doOnFailure(Throwable exception) {
				showError(exception);
			}
		};
	}
	
}
