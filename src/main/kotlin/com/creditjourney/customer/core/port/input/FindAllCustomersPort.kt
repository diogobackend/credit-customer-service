package com.creditjourney.customer.core.port.input

import com.creditjourney.customer.core.domain.model.CustomerSlice

interface FindAllCustomersPort {
    fun findAll(input: FindAllCustomersInput): CustomerSlice
}