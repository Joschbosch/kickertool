package zur.koeln.kickertool.ui.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import zur.koeln.kickertool.application.handler.dtos.TournamentDTO;
import zur.koeln.kickertool.ui.controller.base.AbstractController;
import zur.koeln.kickertool.ui.controller.base.BackgroundTask;
import zur.koeln.kickertool.ui.controller.shared.vms.MatchDTOViewModel;
import zur.koeln.kickertool.ui.controller.shared.vms.TournamentDTOViewModel;
import zur.koeln.kickertool.ui.controller.vms.TournamentMainViewModel;
import zur.koeln.kickertool.ui.service.FXMLGuiService;
import zur.koeln.kickertool.ui.shared.GUIEvents;
import zur.koeln.kickertool.ui.shared.IconDefinition;
import zur.koeln.kickertool.ui.shared.ListContentDefinition;
import javafx.scene.layout.VBox;

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

	@FXML VBox vboxMatches;

	@Override
	public void setupControls() {
		getBtnStartPauseStopwatch().setGraphic(IconDefinition.PLAY.createIconImageView());
		getBtnResetStopwatch().setGraphic(IconDefinition.RESET.createIconImageView());
	}
	
	@Override
	public void doAfterInitializationCompleted(TournamentDTO payload) {
		setTournamentDtoViewModel(getTournamentMainViewModel().mapFromTournamentDTO(payload));
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
		
		for (MatchDTOViewModel matchDTOViewModel : getTournamentDtoViewModel().getMatches()) {
			FXMLLoader loadedFXMLLoader = FXMLGuiService.getInstance().getLoadedFXMLLoader(ListContentDefinition.MATCH, matchDTOViewModel);
			getVboxMatches().getChildren().add(loadedFXMLLoader.getRoot());
		}
		
	}
	
	@Override
	public void handleEvent(GUIEvents guiEvents, Object content) {
		if (guiEvents == GUIEvents.MATCH_RESULT_ENTERED) {
			MatchDTOViewModel matchDTOViewModel = (MatchDTOViewModel) content;
			// TODO Insert update methode here
			// TODO Update View
		}
	}

	@Override
	protected void registerEvents() {
		registerEvent(GUIEvents.MATCH_RESULT_ENTERED);
	} 
}
