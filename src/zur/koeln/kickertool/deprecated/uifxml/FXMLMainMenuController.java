package zur.koeln.kickertool.deprecated.uifxml;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.base.BasicBackendController;

@Getter(value = AccessLevel.PRIVATE)
public class FXMLMainMenuController implements UpdateableUIComponent {

	@Autowired
    private BasicBackendController backendController;
	@FXML
	private Button btnCreateNewTournament;
	@FXML
	private Button btnManagePlayers;
	@FXML
	private TextField txtTournamentName;

	@FXML
	private Button btnImportTournament;
	@FXML
	ComboBox cmbTournaments;

	@FXML
	public void initialize() {

		getBtnCreateNewTournament().disableProperty()
				.bind(Bindings.greaterThan(1, getTxtTournamentName().textProperty().length()));
		ObservableList<String> comboboxItems = FXCollections.observableArrayList();
		comboboxItems.addAll(backendController.createTournamentsListForImport());
		getCmbTournaments().setItems(comboboxItems);
		getCmbTournaments().setDisable(comboboxItems.isEmpty());
		getBtnImportTournament().disableProperty().bind(Bindings.isEmpty(comboboxItems));
		
		if (!comboboxItems.isEmpty()) {
			getCmbTournaments().getSelectionModel().clearAndSelect(0);
		}

	}

	// Event Listener on Button[#btnCreateNewTournament].onAction
	@FXML
	public void onCreateNewTournamentClicked(ActionEvent event) {
		backendController.createNewTournament(txtTournamentName.getText());
	}

	// Event Listener on Button[#btnManagePlayers].onAction
	@FXML
	public void onManagePlayersClicked(ActionEvent event) {
		backendController.showPlayerPoolManagement();
	}

	// Event Listener on Button[#onImportTournamentClicked
	@FXML
	public void onImportTournamentClicked(ActionEvent event) {
		try {
			backendController.importAndStartTournament((String) getCmbTournaments().getSelectionModel().getSelectedItem());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update() {
		//
	}
}
