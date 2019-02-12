package zur.koeln.kickertool.application.api.dtos.base;

import org.springframework.boot.autoconfigure.ldap.embedded.EmbeddedLdapProperties.Validation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class DTO {

    private StatusDTO dtoStatus;
    private Validation validation;
}
