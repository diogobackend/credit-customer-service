package com.creditjourney.customer.app.configuration

import com.creditjourney.customer.core.port.input.CreateCustomerPort
import com.creditjourney.customer.core.port.output.CustomerRepositoryPort
import com.creditjourney.customer.core.usecase.CreateCustomerUseCase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UseCaseConfiguration {

    @Bean
    fun createCustomerPort(
        customerRepositoryPort: CustomerRepositoryPort
    ): CreateCustomerPort =
        CreateCustomerUseCase(customerRepositoryPort)
}