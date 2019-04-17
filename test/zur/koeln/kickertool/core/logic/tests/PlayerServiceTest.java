package zur.koeln.kickertool.core.logic.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import zur.koeln.kickertool.core.api.IPlayerService;
import zur.koeln.kickertool.core.kernl.PlayerStatus;
import zur.koeln.kickertool.core.logic.PlayerService;
import zur.koeln.kickertool.core.model.aggregates.Player;
import zur.koeln.kickertool.core.spi.IPlayerRepository;

@Tag("core-test")
public class PlayerServiceTest {



    private static final String PLAYER_LASTNAME = "Bosch";
    private static final String PLAYER_FIRSTNAME = "Josch";

    @Test
	public void testCreatePlayer() {

		PlayerService newPlayerService = new PlayerService();

		IPlayerRepository playerRepoMock = Mockito.mock(IPlayerRepository.class);
        Player player = new Player();
        player.setFirstName(PLAYER_FIRSTNAME);
        player.setLastName(PLAYER_LASTNAME);
        player.setStatus(PlayerStatus.NOT_IN_TOURNAMENT);
        Mockito.when(playerRepoMock.createNewPlayer(PLAYER_FIRSTNAME, PLAYER_LASTNAME)).thenReturn(player);
		newPlayerService.setPlayerRepo(playerRepoMock);

		IPlayerService playerService = newPlayerService;

        Player newPlayer = playerService.createNewPlayer(PLAYER_FIRSTNAME, PLAYER_LASTNAME);
        Assertions.assertTrue(newPlayer.getFirstName().equals(PLAYER_FIRSTNAME));
        Assertions.assertTrue(newPlayer.getLastName().equals(PLAYER_LASTNAME));
		Assertions.assertTrue(newPlayer.getStatus() == PlayerStatus.NOT_IN_TOURNAMENT);
        Mockito.verify(playerRepoMock, Mockito.times(1)).createNewPlayer(PLAYER_FIRSTNAME, PLAYER_LASTNAME);

	}



}
