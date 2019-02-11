package zur.koeln.kickertool.core.kernl.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

public class CustomModelMapper extends ModelMapper{
	
	public <T> List<T> map(List source, Class<T> targetClass) {
		
		return (List<T>) source.stream().map(element -> map(element, targetClass)).collect(Collectors.toList());
	}
	
}
