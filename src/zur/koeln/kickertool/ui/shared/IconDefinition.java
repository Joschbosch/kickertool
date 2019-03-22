package zur.koeln.kickertool.ui.shared;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.AccessLevel;
import lombok.Getter;


@Getter(value=AccessLevel.PRIVATE)
@SuppressWarnings("nls")
public enum IconDefinition {
	
	ADD_ITEM("add_item"),
	DELETE_ITEM("delete_item"),
	EDIT_ITEM("edit_item"),
	ARROW_RIGHT("arrow_right"),
	ARROW_LEFT("arrow_left"),
	SETTINGS("settings"),
	PLAY("play"),
	PAUSE("pause"),
	RESET("reset");
	
	private String fileName;

	private IconDefinition(String path) {
		this.fileName = path;
	}
	
	public ImageView createIconImageView() {
		
		return createIconImageView(25);
	}
	
	public ImageView createIconImageView(double size) {
		
		ImageView imgView = new ImageView(new Image("images/" + getFileName() + ".png"));
		imgView.setFitWidth(size);
		imgView.setFitHeight(size);
		imgView.setStyle("-fx-cursor: hand;");
		return imgView;
	}
	
}
