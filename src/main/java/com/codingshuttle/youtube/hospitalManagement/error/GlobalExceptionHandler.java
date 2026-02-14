package com.codingshuttle.youtube.hospitalManagement.error;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle UsernameNotFoundException
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiError> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        ApiError apiError = new ApiError("User not found: " + ex.getMessage(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(apiError, apiError.getStatusCode());
    }

    // Handle Authentication Exceptions
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuthenticationException(AuthenticationException ex) {
        ApiError apiError = new ApiError("Authentication failed: " + ex.getMessage(), HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(apiError, apiError.getStatusCode());
    }

    // Handle Bad Credentials
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentialsException(BadCredentialsException ex) {
        ApiError apiError = new ApiError("Invalid username or password", HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(apiError, apiError.getStatusCode());
    }

    // Handle Access Denied
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDeniedException(AccessDeniedException ex) {
        ApiError apiError = new ApiError("Access denied: " + ex.getMessage(), HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(apiError, apiError.getStatusCode());
    }

    // Handle JWT Exceptions
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiError> handleExpiredJwtException(ExpiredJwtException ex) {
        ApiError apiError = new ApiError("JWT token has expired", HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(apiError, apiError.getStatusCode());
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ApiError> handleMalformedJwtException(MalformedJwtException ex) {
        ApiError apiError = new ApiError("Invalid JWT token format", HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(apiError, apiError.getStatusCode());
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ApiError> handleSignatureException(SignatureException ex) {
        ApiError apiError = new ApiError("JWT signature validation failed", HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(apiError, apiError.getStatusCode());
    }

    @ExceptionHandler(io.jsonwebtoken.security.WeakKeyException.class)
    public ResponseEntity<ApiError> handleWeakKeyException(io.jsonwebtoken.security.WeakKeyException ex) {
        ApiError apiError = new ApiError("JWT secret key is too weak: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(apiError, apiError.getStatusCode());
    }

    // Handle IllegalAccessException (used in register)
    @ExceptionHandler(IllegalAccessException.class)
    public ResponseEntity<ApiError> handleIllegalAccessException(IllegalAccessException ex) {
        ApiError apiError = new ApiError(ex.getMessage(), HttpStatus.CONFLICT);
        return new ResponseEntity<>(apiError, apiError.getStatusCode());
    }

    // Handle IllegalArgumentException
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgumentException(IllegalArgumentException ex) {
        ApiError apiError = new ApiError("Invalid argument: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(apiError, apiError.getStatusCode());
    }

    // Handle Validation Exceptions
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        Map<String, Object> response = new HashMap<>();
        response.put("timeStamp", java.time.LocalDateTime.now());
        response.put("statusCode", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Validation failed");
        response.put("errors", errors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Handle Missing Request Parameters
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiError> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        ApiError apiError = new ApiError("Missing required parameter: " + ex.getParameterName(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(apiError, apiError.getStatusCode());
    }

    // Handle Method Argument Type Mismatch
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        String message = String.format("Parameter '%s' should be of type %s", ex.getName(), ex.getRequiredType().getSimpleName());
        ApiError apiError = new ApiError(message, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(apiError, apiError.getStatusCode());
    }

    // Handle HTTP Method Not Supported
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiError> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        StringBuilder message = new StringBuilder();
        message.append(ex.getMethod());
        message.append(" method is not supported for this request. Supported methods are ");
        ex.getSupportedHttpMethods().forEach(t -> message.append(t).append(" "));
        ApiError apiError = new ApiError(message.toString(), HttpStatus.METHOD_NOT_ALLOWED);
        return new ResponseEntity<>(apiError, apiError.getStatusCode());
    }

    // Handle No Handler Found Exception
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiError> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        ApiError apiError = new ApiError("No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(apiError, apiError.getStatusCode());
    }

    // Handle NullPointerException
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiError> handleNullPointerException(NullPointerException ex) {
        ApiError apiError = new ApiError("Null pointer exception occurred. Please check your request data.", HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(apiError, apiError.getStatusCode());
    }

    // Handle Database/Data Access Exceptions
    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolationException(org.springframework.dao.DataIntegrityViolationException ex) {
        ApiError apiError = new ApiError("Data integrity violation: " + ex.getMessage(), HttpStatus.CONFLICT);
        return new ResponseEntity<>(apiError, apiError.getStatusCode());
    }

    @ExceptionHandler(org.springframework.dao.EmptyResultDataAccessException.class)
    public ResponseEntity<ApiError> handleEmptyResultDataAccessException(org.springframework.dao.EmptyResultDataAccessException ex) {
        ApiError apiError = new ApiError("No data found for the requested operation", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(apiError, apiError.getStatusCode());
    }

    // Handle Generic Exception (catch-all)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception ex) {
        ApiError apiError = new ApiError("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        // Log the full exception stack trace for debugging
        ex.printStackTrace();
        return new ResponseEntity<>(apiError, apiError.getStatusCode());
    }
}