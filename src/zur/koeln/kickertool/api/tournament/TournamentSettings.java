package zur.koeln.kickertool.api.tournament;

import zur.koeln.kickertool.api.config.TournamentMode;

public interface TournamentSettings {

    int getCurrentNoOfMatches();

    int getPointsForDraw();

    int getPointsForWinner();

    int getTableCount();

    int getRandomRounds();

    TournamentMode getMode();

	int getGoalsToWin();

	int getMatchesToWin();

	int getMinutesPerMatch();
}
