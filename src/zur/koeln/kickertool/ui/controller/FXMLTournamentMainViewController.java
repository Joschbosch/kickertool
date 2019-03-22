package zur.koeln.kickertool.ui.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import zur.koeln.kickertool.application.api.dtos.TournamentDTO;
import zur.koeln.kickertool.application.api.dtos.base.SingleResponseDTO;
import zur.koeln.kickertool.application.handler.api.ITournamentCommandHandler;
import zur.koeln.kickertool.ui.api.BackgroundTask;
import zur.koeln.kickertool.ui.controller.base.AbstractFXMLController;
import zur.koeln.kickertool.ui.service.Icons;
import zur.koeln.kickertool.ui.vm.TournamentManagementViewModel;
import zur.koeln.kickertool.ui.vm.TournamentViewModel;
import javafx.fxml.FXML;
import com.jfoenix.controls.JFXButton;
import javafx.scene.control.ScrollPane;
import java.util.UUID;

@Component
@Getter(value = AccessLevel.PRIVATE)
public class FXMLTournamentMainViewController extends AbstractFXMLController<UUID> {

	@Autowired
	ITournamentCommandHandler tournamentCommandHandler;

	@Autowired
	TournamentManagementViewModel managementViewModel;

	@Setter(value = AccessLevel.PRIVATE)
	TournamentViewModel tournamentViewModel;
	
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
		getBtnStartPauseStopwatch().setGraphic(Icons.PLAY.createIconImageView());
		getBtnResetStopwatch().setGraphic(Icons.RESET.createIconImageView());
	}

	@FXML
	public void onNewRoundClicked() {
		startBackgroundTask(createNewRoundTask());
	}

	private BackgroundTask createNewRoundTask() {

		return new BackgroundTask<TournamentViewModel>() {

			@Override
			public TournamentViewModel performTask() throws Exception {

				return getManagementViewModel().startNewTournamentRound(getPayload());
			}

			@Override
			public void doOnSuccess(TournamentViewModel result) {
				setTournamentViewModel(result);
			}

			@Override
			public void doOnFailure(Throwable exception) {
				showError(exception);
			}
		};
	}

	private UUID getCurrentTournamendID() {
		return getPayload();
	}
}
