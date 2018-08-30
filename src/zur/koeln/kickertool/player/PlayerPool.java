package zur.koeln.kickertool.player;

import java.io.File;
import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;

@Getter
public class PlayerPool {

    private List<Player> players;

    public PlayerPool() {
        players = new ArrayList<>();
        loadPlayerPool();
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

}
