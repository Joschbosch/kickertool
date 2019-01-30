package zur.koeln.kickertool.deprecated.uifxml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.context.ConfigurableApplicationContext;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import zur.koeln.kickertool.ToolState;
import zur.koeln.kickertool.api.ui.GUIController;

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
		getSecondaryStage().setTitle("parcIT Kickerturnier Helferlein");
        getSecondaryStage().getIcons().add(new Image(this.getClass().getResource("/images/icon.png").toString()));

		FXMLLoader loader = getFXMLLoader(newState);
		Pane rootPane = (Pane) loader.load();
		prepareStage(getSecondaryStage(), rootPane);
	}

	private FXMLLoader getFXMLLoader(ToolState newState) throws IOException {
		FXMLLoader loader = null;
		switch (newState) {
		case MAIN_MENU:
			loader = new FXMLLoader(getClass().getResource(FXMLGUI.MAIN_MENU.getFxmlFile()));
			break;
		case NEW_TOURNAMENT_CONFIG:
			loader = new FXMLLoader(getClass().getResource(FXMLGUI.TOURNAMENT_CONFIGURATION.getFxmlFile()));
			break;
		case PLAYER_CONFIG:
			loader = new FXMLLoader(getClass().getResource(FXMLGUI.PLAYER_SELECTION.getFxmlFile()));
			break;
		case PLAYER_POOL:
			loader = new FXMLLoader(getClass().getResource(FXMLGUI.PLAYER_POOL_MANAGEMENT.getFxmlFile()));
			break;
		case TOURNAMENT:
			loader = new FXMLLoader(getClass().getResource(FXMLGUI.TOURMANENT.getFxmlFile()));
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
        stage.setTitle("parcIT Kickerturnier Helferlein");
        stage.getIcons().add(new Image(this.getClass().getResource("/images/icon.png").toString()));
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
