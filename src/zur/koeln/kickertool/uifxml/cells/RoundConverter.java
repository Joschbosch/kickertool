package zur.koeln.kickertool.uifxml.cells;

import javafx.util.StringConverter;
import zur.koeln.kickertool.core.entities.Round;

public class RoundConverter extends StringConverter<Round>{

	@Override
	public String toString(Round object) {
		// TODO Auto-generated method stub
		return "Runde " + String.valueOf(object.getRoundNo());
	}

	@Override
	public Round fromString(String string) {
		// TODO Auto-generated method stub
		return null;
	}

}
