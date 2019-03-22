package zur.koeln.kickertool.ui.controller.shared.vms;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import zur.koeln.kickertool.core.kernl.GameTableStatus;
import zur.koeln.kickertool.ui.controller.base.vm.FXViewModel;
import zur.koeln.kickertool.ui.controller.base.vm.ModelValidationResult;

@Getter
@Setter
public class GameTableDTOViewModel extends FXViewModel{

    private UUID id;

    private int tableNumber;

    private GameTableStatus status;
	
	@Override
	public ModelValidationResult validate() {
		return new ModelValidationResult();
	}

}
