package zur.koeln.kickertool.uifxml.tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javafx.util.StringConverter;

public class TimerStringConverter extends StringConverter<Number>{
	
	private SimpleDateFormat sdf = new SimpleDateFormat("mm:ss.SSS");
	
	@Override
	public String toString(Number millisec) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millisec.longValue());
		
		return sdf.format(calendar.getTime());
	}
	
	@Override
	public Number fromString(String string) {
		// TODO Auto-generated method stub
		return Long.valueOf(0);
	}
	
}
