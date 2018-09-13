package zur.koeln.kickertool.player;

import java.io.Serializable;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Player
    implements Serializable, zur.koeln.kickertool.api.Player {

    private UUID uid;

    private String name;

    private boolean dummy;

    @JsonIgnore
    private boolean pausingTournament = false;

    public Player() {
    }

    public Player(
        String name) {
        uid = UUID.randomUUID();
        this.name = name;
        dummy = false;
        pausingTournament = false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Player)) {
            return false;
        }
        return ((Player) obj).uid.equals(uid);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return uid.hashCode();
    }

    public UUID getUid() {
        return uid;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDummy() {
        return dummy;
    }

    public void setDummy(boolean dummy) {
        this.dummy = dummy;
    }

    public boolean isPausingTournament() {
        return pausingTournament;
    }

    public void setPausingTournament(boolean pausingTournament) {
        this.pausingTournament = pausingTournament;
    }

}
