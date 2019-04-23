package zur.koeln.kickertool.ui.controller.dialogs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.core.kernl.utils.CustomModelMapper;
import zur.koeln.kickertool.ui.controller.base.AbstractController;
import zur.koeln.kickertool.ui.controller.base.DialogContent;
import zur.koeln.kickertool.ui.controller.base.vm.ModelValidationResult;
import zur.koeln.kickertool.ui.controller.shared.vms.PlayerDTOViewModel;
import zur.koeln.kickertool.ui.shared.GUIEvents;

@Component
@Getter(value=AccessLevel.PRIVATE)
public class PlayerNameEditDialogController extends AbstractController implements DialogContent<PlayerDTOViewModel, PlayerDTOViewModel>{

	@Autowired
	PlayerDTOViewModel vm;

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
	public void doAfterInitializationCompleted(Object payload) {
		getTxtFirstName().requestFocus();
	}
	
	@Override
	public PlayerDTOViewModel sendResult() {
		return getVm();
	}
	
	@Override
	public ModelValidationResult validateBeforeClose() {
		
		return getVm().validate();
	}

	@Override
	public void initializeDialogWithContent(PlayerDTOViewModel content) {
		getVm().setFirstName(content.getFirstName());
		getVm().setLastName(content.getLastName());
		getVm().setUid(content.getUid());
	}
	
	@Override
	public void handleEvent(GUIEvents guiEvents, Object content) {
		// nothing to do here
	}

	@Override
	protected void registerEvents() {
		// nothing to do here
	} 
}
