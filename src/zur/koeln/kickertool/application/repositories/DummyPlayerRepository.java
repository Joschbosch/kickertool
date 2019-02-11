package zur.koeln.kickertool.application.repositories;

import java.util.*;

import org.springframework.stereotype.Service;

import zur.koeln.kickertool.core.kernl.PlayerStatus;
import zur.koeln.kickertool.core.model.Player;
import zur.koeln.kickertool.core.spi.IDummyPlayerRepository;

@Service
public class DummyPlayerRepository
    implements IDummyPlayerRepository {

    private final Map<UUID, Player> dummyPlayers = new HashMap<>();

    @Override
    public Player getDummyPlayer(UUID uid) {
        return dummyPlayers.get(uid);
    }

    @Override
    public Player getNewOrFreeDummyPlayer() {
        Optional<Player> findFirst = dummyPlayers.values().stream().filter(d -> d.getStatus() == PlayerStatus.NOT_IN_TOURNAMENT).findFirst();
        Player dummy = null;
        if (findFirst.isPresent()) {
            dummy = findFirst.get();
        } else {
            dummy = new Player(UUID.randomUUID(), "Dummy Player", String.valueOf(dummyPlayers.size()), true);
        }
        dummy.setStatus(PlayerStatus.IN_TOURNAMENT);
        return dummy;
    }

    @Override
    public List<Player> getAllDummyPlayer() {
        return new ArrayList<>(dummyPlayers.values());
    }

}
