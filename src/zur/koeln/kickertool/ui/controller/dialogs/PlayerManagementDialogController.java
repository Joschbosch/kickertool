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
import zur.koeln.kickertool.ui.api.BackgroundTask;
import zur.koeln.kickertool.ui.api.FXMLDialogContent;
import zur.koeln.kickertool.ui.api.events.DialogCloseEvent;
import zur.koeln.kickertool.ui.api.events.TableCellClickEvent;
import zur.koeln.kickertool.ui.cells.ImageEditTableCellFactory;
import zur.koeln.kickertool.ui.controller.base.AbstractFXMLController;
import zur.koeln.kickertool.ui.controller.vms.PlayerManagementViewModel;
import zur.koeln.kickertool.ui.controller.vms.PlayerViewModel;
import zur.koeln.kickertool.ui.shared.DialogContent;
import zur.koeln.kickertool.ui.shared.Icons;

@Component
@Getter(value=AccessLevel.PRIVATE)
@SuppressWarnings("nls")
public class PlayerManagementDialogController extends AbstractFXMLController implements FXMLDialogContent<Void, Void> {
	
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
		getBtnAddPlayer().setGraphic(Icons.ADD_ITEM.createIconImageView());
		getBtnDeletePlayer().setGraphic(Icons.DELETE_ITEM.createIconImageView());
	}
	
	private void initTableColumns() {
		
		getTblColFirstName().setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PlayerViewModel, String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<PlayerViewModel, String> param) {
				
				return param.getValue().getFirstNameProperty();
			}
		});
		
		getTblColLastName().setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PlayerViewModel, String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<PlayerViewModel, String> param) {
				
				return param.getValue().getLastNameProperty();
			}
		});
		
		getTblColEdit().setCellFactory(new ImageEditTableCellFactory(new TableCellClickEvent() {

			@Override
			public void doOnClick(int rowIndex) {
				
				openDialog(DialogContent.PLAYER_NAME_DIALOG, getVm().getPlayers().get(rowIndex), new DialogCloseEvent<PlayerViewModel>() {

					@Override
					public void doAfterDialogClosed(PlayerViewModel result) {
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
	public void doAfterInitializationCompleted() {
		startBackgroundTask(loadPlayerListTask());
	}

	private BackgroundTask loadPlayerListTask() {
		return new BackgroundTask<List<PlayerViewModel>>() {

			@Override
			public List<PlayerViewModel> performTask() throws Exception{
				return getVm().loadAllPlayer();
			}

			@Override
			public void doOnSuccess(List<PlayerViewModel> result) {
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
	
	private BackgroundTask updatePlayerTask(PlayerViewModel playerViewModel) {
		return new BackgroundTask<PlayerViewModel>() {

			@Override
			public PlayerViewModel performTask() throws Exception {
				
				return getVm().updatePlayer(playerViewModel);
			}

			@Override
			public void doOnSuccess(PlayerViewModel result) {
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
		openDialog(DialogContent.PLAYER_NAME_DIALOG, new DialogCloseEvent<PlayerViewModel>() {

			@Override
			public void doAfterDialogClosed(PlayerViewModel result) {
				startBackgroundTask(insertNewPlayerTask(result));
			}

		});
	}
	
	private BackgroundTask insertNewPlayerTask(PlayerViewModel newPlayerNameViewModel) {
		
		return new BackgroundTask<PlayerViewModel>() {

			@Override
			public PlayerViewModel performTask() throws Exception {
				
				return getVm().insertNewPlayer(newPlayerNameViewModel.getFirstName(), newPlayerNameViewModel.getLastName());
			}

			@Override
			public void doOnSuccess(PlayerViewModel result) {
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
		
		showConfirmationDialog("Spieler löschen?", "Wollen Sie folgende Spieler wirklich löschen?", getTblPlayers().getSelectionModel().getSelectedItems(), new DialogCloseEvent<Boolean>() {
			
			@Override
			public void doAfterDialogClosed(Boolean result) {
				if (result.booleanValue()) {
					ObservableList<PlayerViewModel> selectedItems = getTblPlayers().getSelectionModel().getSelectedItems();

					startBackgroundTask(deletePlayerTask(selectedItems));
				} 
			}
		});
		
	}

	private BackgroundTask deletePlayerTask(List<PlayerViewModel> selectedItems) {
		
		return new BackgroundTask<List<PlayerViewModel>>() {

			@Override
			public List<PlayerViewModel> performTask() throws Exception {
				return getVm().deletePlayer(selectedItems);
			}

			@Override
			public void doOnSuccess(List<PlayerViewModel> result) {
				getVm().getPlayers().removeAll(result);
			}

			@Override
			public void doOnFailure(Throwable exception) {
				showError(exception);
			}
		};
	}

}
