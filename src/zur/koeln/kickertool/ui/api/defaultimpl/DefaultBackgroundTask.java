package zur.koeln.kickertool.ui.api.defaultimpl;

import zur.koeln.kickertool.ui.api.BackgroundTask;

public class DefaultBackgroundTask implements BackgroundTask<Object>{

	@Override
	public Object performTask() {
		// nothing to do
		return null;
	}

	@Override
	public void doOnSucceed(Object result) {
		// nothing to do
	}

	@Override
	public void doOnFailure() {
		// nothing to do
	}

}
