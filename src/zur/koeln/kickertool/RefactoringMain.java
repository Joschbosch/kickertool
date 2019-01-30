package zur.koeln.kickertool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import zur.koeln.kickertool.application.handler.api.CommandHandler;
import zur.koeln.kickertool.application.handler.commands.player.PlayerCommandCreateDTO;
import zur.koeln.kickertool.application.handler.commands.player.PlayerCommandEnum;

@SpringBootApplication
@Component
public class RefactoringMain {
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(RefactoringMain.class);
        CommandHandler handler = ctx.getBean(CommandHandler.class);
        handler.firePlayerCommand(PlayerCommandEnum.CREATE_PLAYER, new PlayerCommandCreateDTO("Josch", "Bosch"));
    }

}
