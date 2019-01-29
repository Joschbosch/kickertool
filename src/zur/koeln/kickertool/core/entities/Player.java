package zur.koeln.kickertool.core.entities;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import zur.koeln.kickertool.core.kernl.PlayerStatus;

@Getter
@Setter
public class Player{

    private UUID uid;

    private String name;

    private boolean dummy;

private PlayerStatus status;

    public Player(UUID uid, String name, boolean dummy) {
    	this.uid = uid;
    	this.name = name;
    	this.dummy = dummy;
    	status = PlayerStatus.NOT_IN_TOURNAMENT;
    }
}
