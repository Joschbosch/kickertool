package zur.koeln.kickertool.ui.service;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.AccessLevel;
import lombok.Getter;


@Getter(value=AccessLevel.PRIVATE)
@SuppressWarnings("nls")
public enum Icons {
	
	ADD_ITEM("add_item"),
	DELETE_ITEM("delete_item");
	
	private String fileName;

	private Icons(String path) {
		this.fileName = path;
	}
	
	public ImageView createIconImageView() {
		
		ImageView imgView = new ImageView(new Image("images/" + getFileName() + ".png"));
		imgView.setFitWidth(25);
		imgView.setFitHeight(25);
		return imgView;
	}
	
}
