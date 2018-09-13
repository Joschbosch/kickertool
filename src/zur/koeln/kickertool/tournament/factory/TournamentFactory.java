package zur.koeln.kickertool.tournament.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import zur.koeln.kickertool.api.player.Player;
import zur.koeln.kickertool.api.tournament.PlayerTournamentStatistics;
import zur.koeln.kickertool.api.tournament.Round;
import zur.koeln.kickertool.api.tournament.Tournament;
import zur.koeln.kickertool.api.tournament.TournamentSettings;
import zur.koeln.kickertool.tournament.PlayerTournamentStatisticsImpl;
import zur.koeln.kickertool.tournament.TournamentMatch;
import zur.koeln.kickertool.tournament.TournamentRound;
import zur.koeln.kickertool.tournament.TournamentTeam;

@Service
public class TournamentFactory {

    @Autowired
    private ApplicationContext ctx;

    public Tournament createNewTournament() {
        return ctx.getBean(Tournament.class);
    }

    public TournamentSettings createNewTournamentConfig() {
        return ctx.getBean(TournamentSettings.class);
    }
    public TournamentMatch createNewMatch(Integer roundNo, TournamentTeam home, TournamentTeam visiting, int matchNumber) {
        TournamentMatch newMatch = ctx.getBean(TournamentMatch.class);
        newMatch.setRoundNumber(roundNo);
        newMatch.setHomeTeam(home);
        newMatch.setVisitingTeam(visiting);
        newMatch.setMatchNo(matchNumber);
        return newMatch;
    }

    public Round createNewRound(int nextRoundNumber) {
        TournamentRound bean = ctx.getBean(TournamentRound.class);
        bean.setRoundNo(nextRoundNumber);
        return bean;
    }

    public PlayerTournamentStatistics createNewTournamentStatistics(Player p) {
        PlayerTournamentStatisticsImpl statistics = ctx.getBean(PlayerTournamentStatisticsImpl.class);
        statistics.setPlayer(p);
        statistics.setPlayerId(p.getUid());
        return statistics;
    }


}
