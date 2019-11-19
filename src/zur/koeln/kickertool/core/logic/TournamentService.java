package zur.koeln.kickertool.core.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.inject.Inject;
import javax.inject.Named;

import org.javatuples.Pair;

import zur.koeln.kickertool.core.api.IPlayerService;
import zur.koeln.kickertool.core.api.ITournamentService;
import zur.koeln.kickertool.core.kernl.PlayerRankingComparator;
import zur.koeln.kickertool.core.kernl.PlayerRankingRow;
import zur.koeln.kickertool.core.kernl.PlayerStatus;
import zur.koeln.kickertool.core.kernl.TournamentMode;
import zur.koeln.kickertool.core.model.aggregates.Player;
import zur.koeln.kickertool.core.model.aggregates.Tournament;
import zur.koeln.kickertool.core.model.entities.Match;
import zur.koeln.kickertool.core.model.entities.Settings;
import zur.koeln.kickertool.core.model.valueobjects.Team;
import zur.koeln.kickertool.core.spi.ITournamentRepository;

@Named
public class TournamentService implements ITournamentService {

	private final ITournamentRepository tournamentRepo;

	private final IPlayerService playerService;

	private Random r = new Random();

	@Inject
	public TournamentService(ITournamentRepository tournamentRepo, IPlayerService playerService) {
		this.tournamentRepo = tournamentRepo;
		this.playerService = playerService;
	}

	@Override
	public Tournament createNewTournament(String tournamentName, List<Player> participants, Settings settings) {

		Tournament newTournament = tournamentRepo.createNewTournament(tournamentName);
		newTournament.configureSettings(settings);
		addParticipantsToTournament(newTournament.getUid(),
				participants.stream().map(Player::getUid).collect(Collectors.toList()));
		tournamentRepo.saveOrUpdateTournament(newTournament);
		return newTournament;
	}

	@Override
	public Tournament startTournament(UUID tournamentIDToStart) {
		Tournament tournament = tournamentRepo.getTournament(tournamentIDToStart);
		checkDummyPlayer(tournament);
		tournament.startTournament();
		tournamentRepo.saveOrUpdateTournament(tournament);
		return tournament;
	}

	@Override
	public Tournament renameTournament(UUID tournamentIDToRename, String name) {
		Tournament tournament = tournamentRepo.getTournament(tournamentIDToRename);
		tournament.setName(name);
		tournamentRepo.saveOrUpdateTournament(tournament);
		return tournament;
	}

	private void addParticipantsToTournament(UUID uid, List<UUID> participantUIDs) {
		participantUIDs.forEach(p -> addParticipantToTournament(uid, p));

	}

	@Override
	public List<Player> addParticipantToTournament(UUID tournamentIDToAdd, UUID participant) {
		Tournament tournament = tournamentRepo.getTournament(tournamentIDToAdd);
		if (tournament.getAllParticipants().contains(participant)) {
			return null;
		}
		tournament.addParticipant(participant);
		playerService.setPlayerStatus(participant, PlayerStatus.IN_TOURNAMENT);
		checkDummyPlayer(tournament);
		updateParticipantsStatus(tournament);
		tournamentRepo.saveOrUpdateTournament(tournament);
		return getTournamentParticipants(tournamentIDToAdd);
	}

	@Override
	public List<Player> removeParticipantFromTournament(UUID tournamentIDToRemove, UUID participantId) {
		Tournament tournament = tournamentRepo.getTournament(tournamentIDToRemove);
		tournament.removeParticipant(participantId);
		checkDummyPlayer(tournament);
		tournamentRepo.saveOrUpdateTournament(tournament);
		return getTournamentParticipants(tournamentIDToRemove);
	}

	@Override
	public List<Player> getTournamentParticipants(UUID tournamentId) {
		Tournament tournament = tournamentRepo.getTournament(tournamentId);
		List<UUID> allParticipantIds = tournament.getAllParticipants();
		List<Player> participants = new ArrayList<>();
		allParticipantIds.forEach(id -> {

			Player playerById = playerService.getPlayerById(id);
			if (playerById != null) {
				participants.add(playerById);
			} else {
				tournament.removeParticipant(id);
			}
		});
		return participants;
	}

	@Override
	public boolean isCurrentRoundComplete(UUID tournamentId) {
		Tournament tournament = tournamentRepo.getTournament(tournamentId);
		return tournament.isCurrentRoundComplete();
	}

	@Override
	public List<Match> getMatchesForRound(int roundNo, UUID tournamentId) {
		Tournament tournament = tournamentRepo.getTournament(tournamentId);
		return tournament.getMatchesInTournamentRound(roundNo);
	}

	@Override
	public Tournament startNewRound(UUID tournamentToStartNewRound) {
		Tournament tournament = tournamentRepo.getTournament(tournamentToStartNewRound);
		if (!tournament.isCurrentRoundComplete()) {
			return null;
		}
		tournament.increaseRound();
		createNextMatches(tournament);
		tournament.updateGameTableUsage();
		tournamentRepo.saveOrUpdateTournament(tournament);
		return tournament;
	}

