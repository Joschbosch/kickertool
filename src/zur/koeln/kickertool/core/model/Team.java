/**
 * 
 */
package zur.koeln.kickertool.core.model;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Team {

    private UUID uid;

    private Player player1;
    private Player player2;

}
