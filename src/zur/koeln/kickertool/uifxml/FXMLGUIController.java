package zur.koeln.kickertool.uifxml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.context.ConfigurableApplicationContext;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import zur.koeln.kickertool.base.ToolState;
import zur.koeln.kickertool.base.ui.GUIController;

@Getter(value=AccessLevel.PRIVATE)
@Setter(value=AccessLevel.PRIVATE)
public class FXMLGUIController
    implements GUIController {

	private Stage secondaryStage;
	
    @NonNull
    private final Stage stage;

    @NonNull
    private final ToolState state;

    private ConfigurableApplicationContext ctx;

    private final List<UpdateableUIComponent> updateListener = new ArrayList<>();

	public FXMLGUIController(Stage stage, ToolState state) {
        this.stage = stage;
        this.state = state;
	}

	@Override
    public void switchToolState(ToolState newState) {

		try {

			FXMLLoader loader = getFXMLLoader(newState);
			Pane newPane = (Pane) loader.load();
            registerGUIUpdateComponents((UpdateableUIComponent) loader.getController());
			prepareStage(getStage(), newPane);
			
			if (newState == ToolState.TOURNAMENT) {
				//createSecondTournamentStage(newState);
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

	private void createSecondTournamentStage(ToolState newState) throws IOException {
		setSecondaryStage(new Stage());
		getSecondaryStage().setTitle("Kicker APP");
		FXMLLoader loader = getFXMLLoader(newState);
		Pane rootPane = (Pane) loader.load();
		
		prepareStage(getSecondaryStage(), rootPane);
	}

	@SuppressWarnings("nls")
	private FXMLLoader getFXMLLoader(ToolState newState) throws IOException {
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

    @SuppressWarnings("nls")
	@PostConstruct
    public void init(ConfigurableApplicationContext ctx, Parent rootPane, double width, double height) {
        this.ctx = ctx;
        stage.setTitle("Kicker APP");
        Scene mainScene = new Scene(rootPane, width, height);
        stage.setScene(mainScene);
        stage.show();

    }

    public void unregisterGUIUpdateComponents(UpdateableUIComponent oldComponent) {
        updateListener.remove(oldComponent);
    }

    public void registerGUIUpdateComponents(UpdateableUIComponent newComponent) {
        updateListener.add(newComponent);
    }

    @Override
    public void update() {
        updateListener.forEach(UpdateableUIComponent::update);
    }

}
