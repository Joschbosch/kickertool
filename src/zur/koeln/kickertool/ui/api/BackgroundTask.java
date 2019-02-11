package zur.koeln.kickertool.ui.api;

public interface BackgroundTask<T> {
	
	T performTask() throws Exception;
	
	void doOnSuccess(T result);
	
	void doOnFailure(Throwable exception);
	
}
