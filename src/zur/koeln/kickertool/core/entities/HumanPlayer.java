package zur.koeln.kickertool.core.entities;

import java.util.UUID;

public class HumanPlayer
    implements Player {

    private UUID uid;

    private String name;

    private boolean dummy;

    public HumanPlayer() {
    }

    public HumanPlayer(
        String name) {
        uid = UUID.randomUUID();
        this.name = name;
        dummy = false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof HumanPlayer)) {
            return false;
        }
        return ((HumanPlayer) obj).uid.equals(uid);
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


}
