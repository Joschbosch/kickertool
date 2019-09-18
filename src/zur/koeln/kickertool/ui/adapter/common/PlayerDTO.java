package zur.koeln.kickertool.ui.adapter.common;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zur.koeln.kickertool.core.domain.model.entities.player.PlayerStatus;

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
