package zur.koeln.kickertool.ui.api.defaultimpl;

import zur.koeln.kickertool.ui.api.events.DialogCloseEvent;

public class DefaultDialogCloseEvent implements DialogCloseEvent<Void>{

	@Override
	public void doAfterDialogClosed(Void result) {
		// nothing to do	
	}


}
