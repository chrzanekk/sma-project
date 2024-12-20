package pl.com.chrzanowski.sma.common.exception.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.com.chrzanowski.sma.common.exception.CustomException;
import pl.com.chrzanowski.sma.common.exception.ForbiddenException;
import pl.com.chrzanowski.sma.common.exception.UnauthorizedException;

import java.time.LocalDateTime;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> runtimeExceptionHandling(Exception e, WebRequest request) {
        if (e instanceof AuthenticationException || e instanceof AccessDeniedException) {
            log.error("SecurityException: ", e);
            throw (RuntimeException) e;
        }
        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(LocalDateTime.now())
                .code("RUNTIME_EXCEPTION")
                .message(e.getMessage())
                .details(Map.of("description", request.getDescription(false))).build();
        log.debug("Error: ", e);
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException e, WebRequest request) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(LocalDateTime.now())
                .code(ErrorCode.INVALID_ARGUMENT.getCode())
                .message(e.getMessage())
                .details(Map.of("description", request.getDescription(false)))
                .build();
        log.error("IllegalArgumentException: ", e);
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(LocalDateTime.now())
                .code(ErrorCode.MALFORMED_JSON.getCode())
                .message("Invalid JSON format")
                .details(Map.of("description", request.getDescription(false)))
                .build();
        log.error("HttpMessageNotReadableException: ", ex);
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception e, WebRequest request) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(LocalDateTime.now())
                .code(ErrorCode.GENERIC_ERROR.getCode())
                .message("An unexpected error occurred")
                .details(Map.of("description", request.getDescription(false)))
                .build();
        log.error("Exception: ", e);
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleCustomException(CustomException ex, WebRequest request) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(LocalDateTime.now())
                .code(ex.getErrorCode().getCode())
                .message(ex.getMessage())
                .details(ex.getDetails())
                .build();

        HttpStatus httpStatus;
        if (ex instanceof ForbiddenException) {
            httpStatus = HttpStatus.FORBIDDEN;
        } else if (ex instanceof UnauthorizedException) {
            httpStatus = HttpStatus.UNAUTHORIZED;
        } else {
            httpStatus = HttpStatus.BAD_REQUEST;
        }
        log.error("CustomException: ", ex);
        return ResponseEntity.status(httpStatus).body(errorDetails);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorDetails> handleUnauthorizedException(UnauthorizedException ex) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(LocalDateTime.now())
                .code(ex.getErrorCode().getCode())
                .message(ex.getMessage())
                .details(ex.getDetails())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDetails);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorDetails> handleForbiddenException(ForbiddenException ex) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(LocalDateTime.now())
                .code(ex.getErrorCode().getCode())
                .message(ex.getMessage())
                .details(ex.getDetails())
                .build();
        log.error("ForbiddenException: ", ex);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDetails);
    }

}
