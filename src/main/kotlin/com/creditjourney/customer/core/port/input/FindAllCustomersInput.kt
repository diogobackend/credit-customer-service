package com.creditjourney.customer.core.port.input

import com.creditjourney.customer.core.domain.model.CustomerStatus
import java.math.BigDecimal

data class FindAllCustomersInput(
    val page: Int = 0,
    val size: Int = 30,
    val status: CustomerStatus? = null,
    val search: String? = null,
    val name: String? = null,
    val minIncome: BigDecimal? = null,
    val maxIncome: BigDecimal? = null
) {
    init {
        require(page >= 0) { "Page must not be negative" }
        require(size in 1..100) { "Size must be between 1 and 100" }

        require(minIncome == null || minIncome >= BigDecimal.ZERO) {
            "Min income must not be negative"
        }

        require(maxIncome == null || maxIncome >= BigDecimal.ZERO) {
            "Max income must not be negative"
        }

        require(minIncome == null || maxIncome == null || minIncome <= maxIncome) {
            "Min income must be less than or equal to max income"
        }
    }
}