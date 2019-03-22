package zur.koeln.kickertool.ui.controller.dialogs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;

import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.util.StringConverter;
import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.ui.api.BackgroundTask;
import zur.koeln.kickertool.ui.api.FXMLDialogContent;
import zur.koeln.kickertool.ui.api.events.DialogCloseEvent;
import zur.koeln.kickertool.ui.controller.base.AbstractFXMLController;
import zur.koeln.kickertool.ui.controller.vms.PlayerViewModel;
import zur.koeln.kickertool.ui.controller.vms.TournamentConfigurationViewModel;
import zur.koeln.kickertool.ui.controller.vms.TournamentSettingsViewModel;
import zur.koeln.kickertool.ui.controller.vms.base.ModelValidationResult;
import zur.koeln.kickertool.ui.shared.DialogContent;
import zur.koeln.kickertool.ui.shared.Icons;

@SuppressWarnings("nls")
@Component
@Getter(value=AccessLevel.PRIVATE)
public class FXMLTournamentConfigurationDialogContent extends AbstractFXMLController implements FXMLDialogContent<Void, TournamentConfigurationViewModel>{

	@FXML 
	JFXListView lstAvailablePlayers;
	
	@FXML 
	JFXListView lstPlayersSelectedForTournament;
	
	@FXML 
	JFXButton btnAddPlayer;
	
	@FXML 
	JFXButton btnRemovePlayer;
	
	@FXML 
	JFXButton btnOpenSettings;

	@FXML 
	Label lblSettingsName;
	
	@FXML 
	JFXTextField txtTournamentName;
	
	@Autowired
	TournamentConfigurationViewModel vm;
	
	@Override
	public void setupControls() {
		getBtnOpenSettings().setGraphic(Icons.SETTINGS.createIconImageView());
		getBtnAddPlayer().setGraphic(Icons.ARROW_RIGHT.createIconImageView());
		getBtnRemovePlayer().setGraphic(Icons.ARROW_LEFT.createIconImageView());
		
		setupListCells();
	}

	private void setupListCells() {
		getLstAvailablePlayers().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		getLstAvailablePlayers().setCellFactory(TextFieldListCell.forListView(new StringConverter<PlayerViewModel>() {

			@Override
			public String toString(PlayerViewModel object) {
				return object.getLabel();
			}

			@Override
			public PlayerViewModel fromString(String string) {
				return null;
			}
		}));
		
		getLstPlayersSelectedForTournament().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		getLstPlayersSelectedForTournament().setCellFactory(TextFieldListCell.forListView(new StringConverter<PlayerViewModel>() {

			@Override
			public String toString(PlayerViewModel object) {
				return object.getLabel();
			}

			@Override
			public PlayerViewModel fromString(String string) {
				return null;
			}
		}));
	}
	
	@Override
	public void setupBindings() {
		
		getLstAvailablePlayers().setItems(getVm().getAvailablePlayers().sorted());
		getLstPlayersSelectedForTournament().setItems(getVm().getPlayersForTournament().sorted());
		getTxtTournamentName().textProperty().bindBidirectional(getVm().getTournamentNameProperty());
		
		getBtnAddPlayer().disableProperty().bind(Bindings.size(getLstAvailablePlayers().getSelectionModel().getSelectedItems()).isEqualTo(0));
		getBtnRemovePlayer().disableProperty().bind(Bindings.size(getLstPlayersSelectedForTournament().getSelectionModel().getSelectedItems()).isEqualTo(0));
	}
	
	@Override
	public void doAfterInitializationCompleted() {
		getVm().getAvailablePlayers().clear();
		getVm().getPlayersForTournament().clear();
		
		startBackgroundTask(loadAvailablePlayersTask());
		startBackgroundTask(loadDefaultSettingsTask());
	}


	private BackgroundTask loadAvailablePlayersTask() {
		return new BackgroundTask<List<PlayerViewModel>>() {

			@Override
			public List<PlayerViewModel> performTask() throws Exception {
				return getVm().loadAllPlayer();
			}

			@Override
			public void doOnSuccess(List<PlayerViewModel> result) {
				getVm().getAvailablePlayers().addAll(result);
			}

			@Override
			public void doOnFailure(Throwable exception) {
				showError(exception);
			}
		};
	}
	
	private BackgroundTask loadDefaultSettingsTask() {
		return new BackgroundTask<TournamentSettingsViewModel>() {

			@Override
			public TournamentSettingsViewModel performTask() throws Exception {
				
				return getVm().loadDefaultSettings();
			}

			@Override
			public void doOnSuccess(TournamentSettingsViewModel result) {
				getVm().setDefaultSettings(result);
			}

			@Override
			public void doOnFailure(Throwable exception) {
				showError(exception);
			}
		};
	}

	@FXML 
	public void onOpenSettingsClicked() {
		openDialog(DialogContent.TOURNAMENT_SETTINGS_DIALOG, getVm().getSettingsVm(), new DialogCloseEvent<TournamentSettingsViewModel>() {

			@Override
			public void doAfterDialogClosed(TournamentSettingsViewModel result) {
				getVm().setDefaultSettings(result);
				getLblSettingsName().setText("Eigene Einstellungen");
			}
		});
	}
	
	@FXML 
	public void onAddPlayerClicked() {
		ObservableList selectedPlayer = getLstAvailablePlayers().getSelectionModel().getSelectedItems();
		getVm().getPlayersForTournament().addAll(selectedPlayer);
		getVm().getAvailablePlayers().removeAll(selectedPlayer);
		
	}
	
	@FXML 
	public void onRemovePlayerClicked() {
		ObservableList selectedPlayer = getLstPlayersSelectedForTournament().getSelectionModel().getSelectedItems();
		getVm().getAvailablePlayers().addAll(selectedPlayer);
		getVm().getPlayersForTournament().removeAll(selectedPlayer);
	}

	@Override
	public ModelValidationResult validate() {
		return getVm().validate();
	}

	@Override
	public TournamentConfigurationViewModel sendResult() {
		return getVm();
	}
}
