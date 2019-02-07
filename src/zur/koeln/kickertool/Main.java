package zur.koeln.kickertool;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import zur.koeln.kickertool.application.handler.api.IPlayerCommandHandler;
import zur.koeln.kickertool.application.handler.commands.player.PlayerDTO;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Main.class);
        IPlayerCommandHandler playerHandler = ctx.getBean(IPlayerCommandHandler.class);
        
        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setFirstName("Josch");
        playerDTO.setLastName("Bosch");
        
        playerHandler.createNewPlayer(playerDTO);

        List<PlayerDTO> allPlayer = playerHandler.getAllPlayer();
        System.out.println(allPlayer);
        allPlayer.forEach(dto -> playerHandler.deletePlayer(dto.getUid()));
    }

}
