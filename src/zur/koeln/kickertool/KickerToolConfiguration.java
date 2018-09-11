package zur.koeln.kickertool;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import zur.koeln.kickertool.base.PlayerPoolService;
import zur.koeln.kickertool.player.SimpleJsonPlayerPool;
import zur.koeln.kickertool.tournament.TournamentAppConfiguration;
import zur.koeln.kickertool.uifxml.tools.SimpleTimer;
import zur.koeln.kickertool.uifxml.tools.Timer;

@Configuration
@Import({TournamentAppConfiguration.class})
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
    public Timer createTimerBean() {
        return new SimpleTimer();
    }
    
  
}
