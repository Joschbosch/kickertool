package zur.koeln.kickertool.ui.vm;

import java.util.UUID;

import org.springframework.stereotype.Component;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;
import zur.koeln.kickertool.ui.vm.base.FXViewModel;
import zur.koeln.kickertool.ui.vm.base.ILabel;
import zur.koeln.kickertool.ui.vm.base.ModelValidationResult;

@Component
@Getter
public class PlayerViewModel extends FXViewModel implements ILabel {
	
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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uid == null) ? 0 : uid.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PlayerViewModel other = (PlayerViewModel) obj;
		if (uid == null) {
			if (other.uid != null)
				return false;
		} else if (!uid.equals(other.uid))
			return false;
		return true;
	}
	@Override
	public String getLabel() {
		
		return getFirstName() + " " + getLastName(); 
	}
	
}	
