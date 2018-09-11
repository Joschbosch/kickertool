/**
 * 
 */
package zur.koeln.kickertool.tournament.content;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import zur.koeln.kickertool.player.Player;

public class Team {

    private UUID p1;
    private UUID p2;

    @JsonIgnore
    private Player p1Obj;
    @JsonIgnore
    private Player p2Obj;

    public Team() {

    }

    public Team(
        Player p1,
        Player p2) {
        this.setP1(p1.getUid());
        this.setP2(p2.getUid());
        this.setP1Obj(p1);
        this.setP2Obj(p2);
    }

    public UUID getP2() {
        return p2;
    }

    public void setP2(UUID p2) {
        this.p2 = p2;
    }

    public UUID getP1() {
        return p1;
    }

    public void setP1(UUID p1) {
        this.p1 = p1;
    }

    public Player getP1Obj() {
        return p1Obj;
    }

    public void setP1Obj(Player p1Obj) {
        this.p1Obj = p1Obj;
    }

    public Player getP2Obj() {
        return p2Obj;
    }

    public void setP2Obj(Player p2Obj) {
        this.p2Obj = p2Obj;
    }
    
}
