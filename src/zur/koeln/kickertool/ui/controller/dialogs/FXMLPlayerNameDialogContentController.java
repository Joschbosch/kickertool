package zur.koeln.kickertool.ui.controller.dialogs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.ui.api.IFXMLDialogContent;
import zur.koeln.kickertool.ui.controller.AbstractFXMLController;
import zur.koeln.kickertool.ui.vm.PlayerNameEditViewModel;
import zur.koeln.kickertool.ui.vm.base.ModelValidationResult;

@Component
@Getter(value=AccessLevel.PRIVATE)
public class FXMLPlayerNameDialogContentController extends AbstractFXMLController implements IFXMLDialogContent<PlayerNameEditViewModel>{

	@Autowired
	PlayerNameEditViewModel vm;

	@FXML
	JFXTextField txtFirstName;
	
	@FXML
	JFXTextField txtLastName;
	
	@Override
	public void setupBindings() {
		vm.getFirstNameProperty().bind(getTxtFirstName().textProperty());
		vm.getLastNameProperty().bind(getTxtLastName().textProperty());
	}

	@Override
	public PlayerNameEditViewModel sendResult() {
		return getVm();
	}
	
	@Override
	public ModelValidationResult validate() {
		
		return getVm().validate();
	}

}
