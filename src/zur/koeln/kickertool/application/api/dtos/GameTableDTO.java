package zur.koeln.kickertool.application.api.dtos;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zur.koeln.kickertool.core.kernl.GameTableStatus;

@Getter
@Setter
@NoArgsConstructor
public class GameTableDTO {

    private UUID id;

    private int tableNumber;

    private GameTableStatus status;
}
