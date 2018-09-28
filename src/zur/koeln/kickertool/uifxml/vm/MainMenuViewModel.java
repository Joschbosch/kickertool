package zur.koeln.kickertool.uifxml.vm;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import zur.koeln.kickertool.api.BackendController;

@Component
public class MainMenuViewModel {

	@Autowired
	private BackendController backendController;

	private final StringProperty txtTournamentNameProperty = new SimpleStringProperty();
	private final ObservableList<String> importableTourmanents = FXCollections.observableArrayList();
	private final ObjectProperty<String> selectedTournamentFile = new SimpleObjectProperty<>();

	public void loadImportableTournaments() {
		getImportableTournaments().clear();
		getImportableTournaments().addAll(getBackendController().createTournamentsListForImport());
	}

	private BackendController getBackendController() {
		return backendController;
	}

	public StringProperty getTxtTournamentNameProperty() {
		return txtTournamentNameProperty;
	}

	public ObservableList<String> getImportableTournaments() {
		return importableTourmanents;
	}

	public ObjectProperty<String> getSelectedTournamentFile() {
		return selectedTournamentFile;
	}

	public void createNewTournament() {
		getBackendController().createNewTournament(getTxtTournamentNameProperty().getValue());
	}

	public void importTournament() throws IOException {
		getBackendController().importAndStartTournament(getSelectedTournamentFile().get());
	}

}
