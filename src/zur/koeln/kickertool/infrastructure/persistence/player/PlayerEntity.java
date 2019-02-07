package zur.koeln.kickertool.infrastructure.persistence.player;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import zur.koeln.kickertool.core.kernl.PlayerStatus;


@Entity
@Table(name = "player")
@Getter
@Setter
public class PlayerEntity {

    @Column(name = "uuid")
    @Id
    private UUID uid;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "status")
    private PlayerStatus status;

    //    @Column(name = "statistics")
    //    @OneToOne
    //    private PlayerStatistics statistics;

}
