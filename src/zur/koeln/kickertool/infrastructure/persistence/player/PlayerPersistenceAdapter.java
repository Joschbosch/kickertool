package zur.koeln.kickertool.infrastructure.persistence.player;

import org.springframework.stereotype.Component;

import zur.koeln.kickertool.core.model.Player;

@Component
public class PlayerPersistenceAdapter {


    public PlayerEntity toEntity(Player player) {
        PlayerEntity entity = new PlayerEntity();
        entity.setFirstName(player.getFirstName());
        entity.setLastName(player.getSurname());
        //        entity.setStatistics(player.getStatistics());
        entity.setUid(player.getUid());
        entity.setStatus(player.getStatus());
        return entity;
    }

    public Player fromEntity(PlayerEntity entity) {
        if (entity == null) {
            return null;
        }
        Player p = new Player(entity.getUid(), entity.getFirstName(), entity.getLastName(), false);
        //        p.setStatistics(entity.getStatistics());
        p.setStatus(entity.getStatus());
        return p;
    }
}
