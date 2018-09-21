package zur.koeln.kickertool.base;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import zur.koeln.kickertool.api.PersistenceService;
import zur.koeln.kickertool.api.config.TournamentMode;
import zur.koeln.kickertool.api.player.Player;
import zur.koeln.kickertool.api.player.PlayerPoolService;
import zur.koeln.kickertool.api.tournament.*;
import zur.koeln.kickertool.tournament.*;
import zur.koeln.kickertool.tournament.factory.TournamentFactory;
import zur.koeln.kickertool.tournament.settings.TournamentSettingsImpl;

@Component
@SuppressWarnings("nls")
public class PersistenceServiceImpl
    implements PersistenceService {

	@Autowired
	private TournamentFactory tournamentFactory;

	@Autowired
	private PlayerPoolService playerpool;


    @Override
	public Tournament importTournament(File tournamentToImport) throws IOException {
		ObjectMapper m = new ObjectMapper();
		JsonNode importRootNode = m.readTree(tournamentToImport);

		importSettings(importRootNode);

		TournamentImpl tournament = (TournamentImpl) tournamentFactory.createNewTournament();
		tournament.setName(importRootNode.get("name").asText());
		tournament.setParticipants(importUIDList(importRootNode.get("participants")));
		tournament.setScoreTable(importScoreTable(importRootNode.get("scoreTable")));
		tournament.setCompleteRounds(importCompleteRounds(importRootNode.get("completeRounds")));
		tournament.setCurrentRound(importRound(importRootNode.get("currentRound")));
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
			Player player = playerpool.getPlayerOrDummyById(playerId);
			if (player == null) {
				player = playerpool.createDummyPlayerWithUUID(playerId);
			}
			PlayerTournamentStatisticsImpl statistics = (PlayerTournamentStatisticsImpl) tournamentFactory
					.createNewTournamentStatistics(player);
			statistics.setMatches(importUIDList(node.get("matches")));
            JsonNode playerPausingNode = node.get("playerPausing");
            statistics.setPlayerPausing(playerPausingNode != null ? playerPausingNode.booleanValue() : false);
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
			home.setPlayer1(playerpool.getPlayerOrDummyById(home.getPlayer1Id()));
			home.setPlayer2(playerpool.getPlayerOrDummyById(home.getPlayer2Id()));
			TournamentTeam visiting = new TournamentTeam();
			visiting.setPlayer1Id(UUID.fromString(node.get("visitingTeam").get("player1Id").asText()));
			visiting.setPlayer2Id(UUID.fromString(node.get("visitingTeam").get("player2Id").asText()));
			visiting.setPlayer1(playerpool.getPlayerOrDummyById(visiting.getPlayer1Id()));
			visiting.setPlayer2(playerpool.getPlayerOrDummyById(visiting.getPlayer2Id()));

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
    @Override
    public List<String> createTournamentsListForImport() {
        List<String> tournamentList = new ArrayList<>();
        try {
            Stream<Path> walk = Files.walk(Paths.get(""), 1); //$NON-NLS-1$
            walk.filter(Files::isRegularFile).forEach(file -> {
                if (file.toString().contains("tournament") && !file.toString().contains("-Round") && file.toString().contains(".json")) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    String name = FilenameUtils.getBaseName(file.toString());
                    name = name.substring("tournament".length()); //$NON-NLS-1$
                    tournamentList.add(name);
                }
            });
            walk.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tournamentList;
    }
    @Override
    public void exportTournament(Tournament currentTournament) {
        File tournamentFile = new File("tournament" + currentTournament.getName() + ".json"); //$NON-NLS-1$ //$NON-NLS-2$
        File tournamentRoundFile = new File("tournament" + currentTournament.getName() + "-Round" + currentTournament.getCurrentRound().getRoundNo() + ".json"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        ObjectMapper m = new ObjectMapper();
        try {
            m.writerWithDefaultPrettyPrinter().writeValue(tournamentFile, currentTournament);
            m.writerWithDefaultPrettyPrinter().writeValue(tournamentRoundFile, currentTournament);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
