package zur.koeln.kickertool.tournament;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

import zur.koeln.kickertool.api.BackendController;
import zur.koeln.kickertool.base.BasicBackendController;
import zur.koeln.kickertool.tournament.content.TournamentMatch;
import zur.koeln.kickertool.tournament.content.PlayerTournamentStatisticsImpl;
import zur.koeln.kickertool.tournament.content.TournamentRound;
import zur.koeln.kickertool.tournament.content.TournamentImpl;
import zur.koeln.kickertool.tournament.factory.TournamentFactory;

@Configuration
public class TournamentAppConfiguration {
    @Bean
    @Primary
    public BackendController createTournamentController() {
        return new BasicBackendController();
    }

    @Bean
    @Primary
    public TournamentFactory createTournamentFactory() {
        return new TournamentFactory();
    }

    @Bean
    @Primary
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public TournamentImpl createTournamentBean() {
        return new TournamentImpl();
    }

    @Bean
    @Primary
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public TournamentMatch createMatchBean() {
        return new TournamentMatch();
    }

    @Bean
    @Primary
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public TournamentRound createRoundBean() {
        return new TournamentRound();
    }

    @Bean
    @Primary
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public PlayerTournamentStatisticsImpl createTournamentStatisticsBean() {
        return new PlayerTournamentStatisticsImpl();
    }

    @Bean
    @Primary
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public TournamentConfig createTournamentConfigBean() {
        return new TournamentConfig();
    }

}
