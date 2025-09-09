package pl.lotto.domain.numberreceiver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.lotto.domain.AdjustableClock;
import pl.lotto.domain.numberreceiver.dto.NumberReceiverResponseDto;
import pl.lotto.domain.numberreceiver.dto.TicketDto;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class NumberReceiverFacadeTest {

    private final TicketRepository ticketRepository = new TicketRepositoryTestImpl();
    private final Clock clock = Clock.systemUTC();
    private DrawDateGenerator drawDateGenerator;

    @BeforeEach
    void setUp(){
        drawDateGenerator = new DrawDateGenerator(clock);
    }

    @Test
    public void it_should_return_correct_response_when_user_input_six_numbers_in_range() {
        // given
        HashGenerable hashGenerator = new HashGeneratorTestImpl();
        NumberValidator numberValidator = new NumberValidator();

        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration()
                .numberReceiverFacade(numberValidator,
                        drawDateGenerator,
                        hashGenerator,
                        ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);
        DrawDateGenerator drawDateGenerator = new DrawDateGenerator(clock);
        LocalDateTime nextDrawDate = drawDateGenerator.getNextDrawDate();

        TicketDto generatedTicket = TicketDto.builder()
                .hash(hashGenerator.getHash())
                .numbers(numbersFromUser)
                .drawDate(nextDrawDate)
                .build();

        // when
        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(numbersFromUser);

        // then
        NumberReceiverResponseDto expectedResponse = new NumberReceiverResponseDto(generatedTicket, ValidationResult.INPUT_SUCCESS.info);
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    public void it_should_return_failed_message_when_user_input_six_numbers_but_one_number_is_out_of_range() {
        // given
        HashGenerator hashGenerator = new HashGenerator();
        NumberValidator numberValidator = new NumberValidator();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration()
                .numberReceiverFacade(numberValidator,
                        drawDateGenerator,
                        hashGenerator,
                        ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 100);

        // when
        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(numbersFromUser);

        // then
        NumberReceiverResponseDto expectedResponse = new NumberReceiverResponseDto(null, ValidationResult.NOT_IN_RANGE.info);
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    public void it_should_return_failed_message_when_user_input_six_numbers_but_one_number_is_out_of_range_and_is_negative() {
        // given
        HashGenerator hashGenerator = new HashGenerator();
        NumberValidator numberValidator = new NumberValidator();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration()
                .numberReceiverFacade(numberValidator,
                        drawDateGenerator,
                        hashGenerator,
                        ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, -4, 5, 6);

        // when
        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(numbersFromUser);

        // then
        NumberReceiverResponseDto expectedResponse = new NumberReceiverResponseDto(null, ValidationResult.NOT_IN_RANGE.info);
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    public void it_should_return_failed_message_when_user_input_less_than_six_numbers() {
        // given
        HashGenerator hashGenerator = new HashGenerator();
        NumberValidator numberValidator = new NumberValidator();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration()
                .numberReceiverFacade(numberValidator,
                        drawDateGenerator,
                        hashGenerator,
                        ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5);

        // when
        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(numbersFromUser);

        // then
        NumberReceiverResponseDto expectedResponse = new NumberReceiverResponseDto(null, ValidationResult.NOT_SIX_NUMBERS_GIVEN.info);
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    public void it_should_return_failed_message_when_user_input_more_than_six_numbers() {
        // given
        HashGenerator hashGenerator = new HashGenerator();
        NumberValidator numberValidator = new NumberValidator();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration()
                .numberReceiverFacade(numberValidator,
                        drawDateGenerator,
                        hashGenerator,
                        ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6, 7);

        // when
        NumberReceiverResponseDto response = numberReceiverFacade.inputNumbers(numbersFromUser);

        // then
        NumberReceiverResponseDto expectedResponse = new NumberReceiverResponseDto(null, ValidationResult.NOT_SIX_NUMBERS_GIVEN.info);
        assertThat(response).isEqualTo(expectedResponse);
    }

    @Test
    public void it_should_return_correct_hash() {
        // given
        HashGenerator hashGenerator = new HashGenerator();
        NumberValidator numberValidator = new NumberValidator();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration()
                .numberReceiverFacade(numberValidator,
                        drawDateGenerator,
                        hashGenerator,
                        ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);

        // when
        String response = numberReceiverFacade.inputNumbers(numbersFromUser).ticketDto().hash();

        // then
        assertThat(response).hasSize(36);
        assertThat(response).isNotNull();
    }

    @Test
    public void it_should_return_correct_draw_date() {
        // given
        Clock clock = Clock.fixed(LocalDateTime.of(2022, 11, 19, 10, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.of("Europe/London"));
        HashGenerator hashGenerator = new HashGenerator();
        NumberValidator numberValidator = new NumberValidator();
        DrawDateGenerator drawDateGenerator = new DrawDateGenerator(clock);
        TicketRepositoryTestImpl ticketRepository = new TicketRepositoryTestImpl();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration()
                .numberReceiverFacade(numberValidator,
                        drawDateGenerator,
                        hashGenerator,
                        ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);

        // when
        LocalDateTime testedDrawDate = numberReceiverFacade.inputNumbers(numbersFromUser).ticketDto().drawDate();

        // then
        LocalDateTime expectedDrawDate = LocalDateTime.of(2022, 11, 19, 12, 0, 0);
        assertThat(testedDrawDate).isEqualTo(expectedDrawDate);
    }

    @Test
    public void it_should_return_next_Saturday_draw_date_when_date_is_Saturday_noon() {
        // given
        Clock clock = Clock.fixed(LocalDateTime.of(2022, 11, 19, 12, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.of("Europe/London"));
        HashGenerator hashGenerator = new HashGenerator();
        NumberValidator numberValidator = new NumberValidator();
        DrawDateGenerator drawDateGenerator = new DrawDateGenerator(clock);
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration()
                .numberReceiverFacade(numberValidator,
                        drawDateGenerator,
                        hashGenerator,
                        ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);

        // when
        LocalDateTime testedDrawDate = numberReceiverFacade.inputNumbers(numbersFromUser).ticketDto().drawDate();

        // then

        LocalDateTime expectedDrawDate = LocalDateTime.of(2022, 11, 26, 12, 0, 0);

        assertThat(testedDrawDate).isEqualTo(expectedDrawDate);
    }

    @Test
    public void it_should_return_next_Saturday_draw_date_when_date_is_Saturday_afternoon() {
        // given
        Clock clock = Clock.fixed(LocalDateTime.of(2022, 11, 19, 14, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.of("Europe/London"));
        HashGenerable hashGenerator = new HashGenerator();
        DrawDateGenerator drawDateGenerator = new DrawDateGenerator(clock);
        NumberValidator numberValidator = new NumberValidator();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration()
                .numberReceiverFacade(numberValidator,
                        drawDateGenerator,
                        hashGenerator,
                        ticketRepository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);

        // when
        LocalDateTime testedDrawDate = numberReceiverFacade.inputNumbers(numbersFromUser).ticketDto().drawDate();

        // then

        LocalDateTime expectedDrawDate = LocalDateTime.of(2022, 11, 26, 12, 0, 0);

        assertThat(testedDrawDate).isEqualTo(expectedDrawDate);
    }

    @Test
    public void it_should_return_tickets_with_correct_draw_date() {
        // given

        Instant fixedInstant = LocalDateTime.of(2022, 12, 15, 12, 0, 0).toInstant(ZoneOffset.UTC);
        ZoneId of = ZoneId.of("Europe/London");
        AdjustableClock clock = new AdjustableClock(fixedInstant, of);

        HashGenerable hashGenerator = new HashGenerator();
        DrawDateGenerator drawDateGenerator = new DrawDateGenerator(clock);
        NumberValidator numberValidator = new NumberValidator();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration()
                .numberReceiverFacade(numberValidator,
                        drawDateGenerator,
                        hashGenerator,
                        ticketRepository);
        NumberReceiverResponseDto numberReceiverResponseDto = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6));
        clock.plusDays(1);
        NumberReceiverResponseDto numberReceiverResponseDto1 = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6));
        clock.plusDays(1);
        NumberReceiverResponseDto numberReceiverResponseDto2 = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6));
        clock.plusDays(1);
        NumberReceiverResponseDto numberReceiverResponseDto3 = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6));
        TicketDto ticketDto = numberReceiverResponseDto.ticketDto();
        TicketDto ticketDto1 = numberReceiverResponseDto1.ticketDto();
        LocalDateTime drawDate = numberReceiverResponseDto.ticketDto().drawDate();
        // when
        List<TicketDto> allTicketsByDate = numberReceiverFacade.retrieveAllTicketsByNextDrawDate(drawDate);
        // then
        assertThat(allTicketsByDate).containsOnly(ticketDto, ticketDto1);
    }

    @Test
    public void it_should_return_empty_collections_if_there_are_no_tickets() {
        // given
        Clock clock = Clock.fixed(LocalDateTime.of(2022, 12, 15, 12, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.of("Europe/London"));
        LocalDateTime drawDate = LocalDateTime.now(clock);

        HashGenerable hashGenerator = new HashGenerator();
        DrawDateGenerator drawDateGenerator = new DrawDateGenerator(clock);
        NumberValidator numberValidator = new NumberValidator();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration()
                .numberReceiverFacade(numberValidator,
                        drawDateGenerator,
                        hashGenerator,
                        ticketRepository);
        // when
        List<TicketDto> allTicketsByDate = numberReceiverFacade.retrieveAllTicketsByNextDrawDate(drawDate);
        // then
        assertThat(allTicketsByDate).isEmpty();
    }

    @Test
    public void it_should_return_empty_collections_if_given_date_is_after_next_drawDate() {
        // given
        Clock clock = Clock.fixed(LocalDateTime.of(2022, 12, 15, 12, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.of("Europe/London"));

        HashGenerable hashGenerator = new HashGenerator();
        DrawDateGenerator drawDateGenerator = new DrawDateGenerator(clock);
        NumberValidator numberValidator = new NumberValidator();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration()
                .numberReceiverFacade(numberValidator,
                        drawDateGenerator,
                        hashGenerator,
                        ticketRepository);
        NumberReceiverResponseDto numberReceiverResponseDto = numberReceiverFacade.inputNumbers(Set.of(1, 2, 3, 4, 5, 6));

        LocalDateTime drawDate = numberReceiverResponseDto.ticketDto().drawDate();

        // when
        List<TicketDto> allTicketsByDate = numberReceiverFacade.retrieveAllTicketsByNextDrawDate(drawDate.plusWeeks(1L));
        // then
        assertThat(allTicketsByDate).isEmpty();
    }

    @Test
    public void it_should_return_next_draw_date() {
        // given
        Clock clock = Clock.fixed(LocalDateTime.of(2022, 11, 19, 10, 0, 0).toInstant(ZoneOffset.UTC), ZoneId.of("Europe/London"));
        HashGenerable hashGenerator = new HashGenerator();
        DrawDateGenerator drawDateGenerator = new DrawDateGenerator(clock);
        NumberValidator numberValidator = new NumberValidator();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration()
                .numberReceiverFacade(numberValidator,
                        drawDateGenerator,
                        hashGenerator,
                        ticketRepository);

        // when
        LocalDateTime testedDrawDate = numberReceiverFacade.retrieveNextDrawDate();

        // then
        LocalDateTime expectedDrawDate = LocalDateTime.of(2022, 11, 19, 12, 0, 0);
        assertThat(testedDrawDate).isEqualTo(expectedDrawDate);
    }

}
