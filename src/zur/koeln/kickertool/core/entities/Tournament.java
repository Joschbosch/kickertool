package zur.koeln.kickertool.core.entities;

import java.util.*;

public class Tournament {

    private String name;

    private boolean started = false;

    private Settings settings;

    private List<UUID> participants = new ArrayList<>();

    private List<Round> completeRounds = new ArrayList<>();

    private Round currentRound;

    private Map<UUID, PlayerStatistics> scoreTable = new HashMap<>();

    private Map<Integer, GameTable> playtables = new HashMap<>();

    private List<UUID> dummyPlayerActive = new ArrayList<>();

    /**
     * @param players
     */
    public void addParticipants(List<Player> players) {
        for (Player p : players) {
            addParticipant(p);
        }
    }

    private void addParticipant(Player p) {
        participants.add(p.getUid());
    }

    public Settings getSettings() {
        return settings;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public Round getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(Round currentRound) {
        this.currentRound = currentRound;
    }

    public String getName() {
        return name;
    }

    public List<UUID> getParticipants() {
        return participants;
    }

    public Map<UUID, PlayerStatistics> getScoreTable() {
        return scoreTable;
    }

    public Map<Integer, GameTable> getPlaytables() {
        return playtables;
    }

    public List<UUID> getDummyPlayerActive() {
        return dummyPlayerActive;
    }


    public void setConfig(Settings settings) {
        this.settings = settings;
    }

    public void setParticipants(List<UUID> participants) {
        this.participants = participants;
    }

    public void setCompleteRounds(List<Round> completeRounds) {
        this.completeRounds = completeRounds;
    }

    public void setScoreTable(Map<UUID, PlayerStatistics> scoreTable) {
        this.scoreTable = scoreTable;
    }

    public void setPlaytables(Map<Integer, GameTable> playtables) {
        this.playtables = playtables;
    }

    public void setDummyPlayerActive(List<UUID> dummyPlayerActive) {
        this.dummyPlayerActive = dummyPlayerActive;
    }

    public List<Round> getCompleteRounds() {
        return completeRounds;
    }

}
