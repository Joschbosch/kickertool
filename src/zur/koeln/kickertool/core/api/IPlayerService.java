package zur.koeln.kickertool.core.api;

import java.util.List;
import java.util.UUID;

import zur.koeln.kickertool.core.kernl.PlayerStatus;
import zur.koeln.kickertool.core.model.aggregates.Player;

public interface IPlayerService {

    Player createNewPlayer(String firstName, String lastName);

    Player updatePlayerName(UUID id, String newFirstName, String newLastName);

    void deletePlayer(UUID player);

    List<Player> getAllPlayer();

    Player getPlayerById(UUID participant);

    Player getDummyPlayer();

    void setPlayerStatus(UUID participant, PlayerStatus inTournament);

}
