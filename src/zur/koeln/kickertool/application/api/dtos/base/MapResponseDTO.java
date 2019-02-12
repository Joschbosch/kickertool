package zur.koeln.kickertool.application.api.dtos.base;

import java.util.Map;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MapResponseDTO<T>
    extends DTO {

    private Map<UUID, T> mapDTOValue;
}