package pl.lotto.infrastructure.numberannoucer.error;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import pl.lotto.domain.resultchecker.PlayerResultNotFoundException;

@ControllerAdvice
@Log4j2
public class ResultAnnouncerControllerErrorHandler {

    public ResultAnnouncerErrorResponse handlePlayerResultNotFound(PlayerResultNotFoundException exception) {
        String message = exception.getMessage();
        log.error(message);
        return new ResultAnnouncerErrorResponse(message, HttpStatus.NOT_FOUND);
    }

}
