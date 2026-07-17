package com.creditjourney.customer.core.usecase.builder

import com.creditjourney.customer.core.port.input.UpdateCustomerInput
import com.creditjourney.customer.core.usecase.builder.CustomerBuilderConstants.CUSTOMER_ID
import java.math.BigDecimal
import java.util.UUID

fun buildUpdateCustomerInput(
    customerId: UUID = CUSTOMER_ID,
    name: String? = "Maria Souza",
    email: String? = "maria.souza@email.com",
    phone: String? = "11988887777",
    income: BigDecimal? = BigDecimal("2500.00")
): UpdateCustomerInput =
    UpdateCustomerInput(
        customerId = customerId,
        name = name,
        email = email,
        phone = phone,
        income = income
    )