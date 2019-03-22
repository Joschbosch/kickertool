package zur.koeln.kickertool.ui.controller.vms;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import zur.koeln.kickertool.core.kernl.GameTableStatus;
import zur.koeln.kickertool.ui.controller.vms.base.FXViewModel;
import zur.koeln.kickertool.ui.controller.vms.base.ModelValidationResult;

@Getter
@Setter
public class GameTableViewModel extends FXViewModel{

    private UUID id;

    private int tableNumber;

    private GameTableStatus status;
	
	@Override
	public ModelValidationResult validate() {
		return new ModelValidationResult();
	}

}
