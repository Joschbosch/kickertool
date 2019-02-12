package zur.koeln.kickertool.application.api.dtos.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class DTO {

    private StatusDTO dtoStatus;
    private ValidationDTO validation;
}
