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
@RequiredArgsConstructor
public class ReadingService {

    private final ReadLockedProductRepository readLockedProductRepository;

    private final WriteLockedProductRepository writeLockedProductRepository;

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public Optional<ReadLockedProduct> readReadLockedProduct(final Long id) {
        return readLockedProductRepository.findOneForUpdate(id);
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW) // logs say that find by id is not transactional but how is that possible with @Lock(LockModeType.PESSIMISTIC_READ) ?!
    public Optional<WriteLockedProduct> readWriteLockedProduct(final Long id) {
        log.info("find readWriteLockedProduct by id -> row should be locked by other service");
        Optional<WriteLockedProduct> byId = writeLockedProductRepository.findById(id);

        log.info("readWriteLockedProduct found and will be returned");
        return byId;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public Optional<ReadLockedProduct> tryToFindAndUpdateLockedProduct(final Long id, final String newProductName) {
        log.info("find readWriteLockedProduct by id -> row should be locked by other service");

        Optional<ReadLockedProduct> byId = readLockedProductRepository.findOneForUpdate(id);
        return byId.map(replaceName(newProductName));
    }

    @org.jetbrains.annotations.NotNull
    private Function<ReadLockedProduct, ReadLockedProduct> replaceName(final String newName) {
        return it -> {
            it.setName(newName);
            return readLockedProductRepository.save(it);
        };
    }
}
