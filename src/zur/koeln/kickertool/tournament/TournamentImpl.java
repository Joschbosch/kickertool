package zur.koeln.kickertool.tournament;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;

import zur.koeln.kickertool.api.exceptions.MatchException;
import zur.koeln.kickertool.api.player.Player;
import zur.koeln.kickertool.api.player.PlayerPoolService;
import zur.koeln.kickertool.api.tournament.Match;
import zur.koeln.kickertool.api.tournament.PlayerTournamentStatistics;
import zur.koeln.kickertool.api.tournament.Round;
import zur.koeln.kickertool.api.tournament.TournamentSettings;
import zur.koeln.kickertool.tournament.factory.TournamentFactory;

@Component
public class TournamentImpl implements zur.koeln.kickertool.api.tournament.Tournament {

	@JsonIgnore
	@Autowired
	private PlayerPoolService playerPool;

	@JsonIgnore
	@Autowired
	private TournamentFactory tournamentFactory;

	@JsonIgnore
	private boolean started = false;

	@Autowired
	private TournamentSettings config;

	private String name;

	private List<UUID> participants = new ArrayList<>();

	private List<Round> completeRounds = new ArrayList<>();

	private Round currentRound;

	private Map<UUID, PlayerTournamentStatistics> scoreTable = new HashMap<>();

	private Map<Integer, GamingTable> playtables = new HashMap<>();

	private List<UUID> dummyPlayerActive = new ArrayList<>();

	/**
	 * @param players
	 */
	public void addParticipants(List<Player> players) {
		for (Player p : players) {
			addParticipant(p);
		}
	}

	public void addParticipant(Player p) {
		if (!started) {
			if (!participants.contains(p.getUid())) {
				participants.add(p.getUid());
			}
		} else {
			if (!scoreTable.containsKey(p.getUid())) {
				scoreTable.put(p.getUid(), tournamentFactory.createNewTournamentStatistics(p));
				checkDummies();
			}
		}
	}

	public void removeParticipant(Player p) {
		if (!started) {
			participants.remove(p.getUid());
		} else {
			scoreTable.remove(p.getUid());
			checkDummies();
		}

	}

	public void pausePlayer(Player selectedPlayer) {
		((PlayerTournamentStatisticsImpl) scoreTable.get(selectedPlayer.getUid())).setPlayerPausing(true);
		checkDummies();
	}

	public void unpausePlayer(Player selectedPlayer) {
		((PlayerTournamentStatisticsImpl) scoreTable.get(selectedPlayer.getUid())).setPlayerPausing(false);
		checkDummies();
	}

	/**
	 * 
	 */
	private void checkDummies() {
		int usedDummies = playerPool.getNoOfDummyPlayerUsed();
		int neededDummies = getActivePlayerCount() % 4 == 0 ? 0 : 4 - getActivePlayerCount() % 4;
		if (neededDummies < usedDummies) {
			for (int i = neededDummies; i < usedDummies; i++) {
				UUID removeDummy = playerPool.removeLastDummy();
				scoreTable.remove(removeDummy);
				dummyPlayerActive.remove(removeDummy);
			}
		} else if (neededDummies > usedDummies) {
			for (int i = usedDummies; i < neededDummies; i++) {
				Player dummy = playerPool.useNextDummyPlayer();
				scoreTable.put(dummy.getUid(), tournamentFactory.createNewTournamentStatistics(dummy));
				dummyPlayerActive.add(dummy.getUid());
			}
		}
	}

	/**
	 * @return
	 */
	private int getActivePlayerCount() {
		int count = 0;
		for (PlayerTournamentStatistics ts : scoreTable.values()) {
			Player player = playerPool.getPlayerOrDummyById(ts.getPlayerId());
			if (!scoreTable.get(player.getUid()).isPlayerPausing() && !player.isDummy()) {
				count++;
			}
		}
		return count;
	}

	public void startTournament() {
		if (!started) {
			for (int i = 1; i <= config.getTableCount(); i++) {
				playtables.put(Integer.valueOf(i), new GamingTable(i));
			}
			for (UUID pid : participants) {
				scoreTable.put(pid,
						tournamentFactory.createNewTournamentStatistics(playerPool.getPlayerOrDummyById(pid)));
			}
			checkDummies();
		}
		started = true;
	}

