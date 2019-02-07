package zur.koeln.kickertool.infrastructure.persistence.entities;

import java.util.UUID;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import zur.koeln.kickertool.core.kernl.PlayerStatus;


@Entity
@Table(name = "player")
@Getter
@Setter
public class PlayerEntity {

    @Id
    private UUID uid;

    private String firstName;

    private String lastName;

    private PlayerStatus status;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "STATS_ID")
    private PlayerStatisticsEntity statistics;

}
