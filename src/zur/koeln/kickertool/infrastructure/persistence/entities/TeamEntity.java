package zur.koeln.kickertool.infrastructure.persistence.entities;

import java.util.UUID;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "team")
@Getter
@Setter
public class TeamEntity {

    @Id
    private UUID uid;

    @OneToOne
    @JoinColumn(name = "P1_ID")
    private PlayerEntity player1;

    @OneToOne
    @JoinColumn(name = "P2_ID")
    private PlayerEntity player2;
}
