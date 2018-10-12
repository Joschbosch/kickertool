package zur.koeln.kickertool.base;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import zur.koeln.kickertool.tournament.TournamentService;
import zur.koeln.kickertool.tournament.data.*;
import zur.koeln.kickertool.tournament.settings.TournamentSettingsImpl;

@Component
@SuppressWarnings("nls")
public class PersistenceServiceImpl
    implements PersistenceService {

    private static final String TOURNAMENT_ROUND_SUFFIX = "-Round";

    private static final String EXPORT_FILE_ENDING = ".json";

    private static final String TOURNAMENT_ROOT_DIR = "tournaments";

    private static final Logger logger = LogManager.getLogger(PersistenceService.class);


    private final TournamentService tournamentService;

	private final PlayerPoolService playerpool;

    public PersistenceServiceImpl(
        PlayerPoolService playerpool,
        TournamentService tournamentService) {
        this.playerpool = playerpool;
        this.tournamentService = tournamentService;
    }

    @Override
    public Tournament importTournament(String tournamentToImport) throws IOException {
        File tournamentFile = new File(new File(TOURNAMENT_ROOT_DIR), tournamentToImport + EXPORT_FILE_ENDING);
        ObjectMapper m = new ObjectMapper();
        JsonNode importRootNode = m.readTree(tournamentFile);

		importSettings(importRootNode);

        TournamentImpl tournament = (TournamentImpl) tournamentService.createNewTournament("");
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
        tournamentService.getAllMatches().forEach(m -> uuidToMatch.put(m.getMatchID(), m));
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
            PlayerTournamentStatisticsImpl statistics = new PlayerTournamentStatisticsImpl(player);
			statistics.setMatches(importUIDList(node.get("matches")));
            JsonNode playerPausingNode = node.get("playerPausing");
            statistics.setPlayerPausing(playerPausingNode != null ? playerPausingNode.booleanValue() : Boolean.FALSE.booleanValue());
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
        TournamentSettingsImpl settings = new TournamentSettingsImpl();
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
        TournamentRound newRound = new TournamentRound(node.get("roundNo").asInt());
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

            TournamentMatch match = new TournamentMatch(Integer.valueOf(node.get("roundNumber").asInt()),
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
        File tournamentRootDir = new File(TOURNAMENT_ROOT_DIR);
        if (!tournamentRootDir.exists()) {
            tournamentRootDir.mkdirs();
        }
        try {
            Stream<Path> walk = Files.walk(Paths.get(TOURNAMENT_ROOT_DIR), 1); //$NON-NLS-1$
            walk.filter(Files::isRegularFile).forEach(file -> {
                if (!file.toString().contains(TOURNAMENT_ROUND_SUFFIX) && file.toString().contains(EXPORT_FILE_ENDING)) { //$NON-NLS-1$ 
                    String name = FilenameUtils.getBaseName(file.toString());
                    tournamentList.add(name);
                }
            });
            walk.close();
        } catch (IOException e) {
            logger.error("Error importing tournament: ", e);

        }
        return tournamentList;
    }
    @Override
    public void exportTournament(Tournament currentTournament) {
        File tournamentFile = new File(new File(TOURNAMENT_ROOT_DIR), currentTournament.getName() + EXPORT_FILE_ENDING);
        File tournamentRoundFile = new File(new File(TOURNAMENT_ROOT_DIR), currentTournament.getName() + TOURNAMENT_ROUND_SUFFIX + currentTournament.getCurrentRound().getRoundNo() + EXPORT_FILE_ENDING); //$NON-NLS-1$ 
        ObjectMapper m = new ObjectMapper();
        try {
            m.writerWithDefaultPrettyPrinter().writeValue(tournamentFile, currentTournament);
            m.writerWithDefaultPrettyPrinter().writeValue(tournamentRoundFile, currentTournament);
        } catch (IOException e) {
            logger.error("Error exporting tournament: ", e);
        }
    }
}
