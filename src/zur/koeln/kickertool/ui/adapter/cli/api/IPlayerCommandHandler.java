package zur.koeln.kickertool.ui.adapter.cli.api;

import java.util.UUID;

import zur.koeln.kickertool.ui.adapter.common.PlayerDTO;
import zur.koeln.kickertool.ui.adapter.common.base.ListResponseDTO;
import zur.koeln.kickertool.ui.adapter.common.base.SingleResponseDTO;
import zur.koeln.kickertool.ui.adapter.common.base.StatusOnlyDTO;

public interface IPlayerCommandHandler {

    SingleResponseDTO<PlayerDTO> createNewPlayer(String firstName, String lastName);

    StatusOnlyDTO deletePlayer(UUID id);

    SingleResponseDTO<PlayerDTO> updatePlayerName(UUID id, String newFirstName, String newLastName);

    ListResponseDTO<PlayerDTO> getAllPlayer();

    ListResponseDTO<PlayerDTO> getAllPlayerNotInTournament(UUID tournamendId);

}
