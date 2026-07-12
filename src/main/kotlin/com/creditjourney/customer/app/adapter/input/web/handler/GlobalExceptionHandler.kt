package com.creditjourney.customer.app.adapter.input.web.handler

import com.creditjourney.customer.app.adapter.input.web.responses.ErrorResponse
import com.creditjourney.customer.core.domain.exception.CustomerAlreadyExistsException
import com.creditjourney.customer.core.domain.exception.CustomerNotFoundException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    private companion object {
        const val CUSTOMER_ALREADY_EXISTS = "Customer already exists"
        const val INVALID_REQUEST = "Invalid request"
        const val INVALID_REQUEST_BODY = "Invalid request body"
        const val UNEXPECTED_ERROR = "Unexpected error"
        const val CUSTOMER_NOT_FOUND = "Customer not found"
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(
        exception: MethodArgumentNotValidException
    ): ResponseEntity<ErrorResponse> {
        val message = exception.bindingResult.fieldErrors
            .joinToString("; ") { "${it.field}: ${it.defaultMessage}" }

        return errorResponse(
            status = HttpStatus.BAD_REQUEST,
            message = message
        )
    }

    @ExceptionHandler(CustomerAlreadyExistsException::class)
    fun handleCustomerAlreadyExistsException(
        exception: CustomerAlreadyExistsException
    ): ResponseEntity<ErrorResponse> =
        errorResponse(
            status = HttpStatus.CONFLICT,
            message = exception.message ?: CUSTOMER_ALREADY_EXISTS
        )

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrityViolationException(): ResponseEntity<ErrorResponse> =
        errorResponse(
            status = HttpStatus.CONFLICT,
            message = CUSTOMER_ALREADY_EXISTS
        )

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(
        exception: IllegalArgumentException
    ): ResponseEntity<ErrorResponse> =
        errorResponse(
            status = HttpStatus.BAD_REQUEST,
            message = exception.message ?: INVALID_REQUEST
        )

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(): ResponseEntity<ErrorResponse> =
        errorResponse(
            status = HttpStatus.BAD_REQUEST,
            message = INVALID_REQUEST_BODY
        )

    @ExceptionHandler(Exception::class)
    fun handleGenericException(): ResponseEntity<ErrorResponse> =
        errorResponse(
            status = HttpStatus.INTERNAL_SERVER_ERROR,
            message = UNEXPECTED_ERROR
        )

    @ExceptionHandler(CustomerNotFoundException::class)
    fun handleCustomerNotFoundException(
        exception: CustomerNotFoundException
    ): ResponseEntity<ErrorResponse> =
        errorResponse(
            status = HttpStatus.NOT_FOUND,
            message = exception.message ?: CUSTOMER_NOT_FOUND
        )

    private fun errorResponse(
        status: HttpStatus,
        message: String
    ): ResponseEntity<ErrorResponse> =
        ResponseEntity
            .status(status)
            .body(
                ErrorResponse(
                    status = status.value(),
                    error = status.reasonPhrase,
                    message = message
                )
            )
}