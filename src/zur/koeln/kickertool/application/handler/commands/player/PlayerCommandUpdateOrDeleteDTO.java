package zur.koeln.kickertool.application.handler.commands.player;

import zur.koeln.kickertool.core.entities.Player;

public class PlayerCommandUpdateOrDeleteDTO
    implements PlayerCommandDTO {

    private final Player player;
    
    public PlayerCommandUpdateOrDeleteDTO(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
