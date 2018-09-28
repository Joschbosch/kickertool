package zur.koeln.kickertool.uifxml.converter;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javafx.util.StringConverter;

public class TimerStringConverter extends StringConverter<Number>{
	
	private final SimpleDateFormat formatter = new SimpleDateFormat("mm:ss.SSS"); //$NON-NLS-1$
	
	@Override
	public String toString(Number millisec) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millisec.longValue());
		
		return formatter.format(calendar.getTime());
	}
	
	@Override
	public Number fromString(String string) {
		
		return Long.valueOf(0);
	}
	
}
