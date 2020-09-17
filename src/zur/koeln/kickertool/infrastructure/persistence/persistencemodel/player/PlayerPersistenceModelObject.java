package zur.koeln.kickertool.infrastructure.persistence.persistencemodel.player;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import zur.koeln.kickertool.core.bl.model.player.PlayerStatus;


@Entity
@Table(name = "player")
@Getter
@Setter
public class PlayerPersistenceModelObject {
    @Id
    private UUID uid;
    private String firstName;
    private String lastName;
    private PlayerStatus status;
}
