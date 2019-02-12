package zur.koeln.kickertool.ui.vm;

import org.springframework.stereotype.Component;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import zur.koeln.kickertool.ui.vm.base.FXViewModel;
import zur.koeln.kickertool.ui.vm.base.ModelValidationResult;

@Component
public class PlayerNameEditViewModel extends FXViewModel {

	@Getter
	private final StringProperty firstNameProperty = new SimpleStringProperty();
	@Getter
	private final StringProperty lastNameProperty = new SimpleStringProperty();
	

	public String getFirstName() {
		return getFirstNameProperty().get().trim();
	}
	public void setFirstName(String firstName) {
		getFirstNameProperty().setValue(firstName);
	}
	public String getLastName() {
		return getLastNameProperty().get().trim();
	}
	public void setLastName(String lastName) {
		getLastNameProperty().setValue(lastName);
	}
	
	@Override
	public ModelValidationResult validate() {
		
		ModelValidationResult valResult = ModelValidationResult.empty();
		
		if (getFirstName().trim().isEmpty()) {
			valResult.addValidationMessage("Bitte gib einen Vornamen ein.");
		}
		
		if (getLastName().trim().isEmpty()) {
			valResult.addValidationMessage("Bitte gib einen Nachnamen ein.");
		}
		
		return valResult;
	}
	
}
