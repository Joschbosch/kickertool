package zur.koeln.kickertool.ui.controller.dialogs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.util.Callback;
import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.ui.api.BackgroundTask;
import zur.koeln.kickertool.ui.controller.AbstractFXMLController;
import zur.koeln.kickertool.ui.vm.PlayerManagementViewModel;
import zur.koeln.kickertool.ui.vm.PlayerViewModel;

@Component
@Getter(value=AccessLevel.PRIVATE)
public class FXMLPlayerManagementDialogContentController extends AbstractFXMLController {
	
	@Autowired
	PlayerManagementViewModel vm;
	
	@FXML JFXTreeTableView tblPlayers;

	@FXML JFXTreeTableColumn tblColFirstName;

	@FXML JFXTreeTableColumn tblColLastName;
	
	@Override
	public void initialize() {
		super.initialize();
		initTableColumns();
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
	}

	@Override
	public void doAfterInitializationCompleted() {
		startBackgroundTask(loadPlayerList());
	}

	private BackgroundTask loadPlayerList() {
		return new BackgroundTask<Void>() {

			@Override
			public Void performTask() throws Exception{
				return getVm().loadPlayersToList();
			}

			@Override
			public void doOnSucceed(Void result) {
				getTblPlayers().setRoot(new RecursiveTreeItem<PlayerViewModel>(getVm().getPlayers(), RecursiveTreeObject::getChildren));
			}

			@Override
			public void doOnFailure(Throwable exception) {
				System.out.println(exception.getMessage());
			}

		};
	}
	
}
