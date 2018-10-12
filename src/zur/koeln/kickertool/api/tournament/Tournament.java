package zur.koeln.kickertool.api.tournament;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import zur.koeln.kickertool.tournament.data.GamingTable;
import zur.koeln.kickertool.tournament.data.PlayerTournamentStatisticsComparator;

public interface Tournament {

    List<Round> getCompleteRounds();

    TournamentSettings getSettings();

    String getName();

    Round getCurrentRound();

    void setName(String newTournamentTitle);

    Map<UUID, PlayerTournamentStatistics> getScoreTable();

    List<UUID> getParticipants();

    Map<Integer, GamingTable> getPlaytables();

    boolean isStarted();

    List<UUID> getDummyPlayerActive();

    void setStarted(boolean b);

    void setCurrentRound(Round newRound);

    PlayerTournamentStatisticsComparator getTableComparator();

}
