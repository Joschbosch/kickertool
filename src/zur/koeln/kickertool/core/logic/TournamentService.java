package zur.koeln.kickertool.core.logic;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import zur.koeln.kickertool.core.api.IPlayerService;
import zur.koeln.kickertool.core.api.ITournamentRepository;
import zur.koeln.kickertool.core.api.ITournamentService;
import zur.koeln.kickertool.core.kernl.TournamentStatus;
import zur.koeln.kickertool.core.model.Settings;
import zur.koeln.kickertool.core.model.Tournament;

@Service
public class TournamentService
    implements ITournamentService {

    @Autowired
    private ITournamentRepository tournamentRepo;

    @Autowired
    private IPlayerService playerService;

    @Override
    public Tournament createNewTournament() {

        Settings newSettings = new Settings(UUID.randomUUID());
        Tournament newTournament = new Tournament(UUID.randomUUID(), newSettings);
        newSettings.setTournament(newTournament);
        tournamentRepo.saveOrUpdateTournament(newTournament);
        return newTournament;
    }
    @Override
    public Tournament startTournament(UUID tournamentIDToStart) {
        Tournament tournament = tournamentRepo.getTournament(tournamentIDToStart);
        tournament.setStatus(TournamentStatus.RUNNING);
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
    @Override
    public boolean addParticipantToTournament(UUID tournamentIDToAdd, UUID participant) {
        Tournament tournament = tournamentRepo.getTournament(tournamentIDToAdd);
        tournament.getParticipants().add(playerService.getPlayerById(participant));
        return true;
    }
    @Override
    public boolean removeParticipantFromournament(UUID tournamentIDToRemove, UUID participant) {
        Tournament tournament = tournamentRepo.getTournament(tournamentIDToRemove);
        tournament.getParticipants().remove(playerService.getPlayerById(participant));
        return true;
    }

    @Override
    public Settings changeTournamentSettings(Settings settings) {

        return null;
    }
}
