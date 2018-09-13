package zur.koeln.kickertool.configuration;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

import zur.koeln.kickertool.tournament.PlayerTournamentStatisticsImpl;
import zur.koeln.kickertool.tournament.TournamentImpl;
import zur.koeln.kickertool.tournament.TournamentMatch;
import zur.koeln.kickertool.tournament.TournamentRound;
import zur.koeln.kickertool.tournament.factory.TournamentFactory;
import zur.koeln.kickertool.tournament.settings.TournamentSettingsImpl;

@Configuration
public class TournamentContentConfiguration {
 

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
    public TournamentSettingsImpl createTournamentConfigBean() {
        return new TournamentSettingsImpl();
    }

}
