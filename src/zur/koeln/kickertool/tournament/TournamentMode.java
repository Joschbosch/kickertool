/**
 * 
 */
package zur.koeln.kickertool.tournament;

import lombok.Getter;

@Getter
public enum TournamentMode {

    SWISS_DYP("Schweizer System | DYP");

    private String displayName;

    TournamentMode(String displayName) {
        this.displayName = displayName;
    }
}
