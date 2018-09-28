package zur.koeln.kickertool.uifxml.converter;

import javafx.util.StringConverter;
import zur.koeln.kickertool.api.tournament.Round;

public class RoundConverter extends StringConverter<Round>{

	@Override
	public String toString(Round object) {
		return "Runde " + String.valueOf(object.getRoundNo());
	}

	@Override
	public Round fromString(String string) {
		return null;
	}

}
