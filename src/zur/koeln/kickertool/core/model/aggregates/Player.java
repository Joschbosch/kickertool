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

    private List<UUID> playedTournaments;

    public void changeName(String newFirstName, String newLastName) {
        if (newFirstName.isEmpty() || newLastName.isEmpty()) {
            throw new IllegalArgumentException("Names can not be empty"); //$NON-NLS-1$
        }
        firstName = newFirstName;
        lastName = newLastName;

    }

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((uid == null) ? 0 : uid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (uid == null) {
			if (other.uid != null)
				return false;
		}
		else if (!uid.equals(other.uid))
			return false;
		return true;
	}


}
