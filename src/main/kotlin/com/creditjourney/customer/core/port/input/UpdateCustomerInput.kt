package com.creditjourney.customer.core.port.input

import com.creditjourney.customer.core.common.messages.CustomerMessages.AT_LEAST_ONE_FIELD_MUST_BE_INFORMED
import com.creditjourney.customer.core.common.messages.CustomerMessages.PHONE_MUST_NOT_BE_BLANK
import java.math.BigDecimal
import java.util.UUID

data class UpdateCustomerInput(
    val customerId: UUID,
    val name: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val income: BigDecimal? = null,
) {
    init {
        require(
            name != null ||
                email != null ||
                phone != null ||
                income != null,
        ) {
            AT_LEAST_ONE_FIELD_MUST_BE_INFORMED
        }

        require(phone == null || phone.isNotBlank()) {
            PHONE_MUST_NOT_BE_BLANK
        }
    }
}
