package pl.lotto.infrastructure;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.lotto.Ticket;

@Repository
public interface DummyRecords extends MongoRepository<Ticket, String> {
}
