package com.creditjourney.customer.core.domain.valueobject

import java.math.BigDecimal

data class Income(
    val value: BigDecimal
) {
    init {
        require(value >= BigDecimal.ZERO) {
            "Income must not be negative"
        }
    }
}
