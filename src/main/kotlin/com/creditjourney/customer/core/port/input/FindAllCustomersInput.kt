package com.creditjourney.customer.core.port.input

import com.creditjourney.customer.core.common.messages.CustomerMessages.MAX_INCOME_MUST_NOT_BE_NEGATIVE
import com.creditjourney.customer.core.common.messages.CustomerMessages.MIN_INCOME_MUST_BE_LESS_THAN_OR_EQUAL_TO_MAX_INCOME
import com.creditjourney.customer.core.common.messages.CustomerMessages.MIN_INCOME_MUST_NOT_BE_NEGATIVE
import com.creditjourney.customer.core.common.messages.CustomerMessages.PAGE_MUST_NOT_BE_NEGATIVE
import com.creditjourney.customer.core.common.messages.CustomerMessages.SIZE_MUST_BE_BETWEEN_ONE_AND_ONE_HUNDRED
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
        require(page >= 0) {
            PAGE_MUST_NOT_BE_NEGATIVE
        }

        require(size in 1..100) {
            SIZE_MUST_BE_BETWEEN_ONE_AND_ONE_HUNDRED
        }

        require(minIncome == null || minIncome >= BigDecimal.ZERO) {
            MIN_INCOME_MUST_NOT_BE_NEGATIVE
        }

        require(maxIncome == null || maxIncome >= BigDecimal.ZERO) {
            MAX_INCOME_MUST_NOT_BE_NEGATIVE
        }

        require(minIncome == null || maxIncome == null || minIncome <= maxIncome) {
            MIN_INCOME_MUST_BE_LESS_THAN_OR_EQUAL_TO_MAX_INCOME
        }
    }
}