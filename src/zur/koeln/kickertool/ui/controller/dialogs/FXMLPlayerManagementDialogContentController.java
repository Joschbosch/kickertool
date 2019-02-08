package zur.koeln.kickertool.ui.controller.dialogs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXListView;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.application.api.dtos.PlayerDTO;
import zur.koeln.kickertool.application.handler.api.IPlayerCommandHandler;
import zur.koeln.kickertool.ui.api.BackgroundTask;
import zur.koeln.kickertool.ui.controller.AbstractFXMLController;

@Component
@Getter(value=AccessLevel.PRIVATE)
public class FXMLPlayerManagementDialogContentController extends AbstractFXMLController {
	
	@Autowired 
	IPlayerCommandHandler playerCommandHandler;
	
	@FXML 
	JFXListView lstViewPlayers;
	
	@Override
	public void doAfterInitializationCompleted() {
		startBackgroundTask(loadPlayerList());
	}

	private BackgroundTask loadPlayerList() {
		return new BackgroundTask<List<PlayerDTO>>() {

			@Override
			public List<PlayerDTO> performTask() throws Exception{
				return getPlayerCommandHandler().getAllPlayer();
			}

			@Override
			public void doOnSucceed(List<PlayerDTO> result) {
				getLstViewPlayers().setItems(FXCollections.observableList(result));
			}

			@Override
			public void doOnFailure(Throwable exception) {
				System.out.println(exception.getMessage());
			}

		};
	}
	
}
