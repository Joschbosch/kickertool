package zur.koeln.kickertool.core.model;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import zur.koeln.kickertool.core.kernl.PlayerStatus;

@Getter
@Setter
public class Player{

    private UUID uid;

    private String firstName;

    private String lastName;

    private boolean dummy;

    private PlayerStatus status;

    private PlayerStatistics statistics;

    public Player() {

    }
    public Player(
        UUID uid,
        String firstname,
        String lastName,
        boolean dummy) {
    	this.uid = uid;
        this.firstName = firstname;
        this.lastName = lastName;
    	this.dummy = dummy;
    	status = PlayerStatus.NOT_IN_TOURNAMENT;
    }
}
