package zur.koeln.kickertool.core.application.api;

import java.util.List;
import java.util.UUID;

import zur.koeln.kickertool.core.domain.model.entities.player.Player;
import zur.koeln.kickertool.core.domain.model.entities.player.PlayerStatus;

public interface IPlayerService {

    Player createNewPlayer(String firstName, String lastName);

    Player updatePlayerName(UUID id, String newFirstName, String newLastName);

    void deletePlayer(UUID player);

    List<Player> getAllPlayer();

    Player getPlayerById(UUID participant);

    Player getDummyPlayer();

    void setPlayerStatus(UUID participant, PlayerStatus inTournament);

}
