package zur.koeln.kickertool.ui.api;

public interface BackgroundTask<T> {
	
	T performTask();
	
	void doOnSucceed(T result);
	
	void doOnFailure();
	
}
