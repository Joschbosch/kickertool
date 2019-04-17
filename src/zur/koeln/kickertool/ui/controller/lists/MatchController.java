package zur.koeln.kickertool.ui.controller.lists;

import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import lombok.AccessLevel;
import lombok.Getter;
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
	
	@FXML public void onEnterScoreClicked() {}

}
