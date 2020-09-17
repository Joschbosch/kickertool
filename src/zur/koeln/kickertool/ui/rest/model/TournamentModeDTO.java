package zur.koeln.kickertool.ui.rest.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zur.koeln.kickertool.core.bl.model.tournament.TournamentMode;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TournamentModeDTO {

    private TournamentMode key;
    private String displayName;
}
