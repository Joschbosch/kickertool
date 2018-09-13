package zur.koeln.kickertool.base;

import java.io.File;
import java.io.IOException;
import java.util.*;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import zur.koeln.kickertool.api.PlayerPoolService;
import zur.koeln.kickertool.player.Player;

@Component
public class SimpleJsonPlayerPool
    implements PlayerPoolService {

    private List<Player> players;

    private final List<Player> dummies;

    private final List<Player> dummyPlayerUnused = new LinkedList<>();

    public SimpleJsonPlayerPool() {
        players = new ArrayList<>();
        dummies = new ArrayList<>();

    }
    @Override
    public void loadPlayerPool() {
        File playerPoolFile = new File("playerpool.json"); //$NON-NLS-1$
        if (playerPoolFile.exists() && playerPoolFile.isFile()) {
            ObjectMapper m = new ObjectMapper();
            try {
                players = m.readValue(playerPoolFile, new TypeReference<List<Player>>() {
                });
            } catch (IOException e) {
                e.printStackTrace();
                players = new LinkedList<>();
            }
        } else {
            players = new LinkedList<>();
        }

    }
    @Override
    public void savePlayerPool() {
        File playerPoolFile = new File("playerpool.json"); //$NON-NLS-1$
        ObjectMapper m = new ObjectMapper();
        try {
            m.writeValue(playerPoolFile, players);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void addPlayer(Player player) {
        players.add(player);
        savePlayerPool();
    }
    @Override
    public void removePlayer(Player player) {
        players.remove(player);
        savePlayerPool();
    }


    /**
     * 
     */
    @Override
    public void clear() {
        players.clear();
    }


    @Override
    public Player getPlayerById(UUID playerId) {
        Optional<Player> p = players.stream().filter(player -> player.getUid().equals(playerId)).findFirst();
        if (p.isPresent()) {
            return p.get();
        }
        Optional<Player> dummy = dummies.stream().filter(player -> player.getUid().equals(playerId)).findFirst();
        if (dummy.isPresent()) {
            return dummy.get();
        }
        return null;
    }
    @Override
    public int getNoOfDummyPlayerUsed() {
        return dummies.size();
    }



    /**
     * @param i
     */
    @Override
    public void createDummyPlayerWithUUID(UUID id) {
        Player dummy = new Player("Dummy Player " + dummies.size() + 1); //$NON-NLS-1$
        dummy.setDummy(true);
        dummy.setUid(id);
        dummies.add(dummy);
    }
    @Override
    public Player useNextDummyPlayer() {
        Player dummy = null;
        if (dummyPlayerUnused.isEmpty()) {
            dummy = createDummyPlayer(dummies.size() + 1);
        } else {
            dummy = dummyPlayerUnused.remove(dummyPlayerUnused.size() - 1);
        }
        dummies.add(dummy);
        return dummy;
    }
    @Override
    public UUID removeLastDummy() {
        Player removedDummy = dummies.remove(dummies.size() - 1);
        dummyPlayerUnused.add(removedDummy);
        return removedDummy.getUid();
    }
    /**
     * @param i
     */
    private Player createDummyPlayer(int i) {
        Player dummy = new Player("Dummy Player " + i); //$NON-NLS-1$
        dummy.setDummy(true);
        return dummy;
    }
    @Override
    public List<Player> getPlayers() {
        return players;
    }
}
