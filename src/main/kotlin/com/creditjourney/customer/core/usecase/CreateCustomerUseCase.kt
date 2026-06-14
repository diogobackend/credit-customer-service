package com.creditjourney.customer.core.usecase

import com.creditjourney.customer.core.domain.exception.CustomerAlreadyExistsException
import com.creditjourney.customer.core.domain.model.Customer
import com.creditjourney.customer.core.domain.valueobject.Document
import com.creditjourney.customer.core.domain.valueobject.Email
import com.creditjourney.customer.core.domain.valueobject.Income
import com.creditjourney.customer.core.port.input.CreateCustomerInput
import com.creditjourney.customer.core.port.input.CreateCustomerPort
import com.creditjourney.customer.core.port.output.CustomerRepositoryPort

class CreateCustomerUseCase(
    private val customerRepositoryPort: CustomerRepositoryPort
) : CreateCustomerPort {

    override fun create(input: CreateCustomerInput): Customer {
        val document = Document(input.document)

        if (customerRepositoryPort.existsByDocument(document)) {
            throw CustomerAlreadyExistsException(input.document)
        }

        val customer = Customer.create(
            name = input.name,
            document = document,
            email = Email(input.email),
            phone = input.phone,
            income = Income(input.income)
        )

        return customerRepositoryPort.save(customer)
    }
}