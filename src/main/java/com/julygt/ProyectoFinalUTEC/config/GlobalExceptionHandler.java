package com.julygt.ProyectoFinalUTEC.config;

import com.julygt.ProyectoFinalUTEC.Producto.ProductoException;
import com.julygt.ProyectoFinalUTEC.auth.AuthDTO;
import com.julygt.ProyectoFinalUTEC.auth.AuthException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthException.UserAlreadyExistsException.class)
    public ResponseEntity<AuthDTO.ErrorResponse> handleUserAlreadyExists(AuthException.UserAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new AuthDTO.ErrorResponse("USER_ALREADY_EXISTS", e.getMessage(), 409));
    }

    @ExceptionHandler(AuthException.InvalidCredentialsException.class)
    public ResponseEntity<AuthDTO.ErrorResponse> handleInvalidCredentials(AuthException.InvalidCredentialsException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new AuthDTO.ErrorResponse("INVALID_CREDENTIALS", e.getMessage(), 401));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<AuthDTO.ErrorResponse> handleGenericException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new AuthDTO.ErrorResponse("INTERNAL_ERROR", "Error interno del servidor", 500));
    }

    // PRODUCTOS

    @ExceptionHandler(ProductoException.NoAutorizadoException.class)
    public ResponseEntity<AuthDTO.ErrorResponse> handleProductoNoAutorizado(ProductoException.NoAutorizadoException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new AuthDTO.ErrorResponse("PRODUCT_NOT_AUTHORIZED", e.getMessage(), 401));
    }

    @ExceptionHandler(ProductoException.AccesoProhibidoException.class)
    public ResponseEntity<AuthDTO.ErrorResponse> handleProductoAccesoProhibido(ProductoException.AccesoProhibidoException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new AuthDTO.ErrorResponse("PRODUCT_ACCESS_FORBIDDEN", e.getMessage(), 403));
    }

    @ExceptionHandler(ProductoException.NoEncontradoException.class)
    public ResponseEntity<AuthDTO.ErrorResponse> handleProductoNoEncontrado(ProductoException.NoEncontradoException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new AuthDTO.ErrorResponse("PRODUCT_NOT_FOUND", e.getMessage(), 404));
    }

    @ExceptionHandler(ProductoException.DatosInvalidosException.class)
    public ResponseEntity<AuthDTO.ErrorResponse> handleProductoDatosInvalidos(ProductoException.DatosInvalidosException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new AuthDTO.ErrorResponse("PRODUCT_BAD_REQUEST", e.getMessage(), 400));
    }

}