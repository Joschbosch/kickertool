/**
 * 
 */
package zur.koeln.kickertool.ui.tournament;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import zur.koeln.kickertool.TournamentController;
import zur.koeln.kickertool.tournament.Round;

public class RoundEntry extends GridPane {

    /**
     * @param r
     */
    public RoundEntry(Round r, TournamentController controller) {

        for (int i = 0; i < r.getMatches().size(); i++) {
            MatchEntry entry = new MatchEntry(r.getMatches().get(i), controller);
            if (i % 2 == 0) {
                entry.setStyle("-fx-background-color: lightgrey;");
            }
            GridPane.setHalignment(entry, HPos.CENTER);
            GridPane.setHgrow(entry, Priority.ALWAYS);
            GridPane.setFillWidth(entry, true);
            this.add(entry, 1, i);

        }
        Label roundNumberLabel = new Label("" + r.getRoundNo());
        if (r.getRoundNo() % 2 == 1) {
            roundNumberLabel.setStyle("-fx-background-color:grey;");
        } else {
            roundNumberLabel.setStyle("-fx-background-color:lightgrey;");
        }
        roundNumberLabel.setMinWidth(20);
        roundNumberLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        roundNumberLabel.setAlignment(Pos.TOP_CENTER);
        GridPane.setValignment(roundNumberLabel, VPos.TOP);
        this.add(roundNumberLabel, 0, 0, 1, r.getMatches().size());
    }

}
