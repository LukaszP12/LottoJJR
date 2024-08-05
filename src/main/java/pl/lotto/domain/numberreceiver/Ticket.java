package pl.lotto.domain.numberreceiver;

import java.time.LocalDateTime;
import java.util.Set;

// encja do bazy
record Ticket(String ticketId, LocalDateTime drawDate, Set<Integer> numbersFromUser) {
}
