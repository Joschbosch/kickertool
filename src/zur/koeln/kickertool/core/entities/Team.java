/**
 * 
 */
package zur.koeln.kickertool.core.entities;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Team {

    private UUID uid;

    private Player player1;
    private Player player2;

}
