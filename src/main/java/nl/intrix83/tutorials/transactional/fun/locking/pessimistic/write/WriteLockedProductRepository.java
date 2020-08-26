package nl.intrix83.tutorials.transactional.fun.locking.pessimistic.write;

import java.util.Optional;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

@Transactional
public interface WriteLockedProductRepository extends JpaRepository<WriteLockedProduct, Long> {

    @Lock(LockModeType.PESSIMISTIC_READ)
    Optional<WriteLockedProduct> findByName(final String name);
}
