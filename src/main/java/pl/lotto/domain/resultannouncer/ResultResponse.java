package pl.lotto.domain.resultannouncer;

import java.time.LocalDateTime;
import java.util.Set;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Document
record ResultResponse(
        @Id String hash,
        Set<Integer> numbers,
        Set<Integer> wonNumbers,
        Set<Integer> hitNumbers,
        LocalDateTime drawDate,
        boolean isWinner,
        @Indexed(expireAfterSeconds = 10)
        LocalDateTime createdDate
) {
}
