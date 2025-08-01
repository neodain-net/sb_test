package com.neodain.springbootbatchdemo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// 이 클래스는 전역 예외 처리기를 구현하기 위한 기본 구조이다.

// 이 클래스에 필요한 예외 처리 메서드를 추가하여 전역 예외 처리를 구현할 수 있다.
// 예외 처리 메서드는 @ExceptionHandler 어노테이션을 사용하여 특정 예외를 처리하거나
// @ControllerAdvice 어노테이션을 사용하여 모든 컨트롤러에서 발생하는 예외를 처리할 수 있다.
// 예외 처리 메서드는 ResponseEntity를 반환하여 HTTP 상태 코드와 응답 본문을 설정할 수 있다.
// 예외 처리 메서드의 예:
// @ExceptionHandler(AlreadyExistsException.class)
// public ResponseEntity<String> handleAlreadyExistsException(AlreadyExistsException ex) {
//     return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
// }
// @ExceptionHandler(NotFoundException.class)
// public ResponseEntity<String> handleNotFoundException(NotFoundException ex) {
//     return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
//
// }
// @ExceptionHandler(Exception.class)
// public ResponseEntity<String> handleException(Exception ex) {
//     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + ex.getMessage());
// }
    
@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle NotFoundException : 예외가 발생했을 때, 404 Not Found 상태 코드를 반환합니다.(NotFoundException class 참조)
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Void> handleNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
    }

    // Handle AlreadyExistsException : 예외가 발생했을 때, 409 Conflict 상태 코드를 반환합니다.(AlreadyExistsException class 참조)
    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<String> handleAlreadyExistsException(AlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage()); // 409 Conflict
    }

    // Handle all other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage()); // 400 Bad Request
    }
}  
