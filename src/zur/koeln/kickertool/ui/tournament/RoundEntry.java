/**
 * 
 */
package zur.koeln.kickertool.ui.tournament;

import java.util.Collections;
import java.util.List;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import zur.koeln.kickertool.base.BackendController;
import zur.koeln.kickertool.tournament.content.Match;
import zur.koeln.kickertool.tournament.content.Round;

public class RoundEntry extends GridPane {

    /**
     * @param r
     */
    public RoundEntry(
        Round r,
        BackendController controller) {

        List<Match> allMatches = r.getAllMatches();
        Collections.sort(allMatches, (m1, m2) -> Integer.compare(m1.getMatchNo(), m2.getMatchNo()));
        for (int i = 0; i < allMatches.size(); i++) {
            MatchEntry entry = new MatchEntry(allMatches.get(i), controller);
            if (i % 2 == 0) {
                entry.setStyle("-fx-background-color: lightgrey;");
            }
            GridPane.setHalignment(entry, HPos.CENTER);
            GridPane.setHgrow(entry, Priority.ALWAYS);
            GridPane.setFillWidth(entry, Boolean.TRUE);
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
        this.add(roundNumberLabel, 0, 0, 1, allMatches.size());
    }

}
