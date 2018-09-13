package zur.koeln.kickertool.api.tournament;

import java.util.UUID;

import zur.koeln.kickertool.api.player.Player;

public interface Team {

    UUID getPlayer1Id();
    UUID getPlayer2Id();

    Player getPlayer1();
    Player getPlayer2();
}
