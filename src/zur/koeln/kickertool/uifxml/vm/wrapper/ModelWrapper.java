package zur.koeln.kickertool.uifxml.vm.wrapper;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Component
public class ModelWrapper<T extends Enum> {

	private final Map<T, StringProperty> fieldToStringPropertyMap = new HashMap<>();
	private final Map<T, StringProperty> fieldToStringPromptPropertyMap = new HashMap<>();
	
	public StringProperty getStringProperty(T field) {
		
		if (getFieldToStringPropertyMap().containsKey(field)) {
			return getFieldToStringPropertyMap().get(field);
		}
		
		StringProperty property = new SimpleStringProperty();
		getFieldToStringPropertyMap().put(field, property);
		
		return getFieldToStringPropertyMap().get(field);
	}
	
	public StringProperty getStringPromptProperty(T field) {
		
		if (getFieldToStringPromptPropertyMap().containsKey(field)) {
			return getFieldToStringPromptPropertyMap().get(field);
		}
		
		StringProperty property = new SimpleStringProperty();
		getFieldToStringPromptPropertyMap().put(field, property);
		
		return getFieldToStringPromptPropertyMap().get(field);
	}
	
	private Map<T, StringProperty> getFieldToStringPropertyMap() {
		return fieldToStringPropertyMap;
	}

	private Map<T, StringProperty> getFieldToStringPromptPropertyMap() {
		return fieldToStringPromptPropertyMap;
	}
	
}