	public Round newRound() {

		if (isCurrentRoundComplete()) {
			int nextRoundNumber = 1;
			if (currentRound != null) {
				Map<UUID, PlayerTournamentStatistics> scoreTableClone = new HashMap<>();
				scoreTable.forEach((key, value) -> {
					PlayerTournamentStatistics clonedStatistics = tournamentFactory
							.createNewTournamentStatistics(value.getPlayer());
					((PlayerTournamentStatisticsImpl) value)
							.getClone((PlayerTournamentStatisticsImpl) clonedStatistics);
					scoreTableClone.put(key, clonedStatistics);
				});
				((TournamentRound) currentRound).setScoreTableAtEndOfRound(scoreTableClone);
				completeRounds.add(currentRound);
				nextRoundNumber = currentRound.getRoundNo() + 1;
			}
			Round newRound = tournamentFactory.createNewRound(nextRoundNumber);
			currentRound = newRound;
			((TournamentRound) newRound).createMatches(getCurrentTableCopySortedByPoints(), config);
			updatePlayTableUsage();
			return newRound;
		}
		return null;
	}

	public void addMatchResult(Match m) throws MatchException {
		((TournamentRound) currentRound).addMatchResult(m);
		playtables.get(Integer.valueOf(m.getTableNo())).setInUse(false);
		updatePlayTableUsage();

		updateTable(m, m.getHomeTeam().getPlayer1Id());
		updateTable(m, m.getHomeTeam().getPlayer2Id());
		updateTable(m, m.getVisitingTeam().getPlayer1Id());
		updateTable(m, m.getVisitingTeam().getPlayer2Id());
	}

	private void updateTable(Match m, UUID playerId) {
		PlayerTournamentStatistics homeP1Stat = scoreTable.get(playerId);
		if (homeP1Stat != null) {
			homeP1Stat.addMatchResult(m);
		}
	}

	private void updatePlayTableUsage() {
		for (Match ongoing : currentRound.getMatches()) {
			if (ongoing.getTableNo() == -1) {
				for (GamingTable gameTable : playtables.values()) {
					if (!gameTable.isInUse() && gameTable.isActive()) {
						((TournamentMatch) ongoing).setTableNo(gameTable.getTableNumber());
						gameTable.setInUse(true);
						break;
					}
				}
			}
		}
	}

	@JsonIgnore
	public List<PlayerTournamentStatistics> getHistoricTableCopySortedByPoints(int roundNo) {
		Collection<PlayerTournamentStatistics> tableToSort = scoreTable.values();

		if (currentRound.getRoundNo() != roundNo) {
			for (Round completeRound : completeRounds) {
				if (completeRound.getRoundNo() == roundNo && completeRound.getScoreTableAtEndOfRound() != null) {
					tableToSort = completeRound.getScoreTableAtEndOfRound().values();
					break;
				}
			}
		}

		List<PlayerTournamentStatistics> sorting = new LinkedList<>(tableToSort);
		Collections.sort(sorting);
		return sorting;
	}

	@JsonIgnore
	public List<PlayerTournamentStatistics> getCurrentTableCopySortedByPoints() {
		return getHistoricTableCopySortedByPoints(currentRound.getRoundNo());
	}

	@JsonIgnore
	public List<Match> getAllMatches() {

		List<Match> result = new LinkedList<>(currentRound.getAllMatches());
		completeRounds.forEach(r -> result.addAll(r.getAllMatches()));
		Collections.sort(result, (o1, o2) -> o1.getMatchNo() - o2.getMatchNo());

		return result;
	}

	@JsonIgnore
	public boolean isCurrentRoundComplete() {
		return currentRound != null ? currentRound.isComplete() : Boolean.TRUE.booleanValue();
	}

	@Override
	public List<Match> getMatchesForRound(int roundNo) {
		if (currentRound.getRoundNo() == roundNo) {
			return currentRound.getAllMatches();
		}
		for (Round r : completeRounds) {
			if (r.getRoundNo() == roundNo) {
				return r.getAllMatches();
			}
		}
		return new ArrayList<>();
	}

	public void setPlayerPool(PlayerPoolService playerPool) {
		this.playerPool = playerPool;
	}

	public void setTournamentFactory(TournamentFactory tournamentFactory) {
		this.tournamentFactory = tournamentFactory;
	}

	public TournamentSettings getSettings() {
		return config;
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

	public PlayerPoolService getPlayerPool() {
		return playerPool;
	}

	public TournamentFactory getTournamentFactory() {
		return tournamentFactory;
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

	public TournamentSettings getConfig() {
		return config;
	}

	public void setConfig(TournamentSettings config) {
		this.config = config;
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

}
