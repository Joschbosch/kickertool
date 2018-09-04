/**
 * 
 */
package zur.koeln.kickertool.tournament;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Team {

    private UUID p1;
    private UUID p2;

    public Team() {

    }

    public Team(
        UUID p1,
        UUID p2) {
        this.p1 = p1;
        this.p2 = p2;
    }
}
