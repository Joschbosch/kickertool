package zur.koeln.kickertool.ui.vm.base;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import lombok.Getter;

@SuppressWarnings("nls")
@Getter
public class ModelValidationResult {
	
	private List<String> validationMessages = new ArrayList<>();
	
	public static ModelValidationResult empty() {
		return new ModelValidationResult();
	}
	
	public void addValidationMessage(String message) {
		getValidationMessages().add(message);
	}
	
	public boolean hasValidationMessages() {
		return !getValidationMessages().isEmpty();
	}
	
	public void addValidationResult(ModelValidationResult valResult) {
		getValidationMessages().addAll(valResult.getValidationMessages());
	}
	
	@Override
	public String toString() {
		return StringUtils.collectionToDelimitedString(getValidationMessages(), "\n");
	}
}
