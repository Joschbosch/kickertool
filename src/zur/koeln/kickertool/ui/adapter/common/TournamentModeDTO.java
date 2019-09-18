package zur.koeln.kickertool.ui.adapter.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zur.koeln.kickertool.core.domain.model.entities.tournament.TournamentMode;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TournamentModeDTO {

    private TournamentMode key;
    private String displayName;
}
