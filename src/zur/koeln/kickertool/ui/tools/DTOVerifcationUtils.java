package zur.koeln.kickertool.ui.tools;

import zur.koeln.kickertool.application.api.dtos.base.DTO;
import zur.koeln.kickertool.application.api.dtos.base.StatusDTO;
import zur.koeln.kickertool.ui.exceptions.BackgroundTaskException;

public class DTOVerifcationUtils {

	private DTOVerifcationUtils() {
		//
	}
	
	public static void checkResponse(DTO baseDto) throws BackgroundTaskException {
		
		if (baseDto.getDtoStatus() != StatusDTO.SUCCESS) {
			throw new BackgroundTaskException(baseDto.getValidation().toString());
		}
		
	}
	
}
