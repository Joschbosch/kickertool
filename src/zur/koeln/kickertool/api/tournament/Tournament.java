package zur.koeln.kickertool.api.tournament;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import zur.koeln.kickertool.api.exceptions.MatchException;
import zur.koeln.kickertool.api.player.Player;

public interface Tournament {


    boolean isCurrentRoundComplete();

    TournamentSettings getSettings();

    void startTournament();

    void addParticipant(Player p);

    void removeParticipant(Player p);

    void addMatchResult(Match match) throws MatchException;

    Round newRound();

    void pausePlayer(Player playerById);

    List<UUID> getParticipants();

    void unpausePlayer(Player playerById);

    Map<UUID, PlayerTournamentStatistics> getScoreTable();

    String getName();

    void setName(String text);

    Round getCurrentRound();

    List<Match> getMatchesForRound(int roundNo);
}
