package nl.intrix83.tutorials.transactional.fun.locking.pessimistic;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.transaction.Transactional;

import nl.intrix83.tutorials.transactional.fun.locking.pessimistic.read.ReadLockedProduct;
import nl.intrix83.tutorials.transactional.fun.locking.pessimistic.read.ReadLockedProductRepository;
import nl.intrix83.tutorials.transactional.fun.locking.pessimistic.write.WriteLockedProduct;
import nl.intrix83.tutorials.transactional.fun.locking.pessimistic.write.WriteLockedProductRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReadingService {

    private final EntityManagerFactory emf;

    private final ReadLockedProductRepository readLockedProductRepository;

    private final WriteLockedProductRepository writeLockedProductRepository;

    public Optional<ReadLockedProduct> readReadLockedProduct(final String name) {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Optional<ReadLockedProduct> byName = readLockedProductRepository.findByName(name);
        transaction.commit();
        emf.close();
        return byName;
    }

    public Optional<WriteLockedProduct> readWriteLockedProduct(final String name) {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Optional<WriteLockedProduct> byName = writeLockedProductRepository.findByName(name);
        transaction.commit();
        emf.close();
        return byName;
    }
}
