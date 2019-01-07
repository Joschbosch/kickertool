/**
 * 
 */
package zur.koeln.kickertool.core.entities;

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
    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isInUse() {
        return inUse;
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }

}
