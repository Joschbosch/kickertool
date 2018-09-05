/**
 * 
 */
package zur.koeln.kickertool.ui.tournament;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import zur.koeln.kickertool.base.TournamentControllerService;
import zur.koeln.kickertool.tournament.content.Round;

public class MatchesTable extends GridPane {

    private final TournamentControllerService controller;

    public MatchesTable(
        TournamentControllerService controller) {
        this.controller = controller;
        this.setVgap(10);

        update();
    }

    /**
     * 
     */
    public void update() {
        this.getChildren().clear();

        List<Round> rounds = new ArrayList<>(controller.getCurrentTournament().getCompleteRounds());
        Round currentRound = controller.getCurrentTournament().getCurrentRound();
        if (currentRound != null) {
            rounds.add(currentRound);
        }

        Collections.sort(rounds, (o1, o2) -> o1.getRoundNo() - o2.getRoundNo());

        for (Round r : rounds) {
            RoundEntry entry = new RoundEntry(r, controller);
            GridPane.setHgrow(entry, Priority.ALWAYS);
            this.add(entry, 0, r.getRoundNo() - 1);
        }
    }
}
