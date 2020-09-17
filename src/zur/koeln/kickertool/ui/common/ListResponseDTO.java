package zur.koeln.kickertool.ui.common;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListResponseDTO<T>
    extends DTO {

    private List<T> dtoValueList;
}
