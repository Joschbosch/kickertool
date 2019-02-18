package zur.koeln.kickertool.ui.cells;

import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.ui.api.events.TableCellClickEvent;
import zur.koeln.kickertool.ui.service.Icons;

@Getter(value=AccessLevel.PRIVATE)
public class ImageEditTableCellFactory implements Callback<TableColumn, TableCell>{

	private final TableCellClickEvent click;
	
	public ImageEditTableCellFactory(TableCellClickEvent click) {
		super();
		this.click = click;
	}

	@Override
	public TableCell call(TableColumn p) {
		
		return new TableCell() {
			
			private final VBox vbox;
			private final ImageView imageView;
			
			{
				vbox = new VBox();
				vbox.setAlignment(Pos.CENTER);
				imageView = Icons.EDIT_ITEM.createIconImageView(20);
				vbox.getChildren().add(imageView);
				vbox.setStyle("-fx-cursor: hand;"); //$NON-NLS-1$
				setGraphic(vbox);
				
				vbox.setOnMouseClicked(event -> click.doOnClick(getIndex()));
			}
			
			@Override
			protected void updateItem(Object item, boolean empty) {
				super.updateItem(item, empty);
				
				imageView.setVisible(!empty);
			}
			
		};
		
	}

}
