package zur.koeln.kickertool.tournament.factory;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import zur.koeln.kickertool.tournament.TournamentConfig;
import zur.koeln.kickertool.tournament.content.*;

@Service
public class TournamentFactory {

    @Autowired
    private ApplicationContext ctx;

    public Tournament createNewTournament() {
        return ctx.getBean(Tournament.class);
    }

    public Match createNewMatch(Integer roundNo, Team home, Team visiting, int matchNumber) {
        Match newMatch = ctx.getBean(Match.class);
        newMatch.setRoundNumber(roundNo);
        newMatch.setHomeTeam(home);
        newMatch.setVisitingTeam(visiting);
        newMatch.setMatchNo(matchNumber);
        return newMatch;
    }

    public Round createNewRound(int nextRoundNumber) {
        Round bean = ctx.getBean(Round.class);
        bean.setRoundNo(nextRoundNumber);
        return bean;
    }

    public PlayerTournamentStatistics createNewTournamentStatistics(UUID playerId) {
        PlayerTournamentStatistics statistics = ctx.getBean(PlayerTournamentStatistics.class);
        statistics.setPlayerId(playerId);
        return statistics;
    }

    public TournamentConfig createNewTournamentConfig() {
        return ctx.getBean(TournamentConfig.class);
    }
}
