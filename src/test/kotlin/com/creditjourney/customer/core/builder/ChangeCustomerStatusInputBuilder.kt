package com.creditjourney.customer.core.builder

import com.creditjourney.customer.core.common.messages.CustomerMessages.CUSTOMER_ID
import com.creditjourney.customer.core.domain.model.CustomerStatus
import com.creditjourney.customer.core.domain.model.CustomerStatus.ACTIVE
import com.creditjourney.customer.core.port.input.ChangeCustomerStatusInput
import java.util.UUID

fun buildChangeCustomerStatusInput(
    customerId: UUID = CUSTOMER_ID,
    status: CustomerStatus = ACTIVE
): ChangeCustomerStatusInput =
    ChangeCustomerStatusInput(
        customerId = customerId,
        status = status
    )