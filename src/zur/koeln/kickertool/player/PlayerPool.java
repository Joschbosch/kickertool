package zur.koeln.kickertool.player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;

@Getter
public class PlayerPool {

    private static PlayerPool instance;

    private List<Player> players;

    private final List<Player> dummies;

    private List<Player> dummyPlayerUnused = new LinkedList<>();

    public PlayerPool() {
        players = new ArrayList<>();
        dummies = new ArrayList<>();
        loadPlayerPool();
        setInstance(this);
    }

    public void loadPlayerPool() {
        File playerPoolFile = new File("playerpool.json"); //$NON-NLS-1$
        if (playerPoolFile.exists() && playerPoolFile.isFile()) {
            ObjectMapper m = new ObjectMapper();
            try {
                players = m.readValue(playerPoolFile, new TypeReference<List<Player>>() {
                    // ?
                });
            } catch (IOException e) {
                e.printStackTrace();
                players = new LinkedList<>();
            }
        } else {
            players = new LinkedList<>();
        }

    }

    public void savePlayerPool() {
        File playerPoolFile = new File("playerpool.json"); //$NON-NLS-1$
        ObjectMapper m = new ObjectMapper();
        try {
            m.writeValue(playerPoolFile, players);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addPlayer(Player player) {
        players.add(player);
        savePlayerPool();
    }

    public void removePlayer(Player player) {
        players.remove(player);
        savePlayerPool();
    }

    public void printPlayerPool() {
        System.out.println(String.format("%s\t%s\t%s\t", "Name", "Nickname", "ID")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        Collections.sort(players, new Comparator<Player>() {

            @Override
            public int compare(Player o1, Player o2) {
                return o1.getUid().compareTo(o2.getUid());
            }

        });
        for (Player p : players) {
            System.out.println(String.format("%s\t%s\t%s\t", p.getName(), p.getUid())); //$NON-NLS-1$
        }
    }

    /**
     * 
     */
    public void clear() {
        players.clear();
    }

    public static PlayerPool getInstance() {
        return instance;
    }

    public static void setInstance(PlayerPool instance) {
        PlayerPool.instance = instance;
    }

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

    public void addDummy(Player dummy) {
        dummies.add(dummy);
    }

    public void removeDummy(Player dummy) {
        dummies.remove(dummy);
    }

    public int getDummyPlayerUsed() {
        return dummies.size();
    }

    /**
     * @param i
     */
    private static Player createDummyPlayer(int i) {
        Player dummy = new Player("Dummy Player " + i); //$NON-NLS-1$
        dummy.setDummy(true);
        return dummy;
    }

    public UUID useNextDummyPlayer() {
        Player dummy = null;
        if (dummyPlayerUnused.isEmpty()) {
            dummy = createDummyPlayer(dummies.size() + 1);
        } else {
            dummy = dummyPlayerUnused.remove(dummyPlayerUnused.size() - 1);
        }
        dummies.add(dummy);
        return dummy.getUid();
    }

    public UUID removeLastDummy() {
        Player removedDummy = dummies.remove(dummies.size() - 1);
        dummyPlayerUnused.add(removedDummy);
        return removedDummy.getUid();
    }
}
