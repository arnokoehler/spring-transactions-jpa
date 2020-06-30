package nl.intrix83.tutorials.transactional.fun.products;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * https://vladmihalcea.com/best-way-map-entity-version-jpa-hibernate/
 * https://www.logicbig.com/tutorials/java-ee-tutorial/jpa/optimistic-lock.html
 * https://vladmihalcea.com/jpa-entity-version-property-hibernate/
 * https://stackoverflow.com/questions/2572566/java-jpa-version-annotation
 */
public interface VersionProductRespository extends JpaRepository<VersionProduct, Long> {
}
