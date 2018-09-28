package zur.koeln.kickertool.uifxml;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.uifxml.service.FXMLGUI;
import zur.koeln.kickertool.uifxml.service.FXMLGUIservice;
import zur.koeln.kickertool.uifxml.vm.MainMenuViewModel;

@Getter(value = AccessLevel.PRIVATE)
@Component
public class FXMLMainMenuController{

	@FXML
	private Button btnCreateNewTournament;
	@FXML
	private Button btnManagePlayers;
	@FXML
	private TextField txtTournamentName;
	@FXML
	private Button btnImportTournament;
	@FXML
	private ComboBox cmbTournaments;
	
	@Autowired
	private MainMenuViewModel vm;
	@Autowired
	private FXMLGUIservice guiService;
	
	@FXML
	public void initialize() {

		getBtnCreateNewTournament().disableProperty().bind(getVm().getTxtTournamentNameProperty().isEmpty());
		getBtnImportTournament().disableProperty().bind(getVm().getSelectedTournamentFile().isNull());
		getTxtTournamentName().textProperty().bindBidirectional(getVm().getTxtTournamentNameProperty());
		getVm().getSelectedTournamentFile().bind(getCmbTournaments().getSelectionModel().selectedItemProperty());
		getCmbTournaments().setItems(getVm().getImportableTournaments());
		
		getVm().loadImportableTournaments();
	}

	@FXML
	public void onCreateNewTournamentClicked(ActionEvent event) {
		getVm().createNewTournament();
		getGuiService().switchToScene(FXMLGUI.TOURNAMENT_CONFIGURATION);
	}

	@FXML
	public void onImportTournamentClicked(ActionEvent event) {
		try {
			getVm().importTournament();
			getGuiService().switchToScene(FXMLGUI.TOURMANENT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void onManagePlayersClicked(ActionEvent event) {
		getGuiService().switchToScene(FXMLGUI.PLAYER_POOL_MANAGEMENT);
	}
	
}
