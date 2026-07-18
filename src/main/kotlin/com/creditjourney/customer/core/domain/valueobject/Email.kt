package com.creditjourney.customer.core.domain.valueobject

import com.creditjourney.customer.core.common.messages.CustomerMessages.EMAIL_MUST_BE_VALID
import com.creditjourney.customer.core.common.messages.CustomerMessages.EMAIL_MUST_NOT_BE_BLANK

data class Email(
    val value: String,
) {
    init {
        require(value.isNotBlank()) {
            EMAIL_MUST_NOT_BE_BLANK
        }

        require(EMAIL_REGEX.matches(value)) {
            EMAIL_MUST_BE_VALID
        }
    }

    private companion object {
        val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
    }
}
