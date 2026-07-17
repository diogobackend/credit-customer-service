package com.creditjourney.customer.core.builder

import com.creditjourney.customer.core.domain.model.CustomerStatus
import com.creditjourney.customer.core.domain.model.CustomerStatus.INACTIVE
import com.creditjourney.customer.core.port.input.DeleteCustomerInput
import com.creditjourney.customer.core.common.messages.CustomerMessages.CUSTOMER_ID
import java.util.UUID

fun buildDeleteCustomerInput(
    customerId: UUID = CUSTOMER_ID,
    status: CustomerStatus = INACTIVE
): DeleteCustomerInput =
    DeleteCustomerInput(
        customerId = customerId,
        status = status
    )