	private void checkDummyPlayer(Tournament tournament) {
		int usedDummies = getDummyPlayerCount(tournament);
		int activePlayerCount = getActivePlayerCount(tournament);
		int neededDummies = activePlayerCount % 4 == 0 ? 0 : 4 - activePlayerCount % 4;
		if (neededDummies < usedDummies) {
			for (int i = neededDummies; i < usedDummies; i++) {
				removeLastDummyPlayer(tournament);
			}
		} else if (neededDummies > usedDummies) {
			for (int i = usedDummies; i < neededDummies; i++) {
				tournament.addDummyPlayer(playerService.getDummyPlayer());
			}
		}
	}

	public List<PlayerRankingRow> getRankingByRound(UUID tournamentUID, int round) {
		Tournament tournament = tournamentRepo.getTournament(tournamentUID);
		List<Player> allParticipants = getTournamentParticipants(tournamentUID);
		Collections.sort(allParticipants, new PlayerRankingComparator(tournament, round));

		List<PlayerRankingRow> ranking = new ArrayList<>();
		for (int i = 0; i < allParticipants.size(); i++) {
			PlayerRankingRow newRow = new PlayerRankingRow();
			Player player = allParticipants.get(i);
			newRow.setPlayer(player);
			newRow.setRank(i + 1);
			if (!player.isDummy()) {
				newRow.setScore((int) tournament.getScoreForPlayerInRound(player, round));

				newRow.setMatchesPlayed((int) tournament.getNoOfFinishedMatchesForPlayerInRound(player, round));
				newRow.setMatchesWon((int) tournament.getMatchesWonForPlayerInRound(player, round));
				newRow.setMatchesDraw((int) tournament.getMatchesDrawForPlayerInRound(player, round));
				newRow.setMatchesLost(newRow.getMatchesPlayed() - newRow.getMatchesWon() - newRow.getMatchesDraw());

				newRow.setGoals((int) tournament.getGoalsForPlayerInRound(player, round));
				newRow.setConcededGoals((int) tournament.getConcededGoalsForPlayerInRound(player, round));
				newRow.setGoaldiff(newRow.getGoals() - newRow.getConcededGoals());
			}
			ranking.add(newRow);

		}
		return ranking;
	}

	@Override
	public boolean enterOrChangeMatchResult(UUID tournamentId, UUID matchID, int scoreHome, int scoreVisiting) {
		Tournament tournament = tournamentRepo.getTournament(tournamentId);
		boolean accepted = tournament.addMatchResult(matchID, scoreHome, scoreVisiting);

		if (accepted) {
			tournamentRepo.saveOrUpdateTournament(tournament);
		}
		return accepted;
	}

	@Override
	public Tournament getTournamentById(UUID tournamentUID) {
		Tournament tournament = tournamentRepo.getTournament(tournamentUID);
		updateParticipantsStatus(tournament);
		return tournament;
	}

	private void updateParticipantsStatus(Tournament tournament) {
		tournament.getAllParticipants().forEach(id -> {

			Player playerById = playerService.getPlayerById(id);
			if (playerById == null) {
				tournament.removeParticipant(id);
			}
		});
	}

	@Override
	public Settings getDefaultSettings() {
		return new Settings();
	}

	@Override
	public Settings getSettings(UUID tournamentUid) {
		return tournamentRepo.getTournament(tournamentUid).getSettings();
	}

	@Override
	public Tournament pauseOrUnpausePlayer(UUID tournamentId, UUID playerToPause, boolean pausing) {
		playerService.setPlayerStatus(playerToPause,
				pausing ? PlayerStatus.PAUSING_TOURNAMENT : PlayerStatus.IN_TOURNAMENT);
		Tournament tournament = tournamentRepo.getTournament(tournamentId);
		checkDummyPlayer(tournament);
		tournamentRepo.saveOrUpdateTournament(tournament);
		return tournament;
	}

	public void createNextMatches(Tournament tournament) {
		List<Player> currentPlayerRanking = getTournamentParticipants(tournament.getUid());
		Collections.sort(currentPlayerRanking, new PlayerRankingComparator(tournament, tournament.getCurrentRound()));

		currentPlayerRanking.removeIf(p -> p.getStatus() == PlayerStatus.PAUSING_TOURNAMENT);

		if (tournament.getCurrentRound() <= tournament.getSettings().getRandomRounds()) {
			while (currentPlayerRanking.size() > 3) {
				Team home = new Team(getRandomPlayer(currentPlayerRanking).getUid(),
						getRandomPlayer(currentPlayerRanking).getUid());
				Team visiting = new Team(getRandomPlayer(currentPlayerRanking).getUid(),
						getRandomPlayer(currentPlayerRanking).getUid());
				tournament.createMatch(home, visiting);
			}
		} else {
			createMatchesByTournamentType(currentPlayerRanking, tournament);
		}
	}

	private Player getRandomPlayer(List<Player> standings) {
		int random = new Random().nextInt(standings.size());
		return standings.remove(random);
	}

