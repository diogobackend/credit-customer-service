package com.creditjourney.customer.core.port.input

import com.creditjourney.customer.core.domain.model.CustomerStatus
import java.util.UUID

data class ChangeCustomerStatusInput(
    val customerId: UUID,
    val status: CustomerStatus
)