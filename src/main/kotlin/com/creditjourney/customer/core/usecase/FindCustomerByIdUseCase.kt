package com.creditjourney.customer.core.usecase

import com.creditjourney.customer.app.configuration.logs.LogInfo
import com.creditjourney.customer.app.configuration.logs.LogParameter
import com.creditjourney.customer.core.domain.exception.CustomerNotFoundException
import com.creditjourney.customer.core.domain.model.Customer
import com.creditjourney.customer.core.port.FindCustomerByIdPort
import com.creditjourney.customer.core.port.output.CustomerRepositoryPort
import java.util.UUID

open class FindCustomerByIdUseCase(
    private val customerRepositoryPort: CustomerRepositoryPort,
) : FindCustomerByIdPort {
    @LogInfo(logParameters = true, logReturn = true)
    override fun findById(
        @LogParameter customerId: UUID,
    ): Customer =
        customerRepositoryPort.findById(customerId)
            ?: throw CustomerNotFoundException(customerId)
}
