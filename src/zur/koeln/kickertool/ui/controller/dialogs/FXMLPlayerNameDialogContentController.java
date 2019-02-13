package zur.koeln.kickertool.ui.controller.dialogs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.core.kernl.utils.CustomModelMapper;
import zur.koeln.kickertool.ui.api.IFXMLDialogContent;
import zur.koeln.kickertool.ui.controller.base.AbstractFXMLController;
import zur.koeln.kickertool.ui.vm.PlayerViewModel;
import zur.koeln.kickertool.ui.vm.base.ModelValidationResult;

@Component
@Getter(value=AccessLevel.PRIVATE)
public class FXMLPlayerNameDialogContentController extends AbstractFXMLController implements IFXMLDialogContent<PlayerViewModel, PlayerViewModel>{

	@Autowired
	PlayerViewModel vm;

	@Autowired
	CustomModelMapper modelMapper;
	
	@FXML
	JFXTextField txtFirstName;
	
	@FXML
	JFXTextField txtLastName;
	
	@Override
	public void setupBindings() {
		vm.getFirstNameProperty().bindBidirectional(getTxtFirstName().textProperty());
		vm.getLastNameProperty().bindBidirectional(getTxtLastName().textProperty());
	}

	@Override
	public void doAfterInitializationCompleted() {
		getTxtFirstName().requestFocus();
	}
	
	@Override
	public PlayerViewModel sendResult() {
		return getVm();
	}
	
	@Override
	public ModelValidationResult validate() {
		
		return getVm().validate();
	}

	@Override
	public void initContent(PlayerViewModel content) {
		getVm().setFirstName(content.getFirstName());
		getVm().setLastName(content.getLastName());
		getVm().setUid(content.getUid());
	}
}
