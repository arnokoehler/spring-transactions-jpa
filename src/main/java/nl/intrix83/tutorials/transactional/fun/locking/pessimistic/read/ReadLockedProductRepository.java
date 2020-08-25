package nl.intrix83.tutorials.transactional.fun.locking.pessimistic.read;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface ReadLockedProductRepository extends JpaRepository<ReadLockedProduct, Long> {

    @Lock(LockModeType.PESSIMISTIC_READ)
    public Optional<ReadLockedProduct> findById(final Long id);

    @Lock(LockModeType.PESSIMISTIC_READ)
    public Optional<ReadLockedProduct> findByName(final String name);
}
