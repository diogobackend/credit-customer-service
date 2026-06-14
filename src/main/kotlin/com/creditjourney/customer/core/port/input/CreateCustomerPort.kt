package com.creditjourney.customer.core.port.input

import com.creditjourney.customer.core.domain.model.Customer

interface CreateCustomerPort {
    fun create(input: CreateCustomerInput): Customer
}