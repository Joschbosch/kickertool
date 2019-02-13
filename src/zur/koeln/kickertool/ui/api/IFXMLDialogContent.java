package zur.koeln.kickertool.ui.api;

import zur.koeln.kickertool.ui.vm.base.ModelValidationResult;

public interface IFXMLDialogContent<Content, Result> {
	
	default Result sendResult() {
		return null;
	}
	
	default ModelValidationResult validate() {
		return ModelValidationResult.empty();
	}
	
	default void initContent(Content content) {
		//
	}
}
