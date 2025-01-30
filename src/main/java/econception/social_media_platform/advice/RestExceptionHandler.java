package econception.social_media_platform.advice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RequiredArgsConstructor
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler
{
    @ExceptionHandler(value = {IllegalArgumentException.class})
    protected ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException exception) {
        log.error("Exception occurred: {}", exception.toString());
        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(exception.getMessage());
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<String> handleALLException(Exception exception)
    {
        log.error("Exception occurred: {}", exception.toString());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    }


}
