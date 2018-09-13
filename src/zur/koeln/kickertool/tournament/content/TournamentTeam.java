/**
 * 
 */
package zur.koeln.kickertool.tournament.content;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import zur.koeln.kickertool.api.player.Player;
import zur.koeln.kickertool.api.tournament.Team;

public class TournamentTeam
    implements Team {

    private UUID player1Id;
    private UUID player2Id;

    @JsonIgnore
    private Player player1;
    @JsonIgnore
    private Player player2;

    public TournamentTeam() {

    }

    public TournamentTeam(
        Player p1,
        Player p2) {
        this.setPlayer1Id(p1.getUid());
        this.setPlayer2Id(p2.getUid());
        this.player1 = p1;
        this.player2 = p2;
    }

    public UUID getPlayer2Id() {
        return player2Id;
    }

    public void setPlayer2Id(UUID p2) {
        this.player2Id = p2;
    }

    public UUID getPlayer1Id() {
        return player1Id;
    }

    public void setPlayer1Id(UUID p1) {
        this.player1Id = p1;
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player p1Obj) {
        this.player1 = p1Obj;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player p2Obj) {
        this.player2 = p2Obj;
    }
    
}
