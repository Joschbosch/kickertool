package zur.koeln.kickertool.application.repos;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import zur.koeln.kickertool.core.api.IDummyPlayerRepository;
import zur.koeln.kickertool.core.entities.Player;

@Service
public class DummyPlayerRepository
    implements IDummyPlayerRepository {

    @Override
    public Player getDummyPlayer(UUID uid) {
        // TODO sazu 5.5 2019 Auto-generated method stub
        return null;
    }

    @Override
    public List<Player> getAllDummyPlayer() {
        // TODO sazu 5.5 2019 Auto-generated method stub
        return null;
    }

}
