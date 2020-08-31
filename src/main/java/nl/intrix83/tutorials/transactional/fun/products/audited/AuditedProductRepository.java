package nl.intrix83.tutorials.transactional.fun.products.audited;

import java.util.Optional;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Transactional
public interface AuditedProductRepository extends JpaRepository<AuditedProduct, Long> {

    @NotNull Optional<AuditedProduct> findById(final @NotNull Long id);

    Optional<AuditedProduct> findByName(final String name);

    @Query("select p from AuditedProduct p where p.id = :id")
    @NotNull Optional<AuditedProduct> findOneForUpdate(@Param("id") Long id);
}