	private void createMatchesByTournamentType(List<Player> standings, Tournament tournament) {
		if (tournament.getSettings().getMode() == TournamentMode.SWISS_DYP) {
			while (standings.size() > 3) {
				Team home = new Team(standings.get(0).getUid(), standings.get(3).getUid());
				Team visiting = new Team(standings.get(1).getUid(), standings.get(2).getUid());
				standings.remove(0);
				standings.remove(0);
				standings.remove(0);
				standings.remove(0);
				tournament.createMatch(home, visiting);
			}
		} else if (tournament.getSettings().getMode() == TournamentMode.SWISS_TUPEL) {
			createMatchesSwissTupelmode(standings, tournament);
		} else if (tournament.getSettings().getMode() == TournamentMode.PARCIT_MODE) {
			int[] permutation = IntStream.range(0, standings.size()).toArray();
			int[] teamPermutation = IntStream.range(0, (standings.size() + 1) / 2).toArray();
			for (int i = 0; i < permutation.length / 4; i++) {
				randomize(permutation, i);

			}
			for (int i = 0; i < teamPermutation.length / 4; i++) {
				randomize(teamPermutation, i);
			}
			for (int i = 0; i < teamPermutation.length / 2; i++) {
				int ht1 = permutation[teamPermutation[i * 2] * 2];
				int ht2 = permutation[teamPermutation[i * 2 + 1] * 2 + 1];
				int vt1 = permutation[teamPermutation[i * 2] * 2 + 1];
				int vt2 = permutation[teamPermutation[i * 2 + 1] * 2];

				Team home = new Team(standings.get(ht1).getUid(), standings.get(ht2).getUid());
				Team visiting = new Team(standings.get(vt1).getUid(), standings.get(vt2).getUid());
				tournament.createMatch(home, visiting);
			}
		}
	}

	private void randomize(int[] permutation, int j) {
		int i = j * 4 + 1;
		int random = r.nextInt(3);
		if (i < permutation.length) {
			if (random == 0) {
				return;
			}
			if (random == 1) {
				swap(i, i + 1, permutation);
			}
			if (random == 2) {
				swap(i, i + 2, permutation);
				swap(i + 1, i + 2, permutation);
			}
		}
	}

	private void swap(int i, int j, int[] permutation) {
		int temp = permutation[i];
		permutation[i] = permutation[j];
		permutation[j] = temp;
	}

	private void createMatchesSwissTupelmode(List<Player> standings, Tournament tournament) {
		List<Pair<Player, Player>> playerPairs = new ArrayList<>();
		while (!standings.isEmpty()) {
			Pair<Player, Player> newPair = new Pair<>(standings.get(0), standings.get(1));
			playerPairs.add(newPair);
			standings.remove(0);
			standings.remove(0);
		}
		while (playerPairs.size() > 3) {
			Team home = new Team(playerPairs.get(0).getValue0().getUid(), playerPairs.get(3).getValue1().getUid());
			Team visiting = new Team(playerPairs.get(0).getValue1().getUid(), playerPairs.get(3).getValue0().getUid());
			tournament.createMatch(home, visiting);

			home = new Team(playerPairs.get(1).getValue0().getUid(), playerPairs.get(2).getValue1().getUid());
			visiting = new Team(playerPairs.get(1).getValue1().getUid(), playerPairs.get(2).getValue0().getUid());
			tournament.createMatch(home, visiting);

			playerPairs.remove(0);
			playerPairs.remove(0);
			playerPairs.remove(0);
			playerPairs.remove(0);
		}
		if (playerPairs.size() > 1) { // notwendig?
			Team home = new Team(playerPairs.get(0).getValue0().getUid(), playerPairs.get(1).getValue1().getUid());
			Team visiting = new Team(playerPairs.get(0).getValue1().getUid(), playerPairs.get(1).getValue0().getUid());
			tournament.createMatch(home, visiting);

		}
	}

	public int getActivePlayerCount(Tournament tournament) {
		return (int) tournament.getParticipants().stream()
				.filter(pId -> (playerService.getPlayerById(pId).getStatus() == PlayerStatus.IN_TOURNAMENT
						|| playerService.getPlayerById(pId).getStatus() == PlayerStatus.PLAYING)
						&& !playerService.getPlayerById(pId).isDummy())
				.count();
	}

	public int getDummyPlayerCount(Tournament tournament) {
		return (int) tournament.getParticipants().stream()
				.filter(pId -> (playerService.getPlayerById(pId) != null && playerService.getPlayerById(pId).isDummy()))
				.count();
	}

	public void removeLastDummyPlayer(Tournament tournament) {
		UUID dummyToRemove = null;
		for (UUID pId : tournament.getAllParticipants()) {
			if (playerService.getPlayerById(pId).isDummy()) {
				dummyToRemove = pId;
			}
		}
		if (dummyToRemove != null) {
			tournament.removeParticipant(dummyToRemove);
			playerService.getPlayerById(dummyToRemove).setStatus(PlayerStatus.NOT_IN_TOURNAMENT);
		}
	}
}
