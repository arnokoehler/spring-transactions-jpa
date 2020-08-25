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
@Transactional
@RequiredArgsConstructor
public class ReadingService {

    private final ReadLockedProductRepository readLockedProductRepository;

    private final WriteLockedProductRepository writeLockedProductRepository;

    public Optional<ReadLockedProduct> readReadLockedProduct(final String name) {
        return readLockedProductRepository.findByName(name);
    }

    public Optional<WriteLockedProduct> readWriteLockedProduct(final String name) {
        return writeLockedProductRepository.findByName(name);
    }
}
