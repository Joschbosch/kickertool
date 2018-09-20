package zur.koeln.kickertool.api.player;

import java.util.UUID;

public interface Player {

    void setDummy(boolean b);

    void setUid(UUID id);

    UUID getUid();

    boolean isDummy();

    String getName();


}
