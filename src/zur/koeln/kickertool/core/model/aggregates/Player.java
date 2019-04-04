package zur.koeln.kickertool.core.model.aggregates;

import java.util.List;
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

    private List<Tournament> playedTournaments;

    public void changeName(String newFirstName, String newLastName) {
        if (newFirstName.isEmpty() || newLastName.isEmpty()) {
            throw new IllegalArgumentException("Names can not be empty");
        }
        firstName = newFirstName;
        lastName = newLastName;

    }
}
