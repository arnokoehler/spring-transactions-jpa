package nl.intrix83.tutorials.transactional.fun.locking.versioned;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VersionedProductRepository extends JpaRepository<VersionedProduct, Long> {

}
