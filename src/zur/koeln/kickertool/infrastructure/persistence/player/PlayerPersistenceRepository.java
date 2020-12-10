package zur.koeln.kickertool.infrastructure.persistence.player;

import java.util.UUID;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.repository.CrudRepository;

import zur.koeln.kickertool.infrastructure.persistence.entities.PlayerEntity;
import zur.koeln.kickertool.infrastructure.persistence.entities.QPlayerEntity;

public interface PlayerPersistenceRepository
    extends CrudRepository<PlayerEntity, UUID>, QuerydslPredicateExecutor<PlayerEntity> {
    // nothing needed

}
