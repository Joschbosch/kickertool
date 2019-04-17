package zur.koeln.kickertool.ui.controller.lists;

import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import zur.koeln.kickertool.ui.controller.base.AbstractController;
import zur.koeln.kickertool.ui.controller.shared.vms.MatchDTOViewModel;

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
		getLbl_tableNo().setText(getVm().getTable() == null ? "ND" : String.valueOf(getVm().getTable().getTableNumber()));
	}

}
