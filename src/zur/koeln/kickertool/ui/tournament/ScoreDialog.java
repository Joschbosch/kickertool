/**
 * 
 */
package zur.koeln.kickertool.ui.tournament;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import javafx.util.StringConverter;
import zur.koeln.kickertool.TournamentController;
import zur.koeln.kickertool.tournament.Match;

public class ScoreDialog<R> extends Dialog<R> {

    @SuppressWarnings("unchecked")
    public ScoreDialog(Match match, TournamentController controller) {

        ButtonType enterButton = new ButtonType("Enter", ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(enterButton, ButtonType.CANCEL);

        setTitle("Enter Match Result");

        GridPane grid = new GridPane();
        Label header = new Label("Enter result for match:");
        GridPane.setColumnSpan(header, 3);
        grid.add(header, 0, 0);

        Label homeLabel = new Label(match.getHomeTeamString());
        Label visitingLabel = new Label(match.getVisitingTeamString());

        grid.add(homeLabel, 0, 1);
        grid.add(visitingLabel, 2, 1);

        Spinner<Integer> scoreHome = createScoreSpinner(controller);
        Spinner<Integer> scoreVisiting = createScoreSpinner(controller);

        Label colon = new Label(":");
        colon.setAlignment(Pos.CENTER);
        colon.setMinWidth(20);

        grid.add(scoreHome, 0, 2);
        grid.add(colon, 1, 2);
        grid.add(scoreVisiting, 2, 2);

        setResultConverter(dialogButton -> {
            if (dialogButton == enterButton) {
                return (R) new Pair<Integer, Integer>(scoreHome.getValue(), scoreVisiting.getValue());
            }
            return null;
        });
        getDialogPane().setContent(grid);

    }

    /**
     * @param contoller
     * @return
     */
    private Spinner<Integer> createScoreSpinner(TournamentController contoller) {
        Spinner<Integer> spinner = new Spinner<>();
        spinner.setEditable(false);
        ObservableList<Integer> items = FXCollections.observableArrayList(0);
        for (int i = 1; i <= contoller.getCurrentTournament().getConfig().getGoalsToWin(); i++) {
            items.add(i);
        }
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.ListSpinnerValueFactory<>(items);
        IntegerConverter converter = new IntegerConverter();
        valueFactory.setConverter(converter);
        spinner.setValueFactory(valueFactory);
        return spinner;
    }
}

class IntegerConverter extends StringConverter<Integer> {

    @Override
    public String toString(Integer object) {
        return object + "";
    }

    @Override
    public Integer fromString(String string) {
        return Integer.parseInt(string);
    }

}
