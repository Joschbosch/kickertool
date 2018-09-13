package zur.koeln.kickertool.api.ui;

import org.springframework.context.ConfigurableApplicationContext;

import javafx.scene.Parent;
import zur.koeln.kickertool.api.ToolState;

public interface GUIController {

    public void init(ConfigurableApplicationContext ctx, Parent rootPane, double width, double height);

    public void switchToolState(ToolState newState);
    
    public void update();
}
