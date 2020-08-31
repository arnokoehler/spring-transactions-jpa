package nl.intrix83.tutorials.transactional.fun.products.audited;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditedProductService {

    private final AuditedProductRepository auditedProductRepository;

    private final EntityManager entityManager;

    public Optional<AuditedProduct> find(final Long id) {
        return auditedProductRepository.findOneForUpdate(id);
    }

    public AuditedProduct update(final AuditedProduct auditedProduct) {
        return auditedProductRepository.save(auditedProduct);
    }

    public List<AuditedProduct> getHistory(final Long id) {
        List<AuditedProduct> history = new ArrayList<>();
        AuditReader auditReader = AuditReaderFactory.get(this.entityManager);

        List<Number> revisions = auditReader.getRevisions(AuditedProduct.class, id);

        for (int i = 0; i < revisions.size(); i++) {
            Number revision = revisions.get(i);
            AuditedProduct auditedProduct = auditReader.find(AuditedProduct.class, id, revision);
            history.add(auditedProduct);
        }

        return history;
    }


}
