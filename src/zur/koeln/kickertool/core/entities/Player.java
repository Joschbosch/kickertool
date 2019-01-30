package zur.koeln.kickertool.core.entities;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import zur.koeln.kickertool.core.kernl.PlayerStatus;

@Getter
@Setter
public class Player{

    private UUID uid;

    private String firstName;

    private String surname;

    private boolean dummy;

    private PlayerStatus status;

    private PlayerStatistics statistics;

    public Player(
        UUID uid,
        String firstname,
        String surname,
        boolean dummy) {
    	this.uid = uid;
        this.firstName = firstname;
        this.surname = surname;
    	this.dummy = dummy;
    	status = PlayerStatus.NOT_IN_TOURNAMENT;
    }
}
