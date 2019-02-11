package zur.koeln.kickertool.ui.api.defaultimpl;

import zur.koeln.kickertool.ui.api.BackgroundTask;

public class DefaultBackgroundTask implements BackgroundTask<Void>{

	@Override
	public Void performTask() {
		// nothing to do
		return null;
	}

	@Override
	public void doOnSuccess(Void result) {
		// nothing to do
	}
	
	@Override
	public void doOnFailure(Throwable exception) {
		//
	}

}
