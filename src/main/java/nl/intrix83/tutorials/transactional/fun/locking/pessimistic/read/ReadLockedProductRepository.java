package nl.intrix83.tutorials.transactional.fun.locking.pessimistic.read;

import java.util.Optional;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Transactional
public interface ReadLockedProductRepository extends JpaRepository<ReadLockedProduct, Long> {

    @Lock(LockModeType.PESSIMISTIC_READ)
    @NotNull Optional<ReadLockedProduct> findById(final @NotNull Long id);

    @Lock(LockModeType.PESSIMISTIC_READ)
    Optional<ReadLockedProduct> findByName(final String name);

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("select p from AuditedProduct p where p.id = :id")
    @NotNull Optional<ReadLockedProduct> findOneForUpdate(@Param("id") Long id);
}
