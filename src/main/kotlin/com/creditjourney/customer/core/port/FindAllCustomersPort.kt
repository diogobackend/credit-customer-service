package com.creditjourney.customer.core.port

import com.creditjourney.customer.core.domain.model.CustomerSlice
import com.creditjourney.customer.core.port.input.FindAllCustomersInput

interface FindAllCustomersPort {
    fun findAll(input: FindAllCustomersInput): CustomerSlice
}
