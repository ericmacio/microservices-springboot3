package eric.util.http;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import eric.api.exceptions.InvalidInputException;
import eric.api.exceptions.NotFoundException;

@RestControllerAdvice //Registered as a Spring Bean for a global use of ExceptionHandler in our RestControllers
//Just need to return a java body object instead of a ResponseEntity
class GlobalControllerExceptionHandler {

  private static final Logger LOG = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

  @ResponseStatus(NOT_FOUND) //specify the HTTP status code of the response
  @ExceptionHandler(NotFoundException.class)
  public @ResponseBody HttpErrorInfo handleNotFoundExceptions(ServerHttpRequest request, NotFoundException ex) {
    LOG.warn("handleNotFoundExceptions. Build HTTP NOT_FOUND response");
    return createHttpErrorInfo(NOT_FOUND, request, ex);
  }

  @ResponseStatus(UNPROCESSABLE_ENTITY) //specify the HTTP status code of the response
  @ExceptionHandler(InvalidInputException.class)
  public @ResponseBody HttpErrorInfo handleInvalidInputException(ServerHttpRequest request, InvalidInputException ex) {
    LOG.warn("handleInvalidInputException. Build HTTP UNPROCESSABLE_ENTITY response");
    return createHttpErrorInfo(UNPROCESSABLE_ENTITY, request, ex);
  }

  private HttpErrorInfo createHttpErrorInfo(HttpStatus httpStatus, ServerHttpRequest request, Exception ex) {
    final String path = request.getPath().pathWithinApplication().value();
    final String message = ex.getMessage();
    LOG.warn("Returning HTTP status: {} for path: {}, message: {}", httpStatus, path, message);
    return new HttpErrorInfo(httpStatus, path, message);
  }
}
