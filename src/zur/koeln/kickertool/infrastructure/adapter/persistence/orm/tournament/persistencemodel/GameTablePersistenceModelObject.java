package zur.koeln.kickertool.infrastructure.adapter.persistence.orm.tournament.persistencemodel;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import zur.koeln.kickertool.core.domain.model.entities.tournament.GameTableStatus;

@Entity
@Table(name = "gametable")
@Getter
@Setter
public class GameTablePersistenceModelObject {

    @Id
    private UUID id;

    private int tableNumber;

    private GameTableStatus status;

    public GameTablePersistenceModelObject() {
        id = UUID.randomUUID();
    }
}
