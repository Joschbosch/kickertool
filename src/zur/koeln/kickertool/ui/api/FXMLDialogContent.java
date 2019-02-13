package zur.koeln.kickertool.ui.api;

import zur.koeln.kickertool.ui.vm.base.ModelValidationResult;

public interface FXMLDialogContent<InitialContent, Result> {
	
	default Result sendResult() {
		return null;
	}
	
	default ModelValidationResult validate() {
		return ModelValidationResult.empty();
	}
	
	default void initContent(InitialContent content) {
		//
	}
}
