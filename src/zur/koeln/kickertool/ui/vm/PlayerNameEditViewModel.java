package zur.koeln.kickertool.ui.vm;

import org.springframework.stereotype.Component;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;

@Component
public class PlayerNameEditViewModel {

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
