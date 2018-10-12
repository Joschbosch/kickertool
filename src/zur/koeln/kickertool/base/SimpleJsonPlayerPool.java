package zur.koeln.kickertool.base;

import java.io.File;
import java.io.IOException;
import java.util.*;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import zur.koeln.kickertool.api.player.Player;
import zur.koeln.kickertool.api.player.PlayerPoolService;

@Component
public class SimpleJsonPlayerPool
    implements PlayerPoolService {

    private static final Logger logger = LogManager.getLogger(PlayerPoolService.class);

    private static final String PLAYER_POOL_ROOT_DIR = "playerpools" + IOUtils.DIR_SEPARATOR;

    private List<Player> players;

    private final List<Player> dummies;

    private final List<Player> dummyPlayerUnused = new LinkedList<>();

    public SimpleJsonPlayerPool() {
        players = new ArrayList<>();
        dummies = new ArrayList<>();
    }
    @Override
    public void loadPlayerPool() {

        File playerPoolFile = new File(PLAYER_POOL_ROOT_DIR + "playerpool.json"); //$NON-NLS-1$
        if (playerPoolFile.exists() && playerPoolFile.isFile()) {
            ObjectMapper m = new ObjectMapper();
            try {
                players = m.readValue(playerPoolFile, new TypeReference<List<HumanPlayer>>() {
                });
            } catch (IOException e) {
                logger.error("Error loading Playerpool: ", e);
                players = new LinkedList<>();
            }
        } else {
            players = new LinkedList<>();
        }

    }
    @Override
    public void savePlayerPool() {
        File playerPoolRoot = new File(PLAYER_POOL_ROOT_DIR);
        if (!playerPoolRoot.exists()) {
            playerPoolRoot.mkdirs();
        }
        File playerPoolFile = new File(playerPoolRoot, "playerpool.json"); //$NON-NLS-1$

        ObjectMapper m = new ObjectMapper();
        try {
            m.writerWithDefaultPrettyPrinter().writeValue(playerPoolFile, players);
        } catch (IOException e) {
            logger.error("Error saving Playerpool: ", e);
        }
    }
    @Override
    public Player createAndAddPlayer(String newPlayerName) {
        if (newPlayerName != null && !newPlayerName.isEmpty()) {
            HumanPlayer newPlayer = new HumanPlayer(newPlayerName);
            players.add(newPlayer);
            savePlayerPool();
            return newPlayer;
        }
        return null;
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
    public Player getPlayerOrDummyById(UUID playerId) {
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
    public Player createDummyPlayerWithUUID(UUID id) {
        Player dummy = new HumanPlayer("Dummy Player " + (dummies.size() + 1)); //$NON-NLS-1$
        dummy.setDummy(true);
        dummy.setUid(id);
        dummies.add(dummy);
        return dummy;
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
        Player dummy = new HumanPlayer("Dummy Player " + i); //$NON-NLS-1$
        dummy.setDummy(true);
        return dummy;
    }
    @Override
    public List<Player> getPlayers() {
        return players;
    }
    @Override
    public void changePlayerName(String newName, Player selectedPlayer) {
        if (players.contains(selectedPlayer) && newName != null && !newName.isEmpty()) {
            ((HumanPlayer) selectedPlayer).setName(newName);
        }
    }

}
