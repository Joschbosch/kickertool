package zur.koeln.kickertool.ui.cells;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableRow;

/**
 * Helper interface. Is needed if you need to respond to a click inside of a {@link TableCell}. With the rowIndex
 * you can then receive the object inside the {@link TableRow}
 * 
 * @author Daniel Cleemann
 */
public interface TableCellClickEvent {
	
	void doOnClick(int rowIndex);
	
}
