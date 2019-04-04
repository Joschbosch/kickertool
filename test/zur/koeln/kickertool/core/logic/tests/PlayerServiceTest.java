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


	@Test
	public void testCreatePlayer() {
		
		PlayerService newPlayerService = new PlayerService();
		
		IPlayerRepository playerRepoMock = Mockito.mock(IPlayerRepository.class);
		newPlayerService.setPlayerRepo(playerRepoMock);
		
		IPlayerService playerService = newPlayerService;
		
        Player newPlayer = playerService.createNewPlayer("Josch", "Bosch"); //$NON-NLS-1$ //$NON-NLS-2$
        Assertions.assertTrue(newPlayer.getFirstName().equals("Josch")); //$NON-NLS-1$
        Assertions.assertTrue(newPlayer.getLastName().equals("Bosch")); //$NON-NLS-1$
		Assertions.assertTrue(newPlayer.getStatus() == PlayerStatus.NOT_IN_TOURNAMENT);
		Mockito.verify(playerRepoMock, Mockito.times(1)).storeOrUpdatePlayer(newPlayer);
		
	}
	
    //	@Test
    //	public void testGetPlayerById() {
    //		
    //		PlayerService newPlayerService = new PlayerService();
    //		IDummyPlayerRepository dummyRepoMock = Mockito.mock(IDummyPlayerRepository.class);
    //        Player dummyPlayerReturn = new Player(UUID.randomUUID(), "new", "dummy", true); //$NON-NLS-1$ //$NON-NLS-2$
    //		Mockito.when(dummyRepoMock.getDummyPlayer(dummyPlayerReturn.getUid())).thenReturn(dummyPlayerReturn);
    //		newPlayerService.setDummyPlayerRepo(dummyRepoMock);
    //		
    //		IPlayerRepository playerRepoMock = Mockito.mock(IPlayerRepository.class);
    //        Player normalPlayerReturn = new Player(UUID.randomUUID(), "new", "Not dummy", true); //$NON-NLS-1$ //$NON-NLS-2$
    //		Mockito.when(playerRepoMock.getPlayer(normalPlayerReturn.getUid())).thenReturn(normalPlayerReturn);
    //		newPlayerService.setPlayerRepo(playerRepoMock);
    //		
    //		IPlayerService playerService = newPlayerService;
    //		
    //		Player newDummyPlayer = playerService.getPlayerById(dummyPlayerReturn.getUid());
    //		Assertions.assertTrue(newDummyPlayer.getUid().equals(dummyPlayerReturn.getUid()));
    //		
    //		Player newPlayer = playerService.getPlayerById(dummyPlayerReturn.getUid());
    //		Assertions.assertTrue(newPlayer.getUid().equals(normalPlayerReturn.getUid()));
    //		
    //		Player nullPlayer = playerService.getPlayerById(UUID.randomUUID());
    //		Assertions.assertNull(nullPlayer);
    //		
    //		Mockito.verify(dummyRepoMock, Mockito.times(2)).getDummyPlayer(Mockito.any(UUID.class));
    //		Mockito.verify(playerRepoMock, Mockito.times(3)).getPlayer(Mockito.any(UUID.class));
    //	}
    //	
    //
    //	public IPlayerService setupPlayerServiceStub() {
    //		PlayerService newPlayerService = new PlayerService();
    //		newPlayerService.setPlayerRepo(new PlayerRepoStub());
    //		newPlayerService.setDummyPlayerRepo(new PlayerRepoStub());
    //		return newPlayerService;
    //
    //	}

}
