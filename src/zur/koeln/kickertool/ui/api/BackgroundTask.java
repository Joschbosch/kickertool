package zur.koeln.kickertool.ui.api;

/**
 * Used for background threads. Defines a workflow where you perform a task in the background (thread) and
 * some resulting methods {@link #doOnSuccess()} and {@link #doOnFailure()}. <br/>
 * {@link #doOnSuccess()} gets called, when the background task was completed successfully. <br/>
 * {@link #doOnFailure()} gets called, whenever the method {@link #performTask()} throws an exception
 * 
 * @author Daniel Cleemann
 *
 */
public interface BackgroundTask<Result> {
	
	/**
	 * Performs the task in the background and delivers an result object, which can be used in the method {@link #doOnSuccess()}.
	 */
	Result performTask() throws Exception;
	
	/**
	 * After the task completed successfully you can do anything with the result object on the main thread here.
	 */
	void doOnSuccess(Result result);
	
	/**
	 * After the task failed by throwing an exception, you should show the exception to the user or do a cleanup.
	 */
	void doOnFailure(Throwable exception);
	
}
