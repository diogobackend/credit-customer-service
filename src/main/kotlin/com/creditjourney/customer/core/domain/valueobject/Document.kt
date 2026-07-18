package com.creditjourney.customer.core.domain.valueobject

import com.creditjourney.customer.core.common.messages.CustomerMessages.DOCUMENT_MUST_CONTAIN_ONLY_DIGITS
import com.creditjourney.customer.core.common.messages.CustomerMessages.DOCUMENT_MUST_HAVE_ELEVEN_DIGITS
import com.creditjourney.customer.core.common.messages.CustomerMessages.DOCUMENT_MUST_NOT_BE_BLANK

data class Document(
    val value: String,
) {
    init {
        require(value.isNotBlank()) {
            DOCUMENT_MUST_NOT_BE_BLANK
        }

        require(value.all { it.isDigit() }) {
            DOCUMENT_MUST_CONTAIN_ONLY_DIGITS
        }

        require(value.length == 11) {
            DOCUMENT_MUST_HAVE_ELEVEN_DIGITS
        }
    }
}
