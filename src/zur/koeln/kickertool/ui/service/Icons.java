package zur.koeln.kickertool.ui.service;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.AccessLevel;
import lombok.Getter;


@Getter(value=AccessLevel.PRIVATE)
@SuppressWarnings("nls")
public enum Icons {
	
	ADD_ITEM("add_item"),
	DELETE_ITEM("delete_item"),
	EDIT_ITEM("edit_item");
	
	private String fileName;

	private Icons(String path) {
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
