package com.creditjourney.customer.core.domain.exception

import com.creditjourney.customer.core.common.messages.CustomerMessages.CUSTOMER_NOT_FOUND_WITH_CUSTOMER_ID
import java.util.UUID

class CustomerNotFoundException(
    customerId: UUID,
) : RuntimeException("$CUSTOMER_NOT_FOUND_WITH_CUSTOMER_ID: $customerId")
