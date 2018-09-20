package zur.koeln.kickertool.base;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import zur.koeln.kickertool.api.config.TournamentMode;
import zur.koeln.kickertool.api.player.Player;
import zur.koeln.kickertool.api.player.PlayerPoolService;
import zur.koeln.kickertool.api.tournament.Match;
import zur.koeln.kickertool.api.tournament.MatchResult;
import zur.koeln.kickertool.api.tournament.PlayerTournamentStatistics;
import zur.koeln.kickertool.api.tournament.Round;
import zur.koeln.kickertool.api.tournament.Tournament;
import zur.koeln.kickertool.tournament.GamingTable;
import zur.koeln.kickertool.tournament.PlayerTournamentStatisticsImpl;
import zur.koeln.kickertool.tournament.TournamentImpl;
import zur.koeln.kickertool.tournament.TournamentMatch;
import zur.koeln.kickertool.tournament.TournamentRound;
import zur.koeln.kickertool.tournament.TournamentTeam;
import zur.koeln.kickertool.tournament.factory.TournamentFactory;
import zur.koeln.kickertool.tournament.settings.TournamentSettingsImpl;

@Component
public class Importer {

	@Autowired
	private TournamentFactory tournamentFactory;

	@Autowired
	private PlayerPoolService playerpool;

	public Tournament importTournament(File tournamentToImport) throws IOException {
		ObjectMapper m = new ObjectMapper();
		JsonNode importRootNode = m.readTree(tournamentToImport);

		importSettings(importRootNode);

		TournamentImpl tournament = (TournamentImpl) tournamentFactory.createNewTournament();
		tournament.setName(importRootNode.get("name").asText());
		tournament.setParticipants(importUIDList(importRootNode.get("participants")));
		tournament.setCompleteRounds(importCompleteRounds(importRootNode.get("completeRounds")));
		tournament.setCurrentRound(importRound(importRootNode.get("currentRound")));
		tournament.setScoreTable(importScoreTable(importRootNode.get("scoreTable")));
		tournament.setPlaytables(importPlaytables(importRootNode.get("playtables")));
		tournament.setDummyPlayerActive(importUIDList(importRootNode.get("dummyPlayerActive")));
		initializeAfterImport(tournament);
		return tournament;

	}

	@JsonIgnore
	public void initializeAfterImport(TournamentImpl tournament) {
		tournament.setStarted(true);
		Map<UUID, Match> uuidToMatch = new HashMap<>();
		tournament.getAllMatches().forEach(m -> uuidToMatch.put(m.getMatchID(), m));
		tournament.getScoreTable().values()
				.forEach(tournamentStatistic -> ((PlayerTournamentStatisticsImpl) tournamentStatistic)
						.setUidToMatch(uuidToMatch));

	}

	private Map<Integer, GamingTable> importPlaytables(JsonNode tablesNode) {
		Map<Integer, GamingTable> tables = new HashMap<>();
		tablesNode.elements().forEachRemaining(node -> {
			GamingTable newTable = new GamingTable();
			newTable.setActive(node.get("active").asBoolean());
			newTable.setInUse(node.get("inUse").asBoolean());
			newTable.setTableNumber(node.get("tableNumber").asInt());
			tables.put(Integer.valueOf(newTable.getTableNumber()), newTable);
		});
		return tables;
	}

	private Map<UUID, PlayerTournamentStatistics> importScoreTable(JsonNode scoreTableNode) {
		Map<UUID, PlayerTournamentStatistics> scoreTable = new TreeMap<>();
		if (scoreTableNode == null) {
			return null;
		}
		scoreTableNode.elements().forEachRemaining(node -> {
			UUID playerId = UUID.fromString(node.get("playerId").asText());
			Player player = playerpool.getPlayerById(playerId);
			if (player == null) {
				player = playerpool.createDummyPlayerWithUUID(playerId);
			}
			PlayerTournamentStatisticsImpl statistics = (PlayerTournamentStatisticsImpl) tournamentFactory
					.createNewTournamentStatistics(player);
			statistics.setMatches(importUIDList(node.get("matches")));
			scoreTable.put(statistics.getPlayerId(),statistics);
		});
		return scoreTable;
	}

