package zur.koeln.kickertool.tournament;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

import zur.koeln.kickertool.base.TournamentControllerService;
import zur.koeln.kickertool.tournament.content.Match;
import zur.koeln.kickertool.tournament.content.Round;
import zur.koeln.kickertool.tournament.content.Tournament;
import zur.koeln.kickertool.tournament.content.TournamentStatistics;
import zur.koeln.kickertool.tournament.factory.TournamentFactory;

@Configuration
public class TournamentAppConfiguration {
    @Bean
    @Primary
    public TournamentControllerService createTournamentController() {
        return new BasicTournamentController();
    }

    @Bean
    @Primary
    public TournamentFactory createTournamentFactory() {
        return new TournamentFactory();
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Tournament createTournamentBean() {
        return new Tournament();
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Match createMatchBean() {
        return new Match();
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Round createRoundBean() {
        return new Round();
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public TournamentStatistics createTournamentStatisticsBean() {
        return new TournamentStatistics();
    }

}
