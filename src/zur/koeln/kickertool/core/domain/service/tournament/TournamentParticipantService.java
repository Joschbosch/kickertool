package zur.koeln.kickertool.core.domain.service.tournament;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;

import zur.koeln.kickertool.core.domain.model.entities.player.Player;
import zur.koeln.kickertool.core.domain.model.entities.player.PlayerStatus;
import zur.koeln.kickertool.core.domain.model.entities.tournament.Tournament;
import zur.koeln.kickertool.core.domain.service.player.api.IPlayerFactory;
import zur.koeln.kickertool.core.domain.service.tournament.api.ITournamentParticipantManager;

@Named
public class TournamentParticipantService
    implements ITournamentParticipantManager {

    private final IPlayerFactory playerFactory;

    @Inject
    public TournamentParticipantService(
        IPlayerFactory playerFactory) {
        this.playerFactory = playerFactory;

    }

    @Override
    public List<Player> checkDummyPlayer(Tournament tournament, List<Player> participants, List<Player> freeDummies) {
        int usedDummies = getDummyPlayerCount(participants);
        int activePlayerCount = getActivePlayerCount(participants);
        int neededDummies = activePlayerCount % 4 == 0 ? 0 : 4 - activePlayerCount % 4;
        List<Player> newCreatedDummies = new ArrayList<>();
        if (neededDummies < usedDummies) {
            for (int i = neededDummies; i < usedDummies; i++) {
                removeLastDummyPlayer(tournament, participants);
            }
        } else if (neededDummies > usedDummies) {
            for (int i = usedDummies; i < neededDummies; i++) {
            	if (freeDummies.isEmpty()) {
            		Player createNewDummyPlayer = playerFactory.createNewDummyPlayer(usedDummies + i);
	                tournament.addDummyPlayer(createNewDummyPlayer);
	                newCreatedDummies.add(createNewDummyPlayer);
            	} else {
            		Player remove =freeDummies.remove(0);
					tournament.addDummyPlayer(remove);
            	}

            }
        }
        return newCreatedDummies;
    }
    @Override
    public boolean addParticipantToTournament(Tournament tournament, Player participant, List<Player> participants) {
        if (tournament.getAllParticipants().contains(participant.getUid())) {
            return false;
        }
        tournament.addParticipant(participant.getUid());
        return true;
    }

    public int getActivePlayerCount(List<Player> playersInTournament) {
        return (int) playersInTournament.stream()
                .filter(player -> (player.getStatus() == PlayerStatus.IN_TOURNAMENT || player.getStatus() == PlayerStatus.PLAYING) && !player.isDummy())
                .count();
    }
    public int getDummyPlayerCount(List<Player> playersInTournament) {
        return (int) playersInTournament.stream().filter(player -> (player != null && player.isDummy())).count();
    }

    public void removeLastDummyPlayer(Tournament tournament, List<Player> playersInTournament) {
        Player dummyToRemove = null;

        for (Player participant : playersInTournament) {
            if (participant.isDummy()) {
                dummyToRemove = participant;
            }
        }
        if (dummyToRemove != null) {
            tournament.removeParticipant(dummyToRemove.getUid());

        }
    }

    @Override
    public void updateParticipantsStatus(Tournament tournament, Map<UUID, Player> playersInTournament) {
        playersInTournament.keySet().forEach(id -> {

            if (playersInTournament.get(id) == null) {
                tournament.removeParticipant(id);
            }
        });
    }
    @Override
    public void removeFromTournament(Tournament tournament, UUID participantId, List<Player> participants) {
        tournament.removeParticipant(participantId);
    }

    @Override
    public void addParticipantsToTournament(List<Player> participants, Tournament newTournament) {
        participants.forEach(participant -> addParticipantToTournament(newTournament, participant, participants));
    }



}
