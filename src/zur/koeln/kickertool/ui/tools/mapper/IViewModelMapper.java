package zur.koeln.kickertool.ui.tools.mapper;

import java.util.List;
import java.util.stream.Collectors;

public interface IViewModelMapper<DTO, VM> {

	public VM map(DTO dto);
	
	public default List<VM> map(List<DTO> dtos) {
		return dtos.stream().map(dto -> map(dto)).collect(Collectors.toList());
	}
	
}
