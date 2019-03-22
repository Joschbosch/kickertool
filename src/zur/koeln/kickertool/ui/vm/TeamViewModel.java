package zur.koeln.kickertool.ui.vm;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamViewModel {
	
    private UUID uid;

    private PlayerViewModel player1;
    private PlayerViewModel player2;
	
}
