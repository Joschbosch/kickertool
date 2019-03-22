package zur.koeln.kickertool.ui.controller.vms.base;

import zur.koeln.kickertool.application.api.dtos.base.DTO;
import zur.koeln.kickertool.ui.exceptions.BackgroundTaskException;
import zur.koeln.kickertool.ui.tools.DTOVerifcationUtils;

public abstract class FXViewModel {
	
	protected void checkResponse(DTO baseDto) throws BackgroundTaskException {
		
		DTOVerifcationUtils.checkResponse(baseDto);
		
	}
	
	public abstract ModelValidationResult validate();
	
}
