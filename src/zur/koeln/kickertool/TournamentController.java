/**
 * 
 */
package zur.koeln.kickertool;

import java.util.Collection;

import lombok.Getter;
import zur.koeln.kickertool.player.Player;
import zur.koeln.kickertool.player.PlayerPool;
import zur.koeln.kickertool.tournament.Match;
import zur.koeln.kickertool.tournament.MatchException;
import zur.koeln.kickertool.tournament.Tournament;
import zur.koeln.kickertool.ui.GUIController;
import zur.koeln.kickertool.ui.GUIState;

@Getter
public class TournamentController {

    private final PlayerPool playerpool;
    private GUIController guiController;
    private Tournament currentTournament;

    /**
     * 
     */
    public TournamentController(GUIController guiController) {
        playerpool = new PlayerPool();
        this.guiController = guiController;
    }

    /**
     * 
     */
    public void showPlayerPoolManagement() {
        guiController.switchStateTo(GUIState.PLAYER_POOL);
    }

    /**
     * 
     */
    public void backToMainMenu() {
        guiController.switchStateTo(GUIState.MAIN_MENU);
    }

    /**
     * 
     */
    public void playerEdited() {
        playerpool.savePlayerPool();
    }

    /**
     * @param newPlayer
     */
    public void addPlayer(Player newPlayer) {
        playerpool.addPlayer(newPlayer);
    }

    /**
     * @param player
     */
    public void removePlayer(Player player) {
        playerpool.removePlayer(player);
    }

    /**
     * @param text
     */
    public void createNewTournament(String text) {
        currentTournament = new Tournament();
        currentTournament.setName(text);
        guiController.switchStateTo(GUIState.NEW_TOURNAMENT_CONFIG);
    }

    /**
     * @param configKey
     * @param newValue
     */
    public void changedTournamentConfig(String configKey, Integer newValue) {
        switch (configKey) {
        case "tables":
            currentTournament.getConfig().setTableCount(newValue.intValue());
            break;
        case "matchesToWin":
            currentTournament.getConfig().setMatchesToWin(newValue.intValue());
            break;
        case "goalsForWin":
            currentTournament.getConfig().setGoalsToWin(newValue.intValue());
            break;
        case "pointForWinner":
            currentTournament.getConfig().setPointsForWinner(newValue.intValue());
            break;
        case "pointsForDraw":
            currentTournament.getConfig().setPointsForDraw(newValue.intValue());
            break;
        case "minutesPerMatch":
            currentTournament.getConfig().setMinutesPerMatch(newValue.intValue());
            break;
        case "randomRounds":
            currentTournament.getConfig().setRandomRounds(newValue.intValue());
            break;
        }

    }

    /**
     * 
     */
    public void selectPlayer() {
        guiController.switchStateTo(GUIState.PLAYER_CONFIG);
    }

    /**
     * 
     */
    public void backToTournamentConfig() {
        guiController.switchStateTo(GUIState.NEW_TOURNAMENT_CONFIG);
    }

    /**
     * 
     */
    public void startTournament() {
        currentTournament.startTournament();
        guiController.switchStateTo(GUIState.TOURNAMENT);
    }

    /**
     * @param p
     */
    public void addParticipantToTournament(Player p) {
        currentTournament.addParticipant(p);
    }

    /**
     * @param p
     */
    public void removeParticipantFromTournament(Player p) {
        currentTournament.removeParticipant(p);
    }

    /**
     * @return
     */
    public Collection<Player> getParticipantList() {
        return currentTournament.getParticipants().values();
    }

    /**
     * 
     */
    public void nextRound() {
        currentTournament.newRound();
        guiController.update();
    }

    /**
     * @param match
     * @param scoreHome
     * @param value
     */
    public void updateMatchResult(Match match, Integer scoreHome, Integer scoreVisiting) {
        try {
            match.setResult(scoreHome, scoreVisiting);
            currentTournament.addMatchResult(match);
        } catch (MatchException e) {
            e.printStackTrace();
        }
        guiController.update();
    }

}
