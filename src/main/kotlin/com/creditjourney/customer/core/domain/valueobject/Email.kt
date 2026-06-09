package com.creditjourney.customer.core.domain.valueobject

data class Email(
    val value: String
) {
    init {
        require(value.isNotBlank()) {
            "Email must not be blank"
        }

        require(EMAIL_REGEX.matches(value)) {
            "Email must be valid"
        }
    }

    private companion object {
        val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
    }
}
