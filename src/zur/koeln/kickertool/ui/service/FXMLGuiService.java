package zur.koeln.kickertool.ui.service;

import java.io.IOException;

import org.springframework.context.ConfigurableApplicationContext;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;


public class FXMLGuiService {

	private static FXMLGuiService instance;
	
	public static FXMLGuiService getInstance() {
		
		if (instance == null) {
			instance = new FXMLGuiService();
		}
		
		return instance;
	}
	
	@Getter(value=AccessLevel.PRIVATE)
	@Setter(value=AccessLevel.PRIVATE)
	private ConfigurableApplicationContext ctx;
	
	@Getter(value=AccessLevel.PRIVATE)
	@Setter(value=AccessLevel.PRIVATE)
	private Stage primaryStage;
	
	private static final String PREFIX = "../"; //$NON-NLS-1$
	
	public void initialize(ConfigurableApplicationContext ctx, Stage primaryStage) {
		setCtx(ctx);
		setPrimaryStage(primaryStage);
	}
	
	public void switchScene(Scenes newScene) {
		
		try {
			FXMLLoader fxmlLoader = getFXMLLoader(newScene);
			Pane pane = fxmlLoader.load();
			prepareStage(getPrimaryStage(), pane);
		} catch (IOException e) {
			// Should not be thrown
			throw new IllegalStateException(e);
		}
	}
	
	private void prepareStage(Stage stage, Pane pane) {
		Scene newScene = new Scene(pane);
		stage.setScene(newScene);
		stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
	}

	private FXMLLoader getFXMLLoader(Scenes scene) {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource(PREFIX + scene.getFxmlFile()));
		loader.setControllerFactory(getCtx()::getBean);
		return loader;
		
	}
}