	private List<UUID> importUIDList(JsonNode node) {
		List<UUID> uids = new ArrayList<>();
		if (node == null) {
			return uids;
		}
		node.elements().forEachRemaining(uid -> uids.add(UUID.fromString(uid.asText())));
		return uids;
	}

	private void importSettings(JsonNode importRootNode) {
		TournamentSettingsImpl settings = (TournamentSettingsImpl) tournamentFactory.getTournamentSettings();
		JsonNode settingsNode = importRootNode.get("settings");
		settings.setMode(TournamentMode.valueOf(settingsNode.get("mode").asText()));
		settings.setTableCount(settingsNode.get("tableCount").asInt());
		settings.setRandomRounds(settingsNode.get("randomRounds").asInt());
		settings.setMatchesToWin(settingsNode.get("matchesToWin").asInt());
		settings.setGoalsToWin(settingsNode.get("goalsToWin").asInt());
		settings.setMinutesPerMatch(settingsNode.get("minutesPerMatch").asInt());
		settings.setPointsForWinner(settingsNode.get("pointsForWinner").asInt());
		settings.setPointsForDraw(settingsNode.get("pointsForDraw").asInt());
		settings.setFixedTeams(settingsNode.get("fixedTeams").asBoolean());
		settings.setCurrentNoOfMatches(settingsNode.get("currentNoOfMatches").asInt());
	}

	private List<Round> importCompleteRounds(JsonNode root) {
		List<Round> completeRounds = new ArrayList<>();
		ArrayNode roundsArray = ((ArrayNode) root);
		roundsArray.forEach(node -> completeRounds.add(importRound(node)));
		return completeRounds;
	}

	private TournamentRound importRound(JsonNode node) {
		TournamentRound newRound = (TournamentRound) tournamentFactory.createNewRound(node.get("roundNo").asInt());
		newRound.setCompleteMatches(importMatches(node.get("completeMatches")));
		newRound.setMatches(importMatches(node.get("matches")));
		newRound.setScoreTableAtEndOfRound(importScoreTable(node.get("scoreTableAtEndOfRound")));
		return newRound;
	}

	private List<Match> importMatches(JsonNode matchListNode) {
		List<Match> importedMatches = new ArrayList<>();

		matchListNode.elements().forEachRemaining(node -> {
			TournamentTeam home = new TournamentTeam();
			home.setPlayer1Id(UUID.fromString(node.get("homeTeam").get("player1Id").asText()));
			home.setPlayer2Id(UUID.fromString(node.get("homeTeam").get("player2Id").asText()));
			home.setPlayer1(playerpool.getPlayerById(home.getPlayer1Id()));
			home.setPlayer2(playerpool.getPlayerById(home.getPlayer2Id()));
			TournamentTeam visiting = new TournamentTeam();
			visiting.setPlayer1Id(UUID.fromString(node.get("visitingTeam").get("player1Id").asText()));
			visiting.setPlayer2Id(UUID.fromString(node.get("visitingTeam").get("player2Id").asText()));
			visiting.setPlayer1(playerpool.getPlayerById(visiting.getPlayer1Id()));
			visiting.setPlayer2(playerpool.getPlayerById(visiting.getPlayer2Id()));

			TournamentMatch match = tournamentFactory.createNewMatch(Integer.valueOf(node.get("roundNumber").asInt()),
					home, visiting, node.get("matchNo").asInt());
			match.setTableNo(node.get("tableNo").asInt());
			JsonNode scoreHomeNode = node.get("scoreHome");
			match.setScoreHome(scoreHomeNode != null ? scoreHomeNode.intValue() : 0);
			JsonNode scoreVisitingNode = node.get("scoreVisiting");
			match.setScoreVisiting(scoreVisitingNode != null ? scoreVisitingNode.intValue() : 0);
			JsonNode resultNode = node.get("result");
			match.setResult(!resultNode.isNull() ? MatchResult.valueOf(resultNode.asText()) : null);
			match.setMatchID(UUID.fromString(node.get("matchID").asText()));
			importedMatches.add(match);
		});

		return importedMatches;
	}

}
