package zur.koeln.kickertool.ui.api;

public interface BackgroundTask<Result> {
	
	Result performTask() throws Exception;
	
	void doOnSuccess(Result result);
	
	void doOnFailure(Throwable exception);
	
}
