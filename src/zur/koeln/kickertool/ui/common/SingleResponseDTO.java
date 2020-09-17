package zur.koeln.kickertool.ui.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SingleResponseDTO<T>
    extends DTO {

    private T dtoValue;

}