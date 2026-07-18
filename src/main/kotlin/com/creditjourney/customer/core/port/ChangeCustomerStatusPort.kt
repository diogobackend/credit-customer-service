package com.creditjourney.customer.core.port

import com.creditjourney.customer.core.domain.model.Customer
import com.creditjourney.customer.core.port.input.ChangeCustomerStatusInput

interface ChangeCustomerStatusPort {
    fun change(input: ChangeCustomerStatusInput): Customer
}