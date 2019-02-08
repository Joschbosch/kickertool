package zur.koeln.kickertool.ui.api;

public interface BackgroundTask<T> {
	
	T performTask() throws Exception;
	
	void doOnSucceed(T result);
	
	void doOnFailure(Throwable exception);
	
}
