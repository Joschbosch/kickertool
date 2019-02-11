package zur.koeln.kickertool.ui.vm;

import java.util.UUID;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;

public class PlayerViewModel extends RecursiveTreeObject<PlayerViewModel>{

	private UUID id;
	
	@Getter
	private final StringProperty firstNameProperty = new SimpleStringProperty();
	@Getter
	private final StringProperty lastNameProperty = new SimpleStringProperty();
	
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
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
