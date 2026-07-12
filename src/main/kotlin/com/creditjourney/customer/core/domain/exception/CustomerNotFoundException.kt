package com.creditjourney.customer.core.domain.exception

import java.util.UUID

class CustomerNotFoundException(
    customerId: UUID
) : RuntimeException("Customer not found with customerId: $customerId")