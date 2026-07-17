package com.creditjourney.customer.core.usecase

import com.creditjourney.customer.app.configuration.logs.LogInfo
import com.creditjourney.customer.app.configuration.logs.LogParameter
import com.creditjourney.customer.core.port.FindAllCustomersPort
import com.creditjourney.customer.core.port.output.CustomerRepositoryPort
import com.creditjourney.customer.core.domain.model.CustomerSlice
import com.creditjourney.customer.core.port.input.FindAllCustomersInput

open class FindAllCustomersUseCase(
    private val customerRepositoryPort: CustomerRepositoryPort
) : FindAllCustomersPort {

    @LogInfo(logParameters = true, logReturn = true)
    override fun findAll(
        @LogParameter input: FindAllCustomersInput
    ): CustomerSlice =
        customerRepositoryPort.findAll(
            page = input.page,
            size = input.size,
            status = input.status,
            search = input.search,
            name = input.name?.trim()?.takeIf { it.isNotBlank() }
        )
}