package zur.koeln.kickertool.application.api.dtos.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SingleResponseDTO<T>
    extends DTO {

    private T dtoValue;

}