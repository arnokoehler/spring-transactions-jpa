package nl.intrix83.tutorials.transactional.fun.locking.dirty;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DirtyProductRepository extends JpaRepository<DirtyProduct, Long> {

}
