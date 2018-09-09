package zur.koeln.kickertool.uifxml;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import zur.koeln.kickertool.base.AbstractGUIController;
import zur.koeln.kickertool.ui.GUIState;

@Getter(value=AccessLevel.PRIVATE)
@Setter(value=AccessLevel.PRIVATE)
public class FXMLGUIController extends AbstractGUIController {

	private Stage secondaryStage;
	
	public FXMLGUIController(Stage stage, GUIState state) {
		super(stage, state);
	}

	@Override
	public void switchStateTo(GUIState newState) {

		try {

			FXMLLoader loader = getFXMLLoader(newState);
			Pane newPane = (Pane) loader.load();
			prepareStage(getStage(), newPane);
			
			if (newState == GUIState.TOURNAMENT) {
				createSecondTournamentStage(newState);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void prepareStage(Stage stage, Parent root) {
		Scene newScene = new Scene(root);
		stage.setScene(newScene);
		stage.sizeToScene();
		stage.centerOnScreen();
		stage.show();
	}

	private void createSecondTournamentStage(GUIState newState) throws IOException {
		setSecondaryStage(new Stage());
		getSecondaryStage().setTitle("Kicker APP");
		FXMLLoader loader = getFXMLLoader(newState);
		Pane rootPane = (Pane) loader.load();
		
		FXMLTournamentController tournamentController = loader.getController();
		tournamentController.hideButtons();
		
		prepareStage(getSecondaryStage(), rootPane);
	}

	private FXMLLoader getFXMLLoader(GUIState newState) throws IOException {
		FXMLLoader loader = null;
		switch (newState) {
		case MAIN_MENU:
			loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
			break;
		case NEW_TOURNAMENT_CONFIG:
			loader = new FXMLLoader(getClass().getResource("TournamentConfiguration.fxml"));
			break;
		case PLAYER_CONFIG:
			loader = new FXMLLoader(getClass().getResource("PlayerSelection.fxml"));
			break;
		case PLAYER_POOL:
			loader = new FXMLLoader(getClass().getResource("PlayerPoolManagement.fxml"));
			break;
		case TOURNAMENT:
			loader = new FXMLLoader(getClass().getResource("Tournament.fxml"));
			break;
		default:
			return null;
		}
		loader.setControllerFactory(getCtx()::getBean);
		
		return loader;
	}
	
	@Override
	public void update() {
		//
	}

}
