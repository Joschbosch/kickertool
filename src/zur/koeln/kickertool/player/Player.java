package zur.koeln.kickertool.player;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Player {

    private UUID uid;

    private String name;

    private String nickname;

    private boolean dummy;

    @JsonIgnore
    private boolean pausingTournement = false;

    public Player() {
    }

    public Player(String name, String nickname) { 
        uid = UUID.randomUUID();
        this.name = name;
        this.nickname = nickname;
        dummy = false;
        pausingTournement = false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Player)) {
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
}
