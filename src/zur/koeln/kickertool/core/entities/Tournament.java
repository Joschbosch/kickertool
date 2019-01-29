package zur.koeln.kickertool.core.entities;

import java.util.*;

import lombok.Getter;
import lombok.Setter;
import zur.koeln.kickertool.core.kernl.TournamentStatus;

@Getter
@Setter
public class Tournament {

    private String name;

    private TournamentStatus status;

    private Settings settings;

    private List<Player> participants = new ArrayList<>();

    private List<Match> matches = new ArrayList<>();

    private int currentRound;

    private Map<UUID, PlayerStatistics> scoreTable = new HashMap<>();

    private Map<Integer, GameTable> playtables = new HashMap<>();

    private List<UUID> dummyPlayerActive = new ArrayList<>();

   
}
