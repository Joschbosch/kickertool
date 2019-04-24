package zur.koeln.kickertool.ui.controller.lists;

import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import zur.koeln.kickertool.ui.controller.base.AbstractController;
import zur.koeln.kickertool.ui.controller.base.DialogCloseEvent;
import zur.koeln.kickertool.ui.controller.dialogs.vms.MatchResultViewModel;
import zur.koeln.kickertool.ui.controller.shared.vms.MatchDTOViewModel;
import zur.koeln.kickertool.ui.service.GUIEventService;
import zur.koeln.kickertool.ui.shared.DialogContentDefinition;
import zur.koeln.kickertool.ui.shared.GUIEvents;

@Component
@Getter(value=AccessLevel.PRIVATE)
public class MatchController extends AbstractController<MatchDTOViewModel>{

	@FXML Label lbl_player1_team1;
	@FXML Label lbl_player2_team1;
	@FXML Label lbl_player1_team2;
	@FXML Label lbl_player2_team2;
	@FXML Label lbl_result;
	@FXML Label lbl_tableNo;
	@FXML JFXButton btn_enterScore;
	
	@Setter(value = AccessLevel.PRIVATE)
	private MatchDTOViewModel vm;
	
	@FXML public void onEnterScoreClicked() {
		
		openDialog(DialogContentDefinition.MATCH_RESULT_DIALOG, getVm(), new DialogCloseEvent<MatchResultViewModel>() {

			@Override
			public void doAfterDialogClosed(MatchResultViewModel result) {
				
				getVm().setScoreHome(result.getScoreHomeTeam());
				getVm().setScoreVisiting(result.getScoreVisitingTeam());
				GUIEventService.getInstance().fireEvent(GUIEvents.MATCH_RESULT_ENTERED, getVm());
				setLabelTexts();
			}
		});
	}
	
	@Override
	public void doAfterInitializationCompleted(MatchDTOViewModel payload) {
		setVm(payload);
		setLabelTexts();
	}

	private void setLabelTexts() {
		getLbl_player1_team1().setText(getVm().getHomeTeam().getPlayer1().getFirstName());
		getLbl_player2_team1().setText(getVm().getHomeTeam().getPlayer2().getFirstName());
		getLbl_player1_team2().setText(getVm().getVisitingTeam().getPlayer1().getFirstName());
		getLbl_player2_team2().setText(getVm().getVisitingTeam().getPlayer2().getFirstName());
		getLbl_result().setText(getVm().getScoreHome() + " : " + getVm().getScoreVisiting());
		getLbl_tableNo().setText(getVm().getTable() == null ? "ND" : String.valueOf(getVm().getTable().getTableNumber()));
	}

	@Override
	public void handleEvent(GUIEvents guiEvents, Object content) {
		// nothing to do here
	}

	@Override
	protected void registerEvents() {
		// nothing to do here
	} 
	
}
