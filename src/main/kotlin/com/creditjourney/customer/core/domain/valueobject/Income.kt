package com.creditjourney.customer.core.domain.valueobject

import com.creditjourney.customer.core.common.messages.CustomerMessages.INCOME_MUST_NOT_BE_NEGATIVE
import java.math.BigDecimal

data class Income(
    val value: BigDecimal,
) {
    init {
        require(value >= BigDecimal.ZERO) {
            INCOME_MUST_NOT_BE_NEGATIVE
        }
    }
}
