package zur.koeln.kickertool.infrastructure.persistence.entities;

import java.util.List;
import java.util.UUID;

import javax.persistence.*;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import lombok.Getter;
import lombok.Setter;
import zur.koeln.kickertool.core.kernl.TournamentStatus;

@Entity
@Table(name = "tournament")
@Getter
@Setter
public class TournamentEntity {

    @Id
    private UUID uid;

    private String name;

    private TournamentStatus status;

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "SETTINGS_UID")
    private SettingsEntity settings;

    @ElementCollection
    @CollectionTable(name = "Participants", joinColumns = @JoinColumn(name = "participant_ids"))
    @Column(name = "participantids")
    private List<UUID> participants;

    @OneToMany(cascade = {CascadeType.ALL})
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "MATCH_ID")
    private List<MatchEntity> matches;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "TABLE_ID")
    private List<GameTableEntity> playtables;

    private int currentRound;

}
