package com.creditjourney.customer.core.port.input

import com.creditjourney.customer.core.domain.model.Customer
import java.util.UUID

interface FindCustomerByIdPort {
    fun findById(customerId: UUID): Customer
}