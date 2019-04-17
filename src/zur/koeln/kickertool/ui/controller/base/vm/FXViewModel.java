package zur.koeln.kickertool.ui.controller.base.vm;

import zur.koeln.kickertool.application.handler.dtos.base.DTO;
import zur.koeln.kickertool.ui.exceptions.BackgroundTaskException;
import zur.koeln.kickertool.ui.tools.DTOVerifcationUtils;

public abstract class FXViewModel {
	
	protected void checkResponse(DTO baseDto) throws BackgroundTaskException {
		
		DTOVerifcationUtils.checkResponse(baseDto);
		
	}
	
	public abstract ModelValidationResult validate();
	
}
