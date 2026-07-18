package com.creditjourney.customer.core.usecase

import com.creditjourney.customer.app.configuration.logs.LogInfo
import com.creditjourney.customer.app.configuration.logs.LogParameter
import com.creditjourney.customer.core.domain.model.Customer
import com.creditjourney.customer.core.port.ChangeCustomerStatusPort
import com.creditjourney.customer.core.port.FindCustomerByIdPort
import com.creditjourney.customer.core.port.input.ChangeCustomerStatusInput
import com.creditjourney.customer.core.port.output.CustomerRepositoryPort

open class ChangeCustomerStatusUseCase(
    private val findCustomerByIdPort: FindCustomerByIdPort,
    private val customerRepositoryPort: CustomerRepositoryPort
) : ChangeCustomerStatusPort {

    @LogInfo(logParameters = true, logReturn = true)
    override fun change(
        @LogParameter input: ChangeCustomerStatusInput
    ): Customer {
        val customer = findCustomerByIdPort.findById(input.customerId)
        val updatedCustomer = customer.changeStatus(input.status)

        return customerRepositoryPort.save(updatedCustomer)
    }
}