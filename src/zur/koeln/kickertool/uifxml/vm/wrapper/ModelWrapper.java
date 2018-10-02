package zur.koeln.kickertool.uifxml.vm.wrapper;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Component
public class ModelWrapper<T extends Enum> {

	private final Map<T, StringProperty> fieldToStringPropertyMap = new HashMap<>();
	
	public StringProperty getStringProperty(T field) {
		
		if (getFieldToStringPropertyMap().containsKey(field)) {
			return getFieldToStringPropertyMap().get(field);
		}
		
		StringProperty property = new SimpleStringProperty();
		getFieldToStringPropertyMap().put(field, property);
		
		return getFieldToStringPropertyMap().get(field);
	}
	
	
	private Map<T, StringProperty> getFieldToStringPropertyMap() {
		return fieldToStringPropertyMap;
	}

	
}
