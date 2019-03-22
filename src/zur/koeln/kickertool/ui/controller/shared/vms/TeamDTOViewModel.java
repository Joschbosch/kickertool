package zur.koeln.kickertool.ui.controller.shared.vms;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamDTOViewModel {
	
    private UUID uid;

    private PlayerDTOViewModel player1;
    private PlayerDTOViewModel player2;
	
}
