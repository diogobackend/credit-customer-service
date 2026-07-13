package com.creditjourney.customer.core.usecase

import com.creditjourney.customer.app.configuration.logs.LogInfo
import com.creditjourney.customer.app.configuration.logs.LogParameter
import com.creditjourney.customer.core.port.DeleteCustomerPort
import com.creditjourney.customer.core.port.FindCustomerByIdPort
import com.creditjourney.customer.core.port.output.CustomerRepositoryPort
import java.util.UUID

open class DeleteCustomerUseCase(
    private val findCustomerByIdPort: FindCustomerByIdPort,
    private val customerRepositoryPort: CustomerRepositoryPort
) : DeleteCustomerPort {

    @LogInfo(logParameters = true)
    override fun delete(
        @LogParameter customerId: UUID
    ) {
        val customer = findCustomerByIdPort.findById(customerId)
        val inactiveCustomer = customer.inactivate()

        customerRepositoryPort.save(inactiveCustomer)
    }
}