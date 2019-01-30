package zur.koeln.kickertool.application.handler.commands.player;

import zur.koeln.kickertool.application.handler.commands.api.Command;

public class PlayerCommand
    implements Command {

    private final PlayerCommandEnum commandType;

    private final PlayerCommandDTO dtoPlayer;

    public PlayerCommand(
        PlayerCommandEnum commandType,
        PlayerCommandDTO dto) {
        this.commandType = commandType;
        this.dtoPlayer = dto;
    }

    public PlayerCommandEnum getCommandType() {
        return commandType;
    }

    public PlayerCommandDTO getDtoPlayer() {
        return dtoPlayer;
    }
}
