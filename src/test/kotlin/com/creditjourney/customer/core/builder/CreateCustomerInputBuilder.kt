package com.creditjourney.customer.core.builder

import com.creditjourney.customer.core.common.messages.CustomerMessages.CUSTOMER_DOCUMENT
import com.creditjourney.customer.core.common.messages.CustomerMessages.CUSTOMER_EMAIL
import com.creditjourney.customer.core.common.messages.CustomerMessages.CUSTOMER_INCOME
import com.creditjourney.customer.core.common.messages.CustomerMessages.CUSTOMER_NAME
import com.creditjourney.customer.core.common.messages.CustomerMessages.CUSTOMER_PHONE
import com.creditjourney.customer.core.port.input.CreateCustomerInput
import java.math.BigDecimal

fun buildCreateCustomerInput(
    name: String = CUSTOMER_NAME,
    document: String = CUSTOMER_DOCUMENT,
    email: String = CUSTOMER_EMAIL,
    phone: String? = CUSTOMER_PHONE,
    income: BigDecimal = CUSTOMER_INCOME,
): CreateCustomerInput =
    CreateCustomerInput(
        name = name,
        document = document,
        email = email,
        phone = phone,
        income = income,
    )
