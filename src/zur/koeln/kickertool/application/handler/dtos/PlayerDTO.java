package zur.koeln.kickertool.application.handler.dtos;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zur.koeln.kickertool.core.kernl.PlayerStatus;

@Getter
@Setter
@NoArgsConstructor
public class PlayerDTO {

    private String firstName;
    private String lastName;
    private UUID uid;
    private PlayerStatus status;
    private boolean isDummyPlayer;

}
