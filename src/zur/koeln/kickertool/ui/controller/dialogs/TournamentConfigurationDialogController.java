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
import zur.koeln.kickertool.ui.controller.base.AbstractController;
import zur.koeln.kickertool.ui.controller.base.BackgroundTask;
import zur.koeln.kickertool.ui.controller.base.DialogCloseEvent;
import zur.koeln.kickertool.ui.controller.base.DialogContent;
import zur.koeln.kickertool.ui.controller.base.vm.ModelValidationResult;
import zur.koeln.kickertool.ui.controller.dialogs.vms.TournamentConfigurationViewModel;
import zur.koeln.kickertool.ui.controller.dialogs.vms.TournamentSettingsViewModel;
import zur.koeln.kickertool.ui.controller.shared.vms.PlayerDTOViewModel;
import zur.koeln.kickertool.ui.shared.DialogContentDefinition;
import zur.koeln.kickertool.ui.shared.IconDefinition;

@SuppressWarnings("nls")
@Component
@Getter(value=AccessLevel.PRIVATE)
public class TournamentConfigurationDialogController extends AbstractController implements DialogContent<Void, TournamentConfigurationViewModel>{

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
		getBtnOpenSettings().setGraphic(IconDefinition.SETTINGS.createIconImageView());
		getBtnAddPlayer().setGraphic(IconDefinition.ARROW_RIGHT.createIconImageView());
		getBtnRemovePlayer().setGraphic(IconDefinition.ARROW_LEFT.createIconImageView());
		
		setupListCells();
	}

	private void setupListCells() {
		getLstAvailablePlayers().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		getLstAvailablePlayers().setCellFactory(TextFieldListCell.forListView(new StringConverter<PlayerDTOViewModel>() {

			@Override
			public String toString(PlayerDTOViewModel object) {
				return object.getLabel();
			}

			@Override
			public PlayerDTOViewModel fromString(String string) {
				return null;
			}
		}));
		
		getLstPlayersSelectedForTournament().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		getLstPlayersSelectedForTournament().setCellFactory(TextFieldListCell.forListView(new StringConverter<PlayerDTOViewModel>() {

			@Override
			public String toString(PlayerDTOViewModel object) {
				return object.getLabel();
			}

			@Override
			public PlayerDTOViewModel fromString(String string) {
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
	public void doAfterInitializationCompleted(Object payload) {
		getVm().getAvailablePlayers().clear();
		getVm().getPlayersForTournament().clear();
		
		startBackgroundTask(loadAvailablePlayersTask());
		startBackgroundTask(loadDefaultSettingsTask());
	}


	private BackgroundTask loadAvailablePlayersTask() {
		return new BackgroundTask<List<PlayerDTOViewModel>>() {

			@Override
			public List<PlayerDTOViewModel> performTask() throws Exception {
				return getVm().loadAllPlayer();
			}

			@Override
			public void doOnSuccess(List<PlayerDTOViewModel> result) {
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
		openDialog(DialogContentDefinition.TOURNAMENT_SETTINGS_DIALOG, getVm().getSettingsVm(), new DialogCloseEvent<TournamentSettingsViewModel>() {

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
	public ModelValidationResult validateBeforeClose() {
		return getVm().validate();
	}

	@Override
	public TournamentConfigurationViewModel sendResult() {
		return getVm();
	}
}
