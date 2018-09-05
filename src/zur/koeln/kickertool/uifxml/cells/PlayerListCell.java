package zur.koeln.kickertool.uifxml.cells;

import javafx.scene.control.ListCell;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import zur.koeln.kickertool.player.Player;

public class PlayerListCell extends ListCell<Player> {
	
	@Override
	protected void updateItem(Player item, boolean empty) {

		super.updateItem(item, empty);
		setText(null);
			
		if (item != null && !empty) {
			setText(item.getName());
		}
		
	}
	
	
}
