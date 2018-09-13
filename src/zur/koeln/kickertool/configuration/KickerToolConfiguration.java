package zur.koeln.kickertool.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import zur.koeln.kickertool.api.BackendController;
import zur.koeln.kickertool.api.player.PlayerPoolService;
import zur.koeln.kickertool.base.BasicBackendController;
import zur.koeln.kickertool.base.SimpleJsonPlayerPool;

@Configuration
@Import({TournamentContentConfiguration.class})
public class KickerToolConfiguration {

    @Bean
    @Primary
    public PlayerPoolService createPlayerPoolService() {
        SimpleJsonPlayerPool simpleJsonPlayerPool = new SimpleJsonPlayerPool();
        simpleJsonPlayerPool.loadPlayerPool();
        return simpleJsonPlayerPool;
    }

    @Bean
    @Primary
    public BackendController createBackendController() {
        return new BasicBackendController();
    }
  
}
