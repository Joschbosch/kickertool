package zur.koeln.kickertool.ui.service;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.ui.controller.base.AbstractController;
import zur.koeln.kickertool.ui.controller.base.Controller;
import zur.koeln.kickertool.ui.shared.GUIEvents;

@Getter(value = AccessLevel.PRIVATE)
public class GUIEventService {
	
	private static GUIEventService instance;

	public static GUIEventService getInstance() {

		if (instance == null) {
			instance = new GUIEventService();
		}

		return instance;
	}
	
	private final List<AbstractController> registeredController = new ArrayList<>();

	public void addControllerToRegister(AbstractController newController) {
		getRegisteredController().add(newController);
	}
	
	public void removeControllerFromRegister(AbstractController controller) {
		getRegisteredController().remove(controller);
	}
	
	public void fireEvent(GUIEvents guiEvent, Object content) {
		getRegisteredController().stream().forEach(eController -> {
			if (eController.hasEventRegistered(guiEvent)) {
				eController.handleEvent(guiEvent, content);
			}
		});
	}
}
