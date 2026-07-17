package com.creditjourney.customer.core.port

import com.creditjourney.customer.core.port.input.DeleteCustomerInput

interface DeleteCustomerPort {
    fun delete(input: DeleteCustomerInput)
}