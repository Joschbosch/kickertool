package zur.koeln.kickertool.ui.controller.dialogs;

import org.springframework.stereotype.Component;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;
import lombok.AccessLevel;
import lombok.Getter;
import zur.koeln.kickertool.ui.api.FXMLDialogContent;
import zur.koeln.kickertool.ui.controller.base.AbstractFXMLController;
import zur.koeln.kickertool.ui.vm.TournamentSettingsViewModel;
import zur.koeln.kickertool.ui.vm.base.ModelValidationResult;

@Component
@Getter(value=AccessLevel.PRIVATE)
public class FXMLTournamentSettingsDialogContentController extends AbstractFXMLController implements FXMLDialogContent<TournamentSettingsViewModel, TournamentSettingsViewModel>{

	private TournamentSettingsViewModel initialSettingsViewModel;

	@FXML JFXTextField txtTableCount;
	@FXML JFXTextField txtPointsForWinner;
	@FXML JFXTextField txtPointsForDraw;
	@FXML JFXTextField txtMinutesPerMatch;
	@FXML JFXTextField txtGoalsToWin;
	@FXML JFXTextField txtMatchesToWin;
	@FXML JFXTextField txtRandomRounds;

	@FXML JFXCheckBox chckFixedTeams;
	
	@Override
	public void setupControls() {
		createTextNumberFormatter();
	}
	
	private void createTextNumberFormatter() {
		getTxtTableCount().setTextFormatter(createIntegerTextFormatter());
		getTxtTableCount().setTextFormatter(createIntegerTextFormatter());
		getTxtMatchesToWin().setTextFormatter(createIntegerTextFormatter());
		getTxtGoalsToWin().setTextFormatter(createIntegerTextFormatter());
		getTxtMinutesPerMatch().setTextFormatter(createIntegerTextFormatter());
		getTxtPointsForWinner().setTextFormatter(createIntegerTextFormatter());
		getTxtPointsForDraw().setTextFormatter(createIntegerTextFormatter());
	}
	
	private TextFormatter<Integer> createIntegerTextFormatter() {
		return new TextFormatter<>(new IntegerStringConverter());
	}

	
	@Override
	public TournamentSettingsViewModel sendResult() {
		
		return initialSettingsViewModel;
	}
	
	@Override
	public ModelValidationResult validate() {
		return initialSettingsViewModel.validate();
	}
	
	@Override
	public void initializeDialogWithContent(TournamentSettingsViewModel initialContent) {
		initialSettingsViewModel = initialContent;
		setupBindingsAfterInitialization();

	}
	
	private void setupBindingsAfterInitialization() {
		getTxtTableCount().textProperty().bindBidirectional(getInitialSettingsViewModel().getTableCountProperty());
		getTxtRandomRounds().textProperty().bindBidirectional(getInitialSettingsViewModel().getRandomRoundsProperty());
		getTxtMatchesToWin().textProperty().bindBidirectional(getInitialSettingsViewModel().getMatchesToWinProperty());
		getTxtGoalsToWin().textProperty().bindBidirectional(getInitialSettingsViewModel().getGoalsToWinProperty());
		getTxtMinutesPerMatch().textProperty().bindBidirectional(getInitialSettingsViewModel().getMinutesPerMatchProperty());
		getTxtPointsForWinner().textProperty().bindBidirectional(getInitialSettingsViewModel().getPointsForWinnerProperty());
		getTxtPointsForDraw().textProperty().bindBidirectional(getInitialSettingsViewModel().getPointsForDrawProperty());
		getChckFixedTeams().selectedProperty().bindBidirectional(getInitialSettingsViewModel().getFixedTeamsProperty());
	}
	
	
}
