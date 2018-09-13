/**
 * 
 */
package zur.koeln.kickertool.api;


public enum TournamentMode {

    SWISS_DYP("Schweizer System | DYP"), SWISS_TUPEL("Schweizer System | Tupel");

    private String displayName;

    TournamentMode(String displayName) {
        this.displayName = displayName;
    }
    public String getDisplayName() {
        return displayName;
    }
}
