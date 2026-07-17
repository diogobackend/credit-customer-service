package com.creditjourney.customer.core.usecase

import com.creditjourney.customer.app.configuration.logs.LogInfo
import com.creditjourney.customer.app.configuration.logs.LogParameter
import com.creditjourney.customer.core.domain.exception.CustomerAlreadyExistsException
import com.creditjourney.customer.core.domain.model.Customer
import com.creditjourney.customer.core.domain.valueobject.Email
import com.creditjourney.customer.core.domain.valueobject.Income
import com.creditjourney.customer.core.port.FindCustomerByIdPort
import com.creditjourney.customer.core.port.UpdateCustomerPort
import com.creditjourney.customer.core.port.input.UpdateCustomerInput
import com.creditjourney.customer.core.port.output.CustomerRepositoryPort

class UpdateCustomerUseCase(
    private val findCustomerByIdPort: FindCustomerByIdPort,
    private val customerRepositoryPort: CustomerRepositoryPort
) : UpdateCustomerPort {

    @LogInfo(logParameters = true, logReturn = true)
    override fun update(
        @LogParameter input: UpdateCustomerInput
    ): Customer {

        val customer = findCustomerByIdPort.findById(input.customerId)

        val email = input.email?.let { Email(it) }
        val income = input.income?.let { Income(it) }
        val phone = input.phone?.trim()

        validateEmail(customer, email)
        validatePhone(customer, phone)

        val updatedCustomer = customer.update(
            name = input.name,
            email = email,
            phone = phone,
            income = income
        )

        return customerRepositoryPort.save(updatedCustomer)
    }

    private fun validateEmail(customer: Customer, email: Email?) {
        if (
            email != null &&
            email.value != customer.email.value &&
            customerRepositoryPort.existsByEmail(email)
        ) {
            throw CustomerAlreadyExistsException("email", email.value)
        }
    }

    private fun validatePhone(customer: Customer, phone: String?) {
        if (
            !phone.isNullOrBlank() &&
            phone != customer.phone &&
            customerRepositoryPort.existsByPhone(phone)
        ) {
            throw CustomerAlreadyExistsException("phone", phone)
        }
    }
}