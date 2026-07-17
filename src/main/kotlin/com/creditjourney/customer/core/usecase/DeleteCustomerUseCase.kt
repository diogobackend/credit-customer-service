package com.creditjourney.customer.core.usecase

import com.creditjourney.customer.app.configuration.logs.LogInfo
import com.creditjourney.customer.app.configuration.logs.LogParameter
import com.creditjourney.customer.core.port.DeleteCustomerPort
import com.creditjourney.customer.core.port.FindCustomerByIdPort
import com.creditjourney.customer.core.port.input.DeleteCustomerInput
import com.creditjourney.customer.core.port.output.CustomerRepositoryPort

open class DeleteCustomerUseCase(
    private val findCustomerByIdPort: FindCustomerByIdPort,
    private val customerRepositoryPort: CustomerRepositoryPort
) : DeleteCustomerPort {

    @LogInfo(logParameters = true)
    override fun delete(
        @LogParameter input: DeleteCustomerInput
    ) {
        val customer = findCustomerByIdPort.findById(input.customerId)
        val updatedCustomer = customer.changeStatus(input.status)

        customerRepositoryPort.save(updatedCustomer)
    }
}