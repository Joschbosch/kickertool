package zur.koeln.kickertool.ui.controller.shared.vms;

import org.springframework.stereotype.Component;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;
import zur.koeln.kickertool.ui.controller.base.vm.FXViewModel;
import zur.koeln.kickertool.ui.controller.base.vm.ModelValidationResult;

@Component
@Getter
@Setter
public class PlayerRankingRowViewModel extends FXViewModel {

	private final IntegerProperty rankProperty = new SimpleIntegerProperty();
	private final StringProperty nameProperty = new SimpleStringProperty();
	private final IntegerProperty scoreProperty = new SimpleIntegerProperty();
	
	@Override
	public ModelValidationResult validate() {
		return new ModelValidationResult();
	}

}
