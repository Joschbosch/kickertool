package zur.koeln.kickertool.deprecated.uifxml.cells;

import javafx.scene.control.ListCell;
import zur.koeln.kickertool.core.model.Player;

public class PlayerListCell
    extends ListCell<Player> {
	
	@Override
    protected void updateItem(Player item, boolean empty) {

		super.updateItem(item, empty);
		setText(null);
			
		if (item != null && !empty) {
			setText(item.getName());
		}
		
	}
	
	
}
