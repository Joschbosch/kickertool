package zur.koeln.kickertool.ui.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import zur.koeln.kickertool.application.handler.api.ITournamentCommandHandler;
import zur.koeln.kickertool.ui.api.BackgroundTask;
import zur.koeln.kickertool.ui.controller.base.AbstractFXMLController;
import zur.koeln.kickertool.ui.controller.vms.TournamentManagementViewModel;
import zur.koeln.kickertool.ui.controller.vms.TournamentViewModel;
import zur.koeln.kickertool.ui.shared.Icons;

@Component
@Getter(value = AccessLevel.PRIVATE)
public class TournamentMainController extends AbstractFXMLController<UUID> {

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

				return getManagementViewModel().startNewTournamentRound(getCurrentTournamendID());
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
