package com.creditjourney.customer.core.port.input

import java.math.BigDecimal
import java.util.UUID

data class UpdateCustomerInput(
    val customerId: UUID,
    val name: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val income: BigDecimal? = null
) {
    init {
        require(
            name != null ||
                    email != null ||
                    phone != null ||
                    income != null
        ) {
            "At least one field must be informed"
        }

        require(phone == null || phone.isNotBlank()) {
            "Phone must not be blank"
        }
    }
}