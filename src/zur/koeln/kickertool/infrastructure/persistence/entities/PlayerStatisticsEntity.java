package zur.koeln.kickertool.infrastructure.persistence.entities;

import java.util.List;
import java.util.UUID;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "playerstatistics")
@Getter
@Setter
public class PlayerStatisticsEntity {

    @Id
    private UUID uid;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "PLAYER_UID")
    private PlayerEntity player;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "MATCH_UID")
    private List<MatchEntity> playedMatches;

}
