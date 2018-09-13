package zur.koeln.kickertool.uifxml.cells;

import javafx.scene.control.ListCell;
import zur.koeln.kickertool.base.HumanPlayer;

public class PlayerListCell extends ListCell<HumanPlayer> {
	
	@Override
	protected void updateItem(HumanPlayer item, boolean empty) {

		super.updateItem(item, empty);
		setText(null);
			
		if (item != null && !empty) {
			setText(item.getName());
		}
		
	}
	
	
}
