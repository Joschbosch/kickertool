package zur.koeln.kickertool.application.handler.api;

import zur.koeln.kickertool.application.handler.commands.player.PlayerCommandDTO;
import zur.koeln.kickertool.application.handler.commands.player.PlayerCommandEnum;
import zur.koeln.kickertool.core.entities.Player;

public interface CommandHandler {

    Player firePlayerCommand(PlayerCommandEnum commandType, PlayerCommandDTO dto);

}
