package zur.koeln.kickertool.infrastructure.persistence.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import zur.koeln.kickertool.core.kernl.GameTableStatus;

@Entity
@Table(name = "gametable")
@Getter
@Setter
public class GameTableEntity {

    @Id
    private Long id;

    private int tableNumber;

    private GameTableStatus status;

}
