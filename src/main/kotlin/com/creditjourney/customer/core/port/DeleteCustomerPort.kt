package com.creditjourney.customer.core.port

import java.util.UUID

interface DeleteCustomerPort {
    fun delete(customerId: UUID)
}
