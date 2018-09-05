package zur.koeln.kickertool;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import zur.koeln.kickertool.base.PlayerPoolService;
import zur.koeln.kickertool.player.SimpleJsonPlayerPool;
import zur.koeln.kickertool.tools.SimpleTimer;
import zur.koeln.kickertool.tools.Timer;
import zur.koeln.kickertool.tournament.TournamentAppConfiguration;

@Configuration
@Import({TournamentAppConfiguration.class})
public class KickerToolConfiguration {

    @Bean
    public PlayerPoolService createPlayerPoolService() {
        SimpleJsonPlayerPool simpleJsonPlayerPool = new SimpleJsonPlayerPool();
        simpleJsonPlayerPool.loadPlayerPool();
        return simpleJsonPlayerPool;
    }


    @Bean
    public Timer createTimerBean() {
        return new SimpleTimer();
    }
}
