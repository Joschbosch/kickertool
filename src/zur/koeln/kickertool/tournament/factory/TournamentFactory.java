package zur.koeln.kickertool.tournament.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import zur.koeln.kickertool.api.player.Player;
import zur.koeln.kickertool.api.tournament.TournamentSettings;
import zur.koeln.kickertool.tournament.*;

@Service
public class TournamentFactory {

    @Autowired
    private ApplicationContext ctx;

    public TournamentImpl createNewTournament() {
        return ctx.getBean(TournamentImpl.class);
    }

    public TournamentMatch createNewMatch(Integer roundNo, TournamentTeam home, TournamentTeam visiting, int matchNumber) {
        TournamentMatch newMatch = ctx.getBean(TournamentMatch.class);
        newMatch.setRoundNumber(roundNo);
        newMatch.setHomeTeam(home);
        newMatch.setVisitingTeam(visiting);
        newMatch.setMatchNo(matchNumber);
        return newMatch;
    }

    public TournamentRound createNewRound(int nextRoundNumber) {
        TournamentRound bean = ctx.getBean(TournamentRound.class);
        bean.setRoundNo(nextRoundNumber);
        return bean;
    }

    public PlayerTournamentStatisticsImpl createNewTournamentStatistics(Player p) {
        PlayerTournamentStatisticsImpl statistics = ctx.getBean(PlayerTournamentStatisticsImpl.class);
        statistics.setPlayer(p);
        statistics.setPlayerId(p.getUid());
        return statistics;
    }

    public TournamentSettings createNewTournamentConfig() {
        return ctx.getBean(TournamentSettings.class);
    }
}
