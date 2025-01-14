package com.eazybytes.accounts.exception;

import com.eazybytes.accounts.dto.ErrorResponseDto;
import lombok.Data;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> validatedErrorMap = new LinkedHashMap<>();
        List<FieldError> validatedErrorList = ex.getBindingResult().getFieldErrors();
        validatedErrorList.forEach(FieldError -> {
            String fieldName = FieldError.getField();
            String errorMessage = FieldError.getDefaultMessage();
            validatedErrorMap.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(validatedErrorMap, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(InvalidInputException.class)
    @Order(1)
    public ResponseEntity<ErrorResponseDto> handleCustomerExistException(
            InvalidInputException customerExistException, WebRequest webRequest){
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.BAD_REQUEST, customerExistException.getMessage(), getDate());
        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @Order(2)
    public ResponseEntity<ErrorResponseDto> handleCustomerNotFoundException(
            ResourceNotFoundException customerNotFoundException, WebRequest webRequest){
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.NOT_FOUND, customerNotFoundException.getMessage(), getDate());
        return new ResponseEntity<>(errorResponseDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    @Order(3)
    public ResponseEntity<ErrorResponseDto> handleGlobalException(
            Exception exception, WebRequest webRequest){
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), getDate());
        return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public String getDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
        return formatter.format(new Date());
    }
}
