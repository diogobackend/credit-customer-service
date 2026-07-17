package com.creditjourney.customer.core.builder

import com.creditjourney.customer.core.common.messages.CustomerMessages.CUSTOMER_EMAIL_UPDATED
import com.creditjourney.customer.core.port.input.UpdateCustomerInput
import com.creditjourney.customer.core.common.messages.CustomerMessages.CUSTOMER_ID
import com.creditjourney.customer.core.common.messages.CustomerMessages.CUSTOMER_INCOME_UPDATED
import com.creditjourney.customer.core.common.messages.CustomerMessages.CUSTOMER_NAME_UPDATED
import com.creditjourney.customer.core.common.messages.CustomerMessages.CUSTOMER_PHONE_UPDATED
import java.math.BigDecimal
import java.util.UUID

fun buildUpdateCustomerInput(
    customerId: UUID = CUSTOMER_ID,
    name: String? = CUSTOMER_NAME_UPDATED,
    email: String? = CUSTOMER_EMAIL_UPDATED,
    phone: String? = CUSTOMER_PHONE_UPDATED,
    income: BigDecimal? = CUSTOMER_INCOME_UPDATED
): UpdateCustomerInput =
    UpdateCustomerInput(
        customerId = customerId,
        name = name,
        email = email,
        phone = phone,
        income = income
    )