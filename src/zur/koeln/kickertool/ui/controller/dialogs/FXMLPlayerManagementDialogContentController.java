package zur.koeln.kickertool.ui.controller.dialogs;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.TreeTableColumn.CellEditEvent;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.util.Callback;
import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.ui.api.BackgroundTask;
import zur.koeln.kickertool.ui.api.DialogClosedCallback;
import zur.koeln.kickertool.ui.api.IFXMLDialogContent;
import zur.koeln.kickertool.ui.controller.AbstractFXMLController;
import zur.koeln.kickertool.ui.service.DialogContent;
import zur.koeln.kickertool.ui.service.Icons;
import zur.koeln.kickertool.ui.vm.PlayerManagementViewModel;
import zur.koeln.kickertool.ui.vm.PlayerNameEditViewModel;
import zur.koeln.kickertool.ui.vm.PlayerViewModel;

@Component
@Getter(value=AccessLevel.PRIVATE)
public class FXMLPlayerManagementDialogContentController extends AbstractFXMLController implements IFXMLDialogContent<Void> {
	
	@Autowired
	PlayerManagementViewModel vm;
	
	@FXML JFXTreeTableView tblPlayers;

	@FXML TreeTableColumn tblColFirstName;

	@FXML TreeTableColumn tblColLastName;

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
		
		getTblColFirstName().setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<PlayerViewModel, String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<PlayerViewModel, String> param) {
				
				return param.getValue().getValue().getFirstNameProperty();
			}
		});
		
		
		getTblColLastName().setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<PlayerViewModel, String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<PlayerViewModel, String> param) {
				
				return param.getValue().getValue().getLastNameProperty();
			}
		});

		getTblColFirstName().setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
		getTblColFirstName().setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<PlayerViewModel, String>>() {

			@Override
			public void handle(CellEditEvent<PlayerViewModel, String> event) {
				PlayerViewModel playerViewModel = event.getRowValue().getValue();
				playerViewModel.setFirstName(event.getNewValue());
				startBackgroundTask(updatePlayerTask(playerViewModel));
			}
		});
		
		getTblColLastName().setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
		getTblColLastName().setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<PlayerViewModel, String>>() {

			@Override
			public void handle(CellEditEvent<PlayerViewModel, String> event) {
				PlayerViewModel playerViewModel = event.getRowValue().getValue();
				playerViewModel.setLastName(event.getNewValue());
				startBackgroundTask(updatePlayerTask(playerViewModel));
			}
		});
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
				getTblPlayers().setRoot(new RecursiveTreeItem<PlayerViewModel>(getVm().getPlayers(), RecursiveTreeObject::getChildren));
			}

			@Override
			public void doOnFailure(Throwable exception) {
				showError(exception);
			}

		};
	}
	
	private BackgroundTask updatePlayerTask(PlayerViewModel playerViewModel) {
		return new BackgroundTask<Void>() {

			@Override
			public Void performTask() throws Exception {
				getVm().updatePlayer(playerViewModel);
				return null;
			}

			@Override
			public void doOnSuccess(Void result) {
				//
			}

			@Override
			public void doOnFailure(Throwable exception) {
				showError(exception);
				startBackgroundTask(loadPlayerListTask());
			}
		};
	}

	@FXML public void onAddPlayerClicked() {
		openDialogue(DialogContent.PLAYER_NAME_DIALOGUE, new DialogClosedCallback<PlayerNameEditViewModel>() {

			@Override
			public void doAfterDialogClosed(PlayerNameEditViewModel result) {
				startBackgroundTask(insertNewPlayerTask(result));
			}

		});
	}
	
	private BackgroundTask insertNewPlayerTask(PlayerNameEditViewModel newPlayerNameViewModel) {
		
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
		
		ObservableList<RecursiveTreeItem<PlayerViewModel>> selectedItems = getTblPlayers().getSelectionModel().getSelectedItems();
		List<PlayerViewModel> playerViewModelsToDelete = selectedItems.stream().map(RecursiveTreeItem<PlayerViewModel>::getValue).collect(Collectors.toList());
		
		startBackgroundTask(deletePlayerTask(playerViewModelsToDelete));
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
