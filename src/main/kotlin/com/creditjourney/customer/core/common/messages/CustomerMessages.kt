package com.creditjourney.customer.core.common.messages

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

object CustomerMessages {
    const val CUSTOMER_NAME_MUST_NOT_BE_BLANK = "Customer name must not be blank"

    const val DOCUMENT_MUST_NOT_BE_BLANK = "Document must not be blank"
    const val DOCUMENT_MUST_CONTAIN_ONLY_DIGITS = "Document must contain only digits"
    const val DOCUMENT_MUST_HAVE_ELEVEN_DIGITS = "Document must have 11 digits"

    const val EMAIL_MUST_NOT_BE_BLANK = "Email must not be blank"
    const val EMAIL_MUST_BE_VALID = "Email must be valid"

    const val INCOME_MUST_NOT_BE_NEGATIVE = "Income must not be negative"

    const val CUSTOMER_NOT_FOUND_WITH_CUSTOMER_ID = "Customer not found with customerId"
    const val CUSTOMER_ALREADY_EXISTS_WITH = "Customer already exists with"
    const val CUSTOMER_ALREADY_EXISTS = "Customer already exists"
    const val CUSTOMER_NOT_FOUND = "Customer not found"

    const val PAGE_MUST_NOT_BE_NEGATIVE = "Page must not be negative"
    const val SIZE_MUST_BE_BETWEEN_ONE_AND_ONE_HUNDRED = "Size must be between 1 and 100"

    const val MIN_INCOME_MUST_NOT_BE_NEGATIVE = "Min income must not be negative"
    const val MAX_INCOME_MUST_NOT_BE_NEGATIVE = "Max income must not be negative"
    const val MIN_INCOME_MUST_BE_LESS_THAN_OR_EQUAL_TO_MAX_INCOME =
        "Min income must be less than or equal to max income"

    const val AT_LEAST_ONE_FIELD_MUST_BE_INFORMED = "At least one field must be informed"
    const val PHONE_MUST_NOT_BE_BLANK = "Phone must not be blank"

    const val DELETE_STATUS_MUST_BE_INACTIVE_OR_BLOCKED = "Delete status must be INACTIVE or BLOCKED"

    const val INVALID_REQUEST = "Invalid request"
    const val INVALID_REQUEST_BODY = "Invalid request body"
    const val UNEXPECTED_ERROR = "Unexpected error"

    const val CUSTOMER_NAME = "Diogo Ferreira"
    const val CUSTOMER_DOCUMENT = "12345678900"
    const val CUSTOMER_EMAIL = "dio7@gmail.com"
    const val CUSTOMER_PHONE = "11999999999"
    const val BLANK_PHONE: String = ""

    val CUSTOMER_INCOME: BigDecimal = BigDecimal("4500.00")
    val CUSTOMER_ID: UUID = UUID.fromString("11111111-1111-1111-1111-111111111111")
    val CUSTOMER_CREATED_AT: LocalDateTime = LocalDateTime.of(2026, 1, 1, 0, 0)

    const val CUSTOMER_NAME_UPDATED = "Diogo Sousa"
    const val CUSTOMER_EMAIL_UPDATED = "ferreira17@gmail.com"
    const val CUSTOMER_PHONE_UPDATED = "33888888888"

    val CUSTOMER_INCOME_UPDATED: BigDecimal = BigDecimal("7835.00")
}
