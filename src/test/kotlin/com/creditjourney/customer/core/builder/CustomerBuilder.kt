package com.creditjourney.customer.core.builder

import com.creditjourney.customer.core.common.messages.CustomerMessages.CUSTOMER_CREATED_AT
import com.creditjourney.customer.core.common.messages.CustomerMessages.CUSTOMER_DOCUMENT
import com.creditjourney.customer.core.common.messages.CustomerMessages.CUSTOMER_EMAIL
import com.creditjourney.customer.core.common.messages.CustomerMessages.CUSTOMER_ID
import com.creditjourney.customer.core.common.messages.CustomerMessages.CUSTOMER_INCOME
import com.creditjourney.customer.core.common.messages.CustomerMessages.CUSTOMER_NAME
import com.creditjourney.customer.core.common.messages.CustomerMessages.CUSTOMER_PHONE
import com.creditjourney.customer.core.domain.model.Customer
import com.creditjourney.customer.core.domain.model.CustomerStatus
import com.creditjourney.customer.core.domain.model.CustomerStatus.ACTIVE
import com.creditjourney.customer.core.domain.valueobject.Document
import com.creditjourney.customer.core.domain.valueobject.Email
import com.creditjourney.customer.core.domain.valueobject.Income
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

fun buildCustomer(
    customerId: UUID = CUSTOMER_ID,
    name: String = CUSTOMER_NAME,
    document: String = CUSTOMER_DOCUMENT,
    email: String = CUSTOMER_EMAIL,
    phone: String? = CUSTOMER_PHONE,
    income: BigDecimal = CUSTOMER_INCOME,
    status: CustomerStatus = ACTIVE,
    createdAt: LocalDateTime = CUSTOMER_CREATED_AT,
    updatedAt: LocalDateTime? = null,
): Customer =
    Customer(
        customerId = customerId,
        name = name,
        document = Document(document),
        email = Email(email),
        phone = phone,
        income = Income(income),
        status = status,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
