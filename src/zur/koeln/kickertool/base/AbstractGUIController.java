package zur.koeln.kickertool.base;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.context.ConfigurableApplicationContext;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import zur.koeln.kickertool.ui.GUIState;

@RequiredArgsConstructor
@Getter(value=AccessLevel.PROTECTED)	
public abstract class AbstractGUIController {

	@NonNull
    private final Stage stage;

    @NonNull
    private final GUIState state;

    private final Map<GUIState, Pane> createdPanes = new HashMap<>();

    private ConfigurableApplicationContext ctx;

    @PostConstruct
    public void init(ConfigurableApplicationContext ctx, Parent rootPane) {
        init(ctx, rootPane, 1200, 800);
    }
    @PostConstruct
    public void init(ConfigurableApplicationContext ctx, Parent rootPane, double width, double height) {
        this.ctx = ctx;
        stage.setTitle("Kicker APP");
        Scene mainScene = new Scene(rootPane, width, height);
        stage.setScene(mainScene);
        stage.show();
        
    }
    
    public abstract void switchStateTo(GUIState newState);
    
    public abstract void update();

}
