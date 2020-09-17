package zur.koeln.kickertool.infrastructure.persistence.persistencemodel.tournament;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "team")
@Getter
@Setter
public class TeamPersistenceModelObject {

    @Id
    @GeneratedValue
    private UUID uid;
    //
    //    @ManyToOne
    //    @JoinColumn(name = "player1_id")
    //    private PlayerEntity player1;
    //
    //    @ManyToOne
    //    @JoinColumn(name = "player2_id")
    //    private PlayerEntity player2;

    private UUID player1Id;
    private UUID player2Id;

}
