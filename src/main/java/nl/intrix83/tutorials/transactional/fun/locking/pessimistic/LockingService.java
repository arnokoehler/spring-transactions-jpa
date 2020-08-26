package nl.intrix83.tutorials.transactional.fun.locking.pessimistic;

import java.util.Optional;
import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.transaction.Transactional;

import nl.intrix83.tutorials.transactional.fun.locking.pessimistic.read.ReadLockedProduct;
import nl.intrix83.tutorials.transactional.fun.locking.pessimistic.read.ReadLockedProductRepository;
import nl.intrix83.tutorials.transactional.fun.locking.pessimistic.write.WriteLockedProduct;
import nl.intrix83.tutorials.transactional.fun.locking.pessimistic.write.WriteLockedProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LockingService {

    private final EntityManagerFactory emf;

    private final ReadLockedProductRepository readLockedProductRepository;

    private final WriteLockedProductRepository writeLockedProductRepository;

    public Optional<ReadLockedProduct> findProductAndLockIt(final String name, final int millis) throws InterruptedException {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Optional<ReadLockedProduct> byName = readLockedProductRepository.findByName(name);
        Thread.sleep(millis);
        transaction.commit();
        emf.close();
        return byName;
    }


    public Optional<WriteLockedProduct> findProductAndLockItByWriting(final String name, final String newName, final int millis) throws InterruptedException {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Optional<WriteLockedProduct> byName = writeLockedProductRepository.findByName(name);
        byName.map(replaceName(newName));
        Thread.sleep(millis);
        transaction.commit();
        emf.close();
        return byName;
    }

    @org.jetbrains.annotations.NotNull
    private Function<WriteLockedProduct, WriteLockedProduct> replaceName(final String newName) {
        return it -> {
            it.setName(newName);
            return it;
        };
    }

}
