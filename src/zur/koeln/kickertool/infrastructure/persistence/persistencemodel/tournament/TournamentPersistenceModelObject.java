package zur.koeln.kickertool.infrastructure.persistence.persistencemodel.tournament;

import java.util.List;
import java.util.UUID;

import javax.persistence.*;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import lombok.Getter;
import lombok.Setter;
import zur.koeln.kickertool.core.bl.model.tournament.TournamentStatus;

@Entity
@Table(name = "tournament")
@Getter
@Setter
public class TournamentPersistenceModelObject {

    @Id
    private UUID uid;

    private String name;

    private TournamentStatus status;

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "SETTINGS_UID")
    private SettingsPersistenceModelObject settings;

    @ElementCollection
    @CollectionTable(name = "Participants", joinColumns = @JoinColumn(name = "participant_ids"))
    @Column(name = "participantids")
    private List<UUID> participants;

    @OneToMany(cascade = {CascadeType.ALL})
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "MATCH_ID")
    private List<MatchPersistenceModelObject> matches;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "TABLE_ID")
    private List<GameTablePersistenceModelObject> playtables;

    private int currentRound;

}
