package com.creditjourney.customer.core.usecase

import com.creditjourney.customer.app.configuration.logs.LogInfo
import com.creditjourney.customer.app.configuration.logs.LogParameter
import com.creditjourney.customer.core.domain.exception.CustomerAlreadyExistsException
import com.creditjourney.customer.core.domain.model.Customer
import com.creditjourney.customer.core.domain.valueobject.Document
import com.creditjourney.customer.core.domain.valueobject.Email
import com.creditjourney.customer.core.domain.valueobject.Income
import com.creditjourney.customer.core.port.input.CreateCustomerInput
import com.creditjourney.customer.core.port.input.CreateCustomerPort
import com.creditjourney.customer.core.port.output.CustomerRepositoryPort

open class CreateCustomerUseCase(
    private val customerRepositoryPort: CustomerRepositoryPort
) : CreateCustomerPort {

    @LogInfo(logParameters = true, logReturn = true)
    override fun create(@LogParameter input: CreateCustomerInput): Customer {
        val document = Document(input.document)
        val email = Email(input.email)
        val phone = input.phone?.trim()

        if (customerRepositoryPort.existsByDocument(document)) {
            throw CustomerAlreadyExistsException("document", document.value)
        }

        if (customerRepositoryPort.existsByEmail(email)) {
            throw CustomerAlreadyExistsException("email", email.value)
        }

        if (!phone.isNullOrBlank() && customerRepositoryPort.existsByPhone(phone)) {
            throw CustomerAlreadyExistsException("phone", phone)
        }

        val customer = Customer.create(
            name = input.name,
            document = document,
            email = email,
            phone = phone,
            income = Income(input.income)
        )

        return customerRepositoryPort.save(customer)
    }
}