package zur.koeln.kickertool.infrastructure.persistence;

import org.springframework.stereotype.Component;

import zur.koeln.kickertool.application.api.IPersistence;
import zur.koeln.kickertool.core.entities.Player;

@Component
public class ConsolePersistenceAdapter
    implements IPersistence {

    @Override
    public void insertPlayer(Player player) {
        System.out.println("Inserted Player " + player.getFirstName() + " " + player.getSurname() + " (" + player.getUid() + ")");

    }

    @Override
    public void updatePlayer(Player player) {
        System.out.println("Updated Player to " + player.getFirstName() + " " + player.getSurname() + " (" + player.getUid() + ")");

    }

    @Override
    public void deletePlayer(Player player) {
        System.out.println("Deleted Player " + player.getFirstName() + " " + player.getSurname() + " (" + player.getUid() + ")");

    }

}
