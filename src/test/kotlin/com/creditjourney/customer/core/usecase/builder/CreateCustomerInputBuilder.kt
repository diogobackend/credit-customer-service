package com.creditjourney.customer.core.usecase.builder

import com.creditjourney.customer.core.port.input.CreateCustomerInput
import java.math.BigDecimal

object CustomerInputBuilderConstants {
    const val CUSTOMER_NAME = "João Silva"
    const val CUSTOMER_DOCUMENT = "12345678900"
    const val CUSTOMER_EMAIL = "joao@email.com"
    const val CUSTOMER_PHONE = "11999999999"

    val CUSTOMER_INCOME: BigDecimal = BigDecimal("4500.00")
}

fun buildCreateCustomerInput(
    name: String = CustomerInputBuilderConstants.CUSTOMER_NAME,
    document: String = CustomerInputBuilderConstants.CUSTOMER_DOCUMENT,
    email: String = CustomerInputBuilderConstants.CUSTOMER_EMAIL,
    phone: String? = CustomerInputBuilderConstants.CUSTOMER_PHONE,
    income: BigDecimal = CustomerInputBuilderConstants.CUSTOMER_INCOME
): CreateCustomerInput =
    CreateCustomerInput(
        name = name,
        document = document,
        email = email,
        phone = phone,
        income = income
    )