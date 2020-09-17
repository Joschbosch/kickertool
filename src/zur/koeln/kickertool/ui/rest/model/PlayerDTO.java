package zur.koeln.kickertool.ui.rest.model;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zur.koeln.kickertool.core.bl.model.player.PlayerStatus;

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
