/**
 * 
 */
package zur.koeln.kickertool.tournament.content;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GamingTable {

    private int tableNumber;

    private boolean active = true;

    private boolean inUse = false;

    public GamingTable() {

    }

    public GamingTable(
        int tableNo) {
        this.tableNumber = tableNo;
    }

}
