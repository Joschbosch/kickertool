package zur.koeln.kickertool.application.handler.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zur.koeln.kickertool.core.kernl.TournamentMode;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TournamentModeDTO {

    private TournamentMode key;
    private String displayName;
}
