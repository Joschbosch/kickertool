package zur.koeln.kickertool.ui.api;

import zur.koeln.kickertool.ui.vm.base.ModelValidationResult;

public interface IFXMLDialogContent<T> {
	
	default T sendResult() {
		return null;
	}
	
	default ModelValidationResult validate() {
		return ModelValidationResult.empty();
	}
	
}
