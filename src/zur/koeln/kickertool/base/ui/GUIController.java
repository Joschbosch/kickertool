package zur.koeln.kickertool.base.ui;

import org.springframework.context.ConfigurableApplicationContext;

import javafx.scene.Parent;
import zur.koeln.kickertool.base.ToolState;

public interface GUIController {

    public void init(ConfigurableApplicationContext ctx, Parent rootPane, double width, double height);

    public void switchToolState(ToolState newState);
    
    public void update();
}
