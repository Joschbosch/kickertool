/**
 * 
 */
package zur.koeln.kickertool.core.kernl;


public enum TournamentMode {

    SWISS_DYP("Schweizer System | DYP"), SWISS_TUPEL("Schweizer System | Tupel"); //$NON-NLS-1$ //$NON-NLS-2$

    private String displayName;

    TournamentMode(String displayName) {
        this.displayName = displayName;
    }
    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public String toString() {
    	return getDisplayName();
    }
}
