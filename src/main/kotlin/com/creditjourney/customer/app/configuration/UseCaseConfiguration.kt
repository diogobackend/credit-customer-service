package com.creditjourney.customer.app.configuration

import com.creditjourney.customer.core.port.CreateCustomerPort
import com.creditjourney.customer.core.port.DeleteCustomerPort
import com.creditjourney.customer.core.port.FindAllCustomersPort
import com.creditjourney.customer.core.port.FindCustomerByIdPort
import com.creditjourney.customer.core.port.UpdateCustomerPort
import com.creditjourney.customer.core.port.output.CustomerRepositoryPort
import com.creditjourney.customer.core.usecase.CreateCustomerUseCase
import com.creditjourney.customer.core.usecase.DeleteCustomerUseCase
import com.creditjourney.customer.core.usecase.FindAllCustomersUseCase
import com.creditjourney.customer.core.usecase.FindCustomerByIdUseCase
import com.creditjourney.customer.core.usecase.UpdateCustomerUseCase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UseCaseConfiguration {

    @Bean
    fun createCustomerPort(
        customerRepositoryPort: CustomerRepositoryPort
    ): CreateCustomerPort =
        CreateCustomerUseCase(customerRepositoryPort)

    @Bean
    fun findCustomerByIdPort(
        customerRepositoryPort: CustomerRepositoryPort
    ): FindCustomerByIdPort =
        FindCustomerByIdUseCase(customerRepositoryPort)

    @Bean
    fun findAllCustomersPort(
        customerRepositoryPort: CustomerRepositoryPort
    ): FindAllCustomersPort =
        FindAllCustomersUseCase(customerRepositoryPort)

    @Bean
    fun deleteCustomerPort(
        findCustomerByIdPort: FindCustomerByIdPort,
        customerRepositoryPort: CustomerRepositoryPort
    ): DeleteCustomerPort =
        DeleteCustomerUseCase(
            findCustomerByIdPort = findCustomerByIdPort,
            customerRepositoryPort = customerRepositoryPort
        )

    @Bean
    fun updateCustomerPort(
        findCustomerByIdPort: FindCustomerByIdPort,
        customerRepositoryPort: CustomerRepositoryPort
    ): UpdateCustomerPort =
        UpdateCustomerUseCase(
            findCustomerByIdPort = findCustomerByIdPort,
            customerRepositoryPort = customerRepositoryPort
        )
}