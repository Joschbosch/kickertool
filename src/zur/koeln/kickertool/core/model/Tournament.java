package zur.koeln.kickertool.core.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import zur.koeln.kickertool.core.kernl.TournamentStatus;

@Getter
@Setter
public class Tournament {

    private UUID uid;

    private String name;

    private TournamentStatus status;

    private Settings settings;

    private List<Player> participants = new ArrayList<>();

    private List<Player> dummyPlayer = new ArrayList<>();

    private List<Match> matches = new ArrayList<>();

    private List<GameTable> playtables = new ArrayList<>();

    private int currentRound;

   
}