package zur.koeln.kickertool.tournament.data;

import java.util.*;

import zur.koeln.kickertool.api.tournament.PlayerTournamentStatistics;
import zur.koeln.kickertool.api.tournament.Round;
import zur.koeln.kickertool.api.tournament.Tournament;
import zur.koeln.kickertool.api.tournament.TournamentSettings;

public class TournamentImpl
    implements Tournament {

    private final TournamentSettings settings;

    private final PlayerTournamentStatisticsComparator tableComparator;

	private boolean started = false;

	private String name;

	private List<UUID> participants = new ArrayList<>();

	private List<Round> completeRounds = new ArrayList<>();

	private Round currentRound;

	private Map<UUID, PlayerTournamentStatistics> scoreTable = new HashMap<>();

	private Map<Integer, GamingTable> playtables = new HashMap<>();

	private List<UUID> dummyPlayerActive = new ArrayList<>();

    public TournamentImpl(
        TournamentSettings settings) {
        this.settings = settings;
        tableComparator = new PlayerTournamentStatisticsComparator(settings);

    }

	public void setName(String name) {
		this.name = name;
	}

	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}

	public Round getCurrentRound() {
		return currentRound;
	}

	public void setCurrentRound(TournamentRound currentRound) {
		this.currentRound = currentRound;
	}


	public String getName() {
		return name;
	}

	public List<UUID> getParticipants() {
		return participants;
	}

	@Override
	public List<Round> getCompleteRounds() {
		return completeRounds;
	}

	public Map<UUID, PlayerTournamentStatistics> getScoreTable() {
		return scoreTable;
	}

	public Map<Integer, GamingTable> getPlaytables() {
		return playtables;
	}

	public List<UUID> getDummyPlayerActive() {
		return dummyPlayerActive;
	}

	public void setParticipants(List<UUID> participants) {
		this.participants = participants;
	}

	public void setCompleteRounds(List<Round> completeRounds) {
		this.completeRounds = completeRounds;
	}

	public void setCurrentRound(Round currentRound) {
		this.currentRound = currentRound;
	}

	public void setScoreTable(Map<UUID, PlayerTournamentStatistics> scoreTable) {
		this.scoreTable = scoreTable;
	}

	public void setPlaytables(Map<Integer, GamingTable> playtables) {
		this.playtables = playtables;
	}

	public void setDummyPlayerActive(List<UUID> dummyPlayerActive) {
		this.dummyPlayerActive = dummyPlayerActive;
	}

    @Override
    public TournamentSettings getSettings() {
        return settings;
    }

    public PlayerTournamentStatisticsComparator getTableComparator() {
        return tableComparator;
    }
}
