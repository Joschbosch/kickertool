package zur.koeln.kickertool.ui.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.application.api.dtos.TournamentDTO;
import zur.koeln.kickertool.application.api.dtos.base.SingleResponseDTO;
import zur.koeln.kickertool.application.handler.api.ITournamentCommandHandler;
import zur.koeln.kickertool.ui.api.BackgroundTask;
import zur.koeln.kickertool.ui.controller.base.AbstractFXMLController;
import zur.koeln.kickertool.ui.service.Icons;
import zur.koeln.kickertool.ui.vm.TournamentMainViewViewModel;
import javafx.fxml.FXML;
import com.jfoenix.controls.JFXButton;
import javafx.scene.control.ScrollPane;
import java.util.UUID;

@Component
@Getter(value=AccessLevel.PRIVATE)
public class FXMLTournamentMainViewController extends AbstractFXMLController<UUID> {

	@Autowired
	@Getter(value = AccessLevel.PRIVATE)
	ITournamentCommandHandler tournamentCommandHandler;
	
	@Autowired
	TournamentMainViewViewModel vm;
	
	@FXML JFXButton btnStartPauseStopwatch;
	@FXML JFXButton btnResetStopwatch;
	@FXML JFXButton btnNewRound;

	@FXML ScrollPane pnlMatches;
	
	@Override
	public void setupControls() {
		getBtnStartPauseStopwatch().setGraphic(Icons.PLAY.createIconImageView());
		getBtnResetStopwatch().setGraphic(Icons.RESET.createIconImageView());
	}

	@FXML public void onNewRoundClicked() {
		startBackgroundTask(createNewRoundTask());
	}
	
	private BackgroundTask createNewRoundTask() {
	
		return new BackgroundTask<TournamentDTO>() {

			@Override
			public TournamentDTO performTask() throws Exception {
				SingleResponseDTO<TournamentDTO> tournamentWithNewRounds = getTournamentCommandHandler().startNextTournamentRound(getCurrentTournamendID());
				checkResponse(tournamentWithNewRounds);
				return tournamentWithNewRounds.getDtoValue();
			}

			@Override
			public void doOnSuccess(TournamentDTO result) {
				
			}

			@Override
			public void doOnFailure(Throwable exception) {
				
			}
		};
	}

	private UUID getCurrentTournamendID() {
		return getPayload();
	}
}
