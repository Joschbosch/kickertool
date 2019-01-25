/**
 * 
 */
package zur.koeln.kickertool.core.entities;

import java.util.UUID;

public class Team {

    private UUID player1Id;
    private UUID player2Id;

    private Player player1;
    private Player player2;

    public Team() {

    }

    public Team(
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
