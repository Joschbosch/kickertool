package zur.koeln.kickertool.ui.vm;

import org.springframework.stereotype.Component;

import lombok.Getter;
import zur.koeln.kickertool.ui.vm.base.FXViewModel;
import zur.koeln.kickertool.ui.vm.base.ModelValidationResult;

@Component
@Getter
public class TournamentSettingsViewModel extends FXViewModel{

	@Override
	public ModelValidationResult validate() {
		return ModelValidationResult.empty();
	}

}
