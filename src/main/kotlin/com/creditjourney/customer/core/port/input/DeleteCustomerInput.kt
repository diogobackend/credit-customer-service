package com.creditjourney.customer.core.port.input

import com.creditjourney.customer.core.common.messages.CustomerMessages.DELETE_STATUS_MUST_BE_INACTIVE_OR_BLOCKED
import com.creditjourney.customer.core.domain.model.CustomerStatus
import java.util.UUID

data class DeleteCustomerInput(
    val customerId: UUID,
    val status: CustomerStatus = CustomerStatus.INACTIVE
) {
    init {
        require(status == CustomerStatus.INACTIVE || status == CustomerStatus.BLOCKED) {
            DELETE_STATUS_MUST_BE_INACTIVE_OR_BLOCKED
        }
    }
}