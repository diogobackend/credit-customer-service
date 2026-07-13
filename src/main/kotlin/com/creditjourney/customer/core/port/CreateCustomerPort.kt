package com.creditjourney.customer.core.port

import com.creditjourney.customer.core.domain.model.Customer
import com.creditjourney.customer.core.port.input.CreateCustomerInput

interface CreateCustomerPort {
    fun create(input: CreateCustomerInput): Customer
}