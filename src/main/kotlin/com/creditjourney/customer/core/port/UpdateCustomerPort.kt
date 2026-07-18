package com.creditjourney.customer.core.port

import com.creditjourney.customer.core.domain.model.Customer
import com.creditjourney.customer.core.port.input.UpdateCustomerInput

interface UpdateCustomerPort {
    fun update(input: UpdateCustomerInput): Customer
}
