package nl.intrix83.tutorials.transactional.fun.locking.pessimistic;

import java.util.Optional;
import java.util.function.Function;

import javax.transaction.Transactional;

import nl.intrix83.tutorials.transactional.fun.locking.pessimistic.read.ReadLockedProduct;
import nl.intrix83.tutorials.transactional.fun.locking.pessimistic.read.ReadLockedProductRepository;
import nl.intrix83.tutorials.transactional.fun.locking.pessimistic.write.WriteLockedProduct;
import nl.intrix83.tutorials.transactional.fun.locking.pessimistic.write.WriteLockedProductRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class LockingService {

    private final ReadLockedProductRepository readLockedProductRepository;

    private final WriteLockedProductRepository writeLockedProductRepository;

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public Optional<ReadLockedProduct> findProductAndLockIt(final String name, final int millis) throws InterruptedException {
        Optional<ReadLockedProduct> byName = readLockedProductRepository.findByName(name);
        Thread.sleep(millis);
        return byName;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public Optional<WriteLockedProduct> findProductAndLockItByWriting(final String name, final String newName, final int millis) throws InterruptedException {
        Optional<WriteLockedProduct> byName = writeLockedProductRepository.findByName(name);

        log.info("start modifying data LOCK expected to kick in now!");
        Optional<WriteLockedProduct> writeLockedProduct = byName.map(replaceName(newName));

        log.info("thread will sleep transaction should stay open locking row");
        Thread.sleep(millis);

        log.info("sleep is done");
        return writeLockedProduct;
    }

    @org.jetbrains.annotations.NotNull
    private Function<WriteLockedProduct, WriteLockedProduct> replaceName(final String newName) {
        return it -> {
            it.setName(newName);
            return it;
        };
    }

}
