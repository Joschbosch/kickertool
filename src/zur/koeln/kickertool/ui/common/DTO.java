package zur.koeln.kickertool.ui.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class DTO {

    private StatusDTO dtoStatus;
    private ValidationDTO validation;
}
