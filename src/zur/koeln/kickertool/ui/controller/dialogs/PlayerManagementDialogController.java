package zur.koeln.kickertool.ui.controller.dialogs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXButton;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.ui.cells.ImageEditTableCellFactory;
import zur.koeln.kickertool.ui.cells.TableCellClickEvent;
import zur.koeln.kickertool.ui.controller.base.AbstractController;
import zur.koeln.kickertool.ui.controller.base.BackgroundTask;
import zur.koeln.kickertool.ui.controller.base.DialogCloseEvent;
import zur.koeln.kickertool.ui.controller.base.DialogConfirmationResponse;
import zur.koeln.kickertool.ui.controller.base.DialogContent;
import zur.koeln.kickertool.ui.controller.base.impl.IDialogConfirmationCloseEvent;
import zur.koeln.kickertool.ui.controller.dialogs.vms.PlayerManagementViewModel;
import zur.koeln.kickertool.ui.controller.shared.vms.PlayerDTOViewModel;
import zur.koeln.kickertool.ui.shared.DialogContentDefinition;
import zur.koeln.kickertool.ui.shared.IconDefinition;

@Component
@Getter(value=AccessLevel.PRIVATE)
@SuppressWarnings("nls")
public class PlayerManagementDialogController extends AbstractController implements DialogContent<Void, Void> {
	
	@Autowired
	PlayerManagementViewModel vm;
	
	@FXML TableView tblPlayers;

	@FXML TableColumn tblColEdit;
	
	@FXML TableColumn tblColFirstName;

	@FXML TableColumn tblColLastName;

	@FXML JFXButton btnAddPlayer;

	@FXML JFXButton btnDeletePlayer;

	@Override
	public void setupControls() {
		
		getTblPlayers().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		setupButtons();
		initTableColumns();
	}

	private void setupButtons() {
		getBtnAddPlayer().setGraphic(IconDefinition.ADD_ITEM.createIconImageView());
		getBtnDeletePlayer().setGraphic(IconDefinition.DELETE_ITEM.createIconImageView());
	}
	
	private void initTableColumns() {
		
		getTblColFirstName().setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PlayerDTOViewModel, String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<PlayerDTOViewModel, String> param) {
				
				return param.getValue().getFirstNameProperty();
			}
		});
		
		getTblColLastName().setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PlayerDTOViewModel, String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<PlayerDTOViewModel, String> param) {
				
				return param.getValue().getLastNameProperty();
			}
		});
		
		getTblColEdit().setCellFactory(new ImageEditTableCellFactory(new TableCellClickEvent() {

			@Override
			public void doOnClick(int rowIndex) {
				
				openDialog(DialogContentDefinition.PLAYER_NAME_DIALOG, getVm().getPlayers().get(rowIndex), new DialogCloseEvent<PlayerDTOViewModel>() {

					@Override
					public void doAfterDialogClosed(PlayerDTOViewModel result) {
						startBackgroundTask(updatePlayerTask(result));
					}
				});
				
			}
		}));
	}
	
	@Override
	public void setupBindings() {
		getBtnDeletePlayer().disableProperty().bind(Bindings.size(getTblPlayers().getSelectionModel().getSelectedItems()).isEqualTo(0));
	}
	
	@Override
	public void doAfterInitializationCompleted(Object payload) {
		startBackgroundTask(loadPlayerListTask());
	}

	private BackgroundTask loadPlayerListTask() {
		return new BackgroundTask<List<PlayerDTOViewModel>>() {

			@Override
			public List<PlayerDTOViewModel> performTask() throws Exception{
				return getVm().loadAllPlayer();
			}

			@Override
			public void doOnSuccess(List<PlayerDTOViewModel> result) {
				getVm().getPlayers().clear();
				getVm().getPlayers().addAll(result);
				getTblPlayers().setItems(getVm().getPlayers());
			}

			@Override
			public void doOnFailure(Throwable exception) {
				showError(exception);
			}

		};
	}
	
	private BackgroundTask updatePlayerTask(PlayerDTOViewModel playerViewModel) {
		return new BackgroundTask<PlayerDTOViewModel>() {

			@Override
			public PlayerDTOViewModel performTask() throws Exception {
				
				return getVm().updatePlayer(playerViewModel);
			}

			@Override
			public void doOnSuccess(PlayerDTOViewModel result) {
				getVm().updatePlayersList(result);
			}

			@Override
			public void doOnFailure(Throwable exception) {
				showError(exception);
				startBackgroundTask(loadPlayerListTask());
			}
		};
	}

	@FXML public void onAddPlayerClicked() {
		openDialog(DialogContentDefinition.PLAYER_NAME_DIALOG, new DialogCloseEvent<PlayerDTOViewModel>() {

			@Override
			public void doAfterDialogClosed(PlayerDTOViewModel result) {
				startBackgroundTask(insertNewPlayerTask(result));
			}

		});
	}
	
	private BackgroundTask insertNewPlayerTask(PlayerDTOViewModel newPlayerNameViewModel) {
		
		return new BackgroundTask<PlayerDTOViewModel>() {

			@Override
			public PlayerDTOViewModel performTask() throws Exception {
				
				return getVm().insertNewPlayer(newPlayerNameViewModel.getFirstName(), newPlayerNameViewModel.getLastName());
			}

			@Override
			public void doOnSuccess(PlayerDTOViewModel result) {
				getVm().getPlayers().add(result);
			}

			@Override
			public void doOnFailure(Throwable exception) {
				showError(exception);
			}
		};
	}

	@FXML 
	public void onDeletePlayerClicked() {
		
		showConfirmationDialog("Spieler löschen?", "Wollen Sie folgende Spieler wirklich löschen?", getTblPlayers().getSelectionModel().getSelectedItems(), new IDialogConfirmationCloseEvent() {
			
			@Override
			public void doAfterDialogClosed(DialogConfirmationResponse response) {
				if (response.isAccepted()) {
					ObservableList<PlayerDTOViewModel> selectedItems = getTblPlayers().getSelectionModel().getSelectedItems();

					startBackgroundTask(deletePlayerTask(selectedItems));
				} 
				
			}
		});
		
	}

	private BackgroundTask deletePlayerTask(List<PlayerDTOViewModel> selectedItems) {
		
		return new BackgroundTask<List<PlayerDTOViewModel>>() {

			@Override
			public List<PlayerDTOViewModel> performTask() throws Exception {
				return getVm().deletePlayer(selectedItems);
			}

			@Override
			public void doOnSuccess(List<PlayerDTOViewModel> result) {
				getVm().getPlayers().removeAll(result);
			}

			@Override
			public void doOnFailure(Throwable exception) {
				showError(exception);
			}
		};
	}

}
