package zur.koeln.kickertool.application.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zur.koeln.kickertool.application.handler.api.CommandHandler;
import zur.koeln.kickertool.application.handler.commands.player.PlayerCommandCreateDTO;
import zur.koeln.kickertool.application.handler.commands.player.PlayerCommandDTO;
import zur.koeln.kickertool.application.handler.commands.player.PlayerCommandEnum;
import zur.koeln.kickertool.application.handler.commands.player.PlayerCommandUpdateOrDeleteDTO;
import zur.koeln.kickertool.core.api.IPlayerService;
import zur.koeln.kickertool.core.entities.Player;

@Service
public class BasicCommandHandler
    implements CommandHandler {

    @Autowired
    private IPlayerService playerService;

    @Override
    public Player firePlayerCommand(PlayerCommandEnum commandType, PlayerCommandDTO dto) {
        if (commandType == PlayerCommandEnum.CREATE_PLAYER) {
            playerService.createNewPlayer(((PlayerCommandCreateDTO) dto).getFirstName(), ((PlayerCommandCreateDTO) dto).getLastName());
        }
        if (commandType == PlayerCommandEnum.UPDATE_PLAYER_NAME) {
            playerService.updatePlayer(((PlayerCommandUpdateOrDeleteDTO) dto).getPlayer());
        }
        if (commandType == PlayerCommandEnum.DELETE_PLAYER) {
            playerService.deletePlayer(((PlayerCommandUpdateOrDeleteDTO) dto).getPlayer());
        }
        return null;
    }

}
