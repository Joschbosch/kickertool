package zur.koeln.kickertool.infrastructure.persistence.entities;

import java.util.LinkedList;
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

    @OneToOne
    @JoinColumn(name = "PLAYER_UID")
    private PlayerEntity player;

    @OneToMany
    @JoinColumn(name = "MATCH_ID")
    private final List<MatchEntity> playedMatches = new LinkedList<>();

}
