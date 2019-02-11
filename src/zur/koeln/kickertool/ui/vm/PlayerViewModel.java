package zur.koeln.kickertool.ui.vm;

import java.util.UUID;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;

public class PlayerViewModel extends RecursiveTreeObject<PlayerViewModel>{
	
	@Getter
	@Setter
	private UUID uid;
	
	@Getter
	private final StringProperty firstNameProperty = new SimpleStringProperty();
	@Getter
	private final StringProperty lastNameProperty = new SimpleStringProperty();
	

	public String getFirstName() {
		return getFirstNameProperty().get();
	}
	public void setFirstName(String firstName) {
		getFirstNameProperty().setValue(firstName);
	}
	public String getLastName() {
		return getLastNameProperty().get();
	}
	public void setLastName(String lastName) {
		getLastNameProperty().setValue(lastName);
	}
	
}	
