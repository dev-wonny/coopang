package com.coopang.apiconfig.error;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<RestApiException> handleIllegalArgumentException(IllegalArgumentException ex) {
        RestApiException restApiException = new RestApiException(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(
                restApiException,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler({IllegalStateException.class})
    public ResponseEntity<RestApiException> handleIllegalStateException(IllegalStateException ex) {
        RestApiException restApiException = new RestApiException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(
                restApiException,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler({NullPointerException.class})
    public ResponseEntity<RestApiException> handleNullPointerException(NullPointerException ex) {
        RestApiException restApiException = new RestApiException(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(
                restApiException,
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<RestApiException> handleAccessDeniedException(AccessDeniedException ex) {
        RestApiException restApiException = new RestApiException(ex.getMessage(), HttpStatus.FORBIDDEN.value());
        return new ResponseEntity<>(
                restApiException,
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<RestApiException> handleUserNotFoundException(UserNotFoundException ex) {
        RestApiException restApiException = new RestApiException(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(
                restApiException,
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestApiException> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        RestApiException restApiException = new RestApiException(errorMessage, HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(restApiException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<RestApiException> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        RestApiException restApiException = new RestApiException("HTTP method not supported: " + ex.getMethod(), HttpStatus.METHOD_NOT_ALLOWED.value());
        return new ResponseEntity<>(restApiException, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<RestApiException> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
        RestApiException restApiException = new RestApiException("Media type not supported: " + ex.getContentType(), HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
        return new ResponseEntity<>(restApiException, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<RestApiException> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        RestApiException restApiException = new RestApiException("Missing required parameter: " + ex.getParameterName(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(restApiException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestApiException> handleGeneralException(Exception ex) {
        RestApiException restApiException = new RestApiException("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(restApiException, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}