package zur.koeln.kickertool.uifxml;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.api.tournament.Match;
import zur.koeln.kickertool.uifxml.vm.MatchEntryViewModel;

@Getter(value=AccessLevel.PRIVATE)
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FXMLMatchEntryController {

	@FXML
	private Label lblScore;
	@FXML
	private Label lblTable;
	@FXML 
	private Button btnFinish;
	@FXML 
	private Label lblPlayer1TeamHome;
	@FXML 
	private Label lblPlayer1TeamVisit;
	@FXML 
	private Label lblPlayer2TeamHome;
	@FXML 
	private Label lblPlayer2TeamVisit;
	
    @Autowired
    private MatchEntryViewModel vm;
	
	public void init(Match currentMatch) {

		getLblPlayer1TeamHome().textProperty().bind(getVm().getPlayer1TeamHomeNameProperty());
		getLblPlayer1TeamVisit().textProperty().bind(getVm().getPlayer1TeamVisitNameProperty());
		getLblPlayer2TeamHome().textProperty().bind(getVm().getPlayer2TeamHomeNameProperty());
		getLblPlayer2TeamVisit().textProperty().bind(getVm().getPlayer2TeamVisitNameProperty());
		getLblScore().textProperty().bind(getVm().getScoreProperty());
		getLblTable().textProperty().bind(getVm().getTableNameProperty());
		getBtnFinish().disableProperty().bind(getVm().getBtnFinishMatchDisableProperty());
		getBtnFinish().visibleProperty().bind(getVm().getBtnFinishMatchVisibleProperty());
		
		getVm().init(currentMatch);
		
	}
	
	@FXML public void onBtnFinishClicked() {
		
		getVm().openScoreEntryDialog();
		
	}

}
