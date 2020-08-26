package nl.intrix83.tutorials.transactional.fun.locking.pessimistic;

import java.util.Optional;

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

    private final ReadLockedProductRepository readLockedProductRepository;

    private final WriteLockedProductRepository writeLockedProductRepository;

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public Optional<ReadLockedProduct> readReadLockedProduct(final Long id) {
        return readLockedProductRepository.findById(id);
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public Optional<WriteLockedProduct> readWriteLockedProduct(final Long id) {
        return writeLockedProductRepository.findById(id);
    }
}
