package zur.koeln.kickertool.application.api.dtos.base;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListResponseDTO<T>
    extends DTO {

    private List<T> dtoValueList;
}
