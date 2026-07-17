package com.creditjourney.customer.core.usecase.builder

import com.creditjourney.customer.core.domain.model.Customer
import com.creditjourney.customer.core.domain.model.CustomerStatus
import com.creditjourney.customer.core.domain.model.CustomerStatus.ACTIVE
import com.creditjourney.customer.core.domain.valueobject.Document
import com.creditjourney.customer.core.domain.valueobject.Email
import com.creditjourney.customer.core.domain.valueobject.Income
import com.creditjourney.customer.core.usecase.builder.CustomerBuilderConstants.CUSTOMER_CREATED_AT
import com.creditjourney.customer.core.usecase.builder.CustomerBuilderConstants.CUSTOMER_ID
import com.creditjourney.customer.core.usecase.builder.CustomerInputBuilderConstants.CUSTOMER_DOCUMENT
import com.creditjourney.customer.core.usecase.builder.CustomerInputBuilderConstants.CUSTOMER_EMAIL
import com.creditjourney.customer.core.usecase.builder.CustomerInputBuilderConstants.CUSTOMER_INCOME
import com.creditjourney.customer.core.usecase.builder.CustomerInputBuilderConstants.CUSTOMER_NAME
import com.creditjourney.customer.core.usecase.builder.CustomerInputBuilderConstants.CUSTOMER_PHONE
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

object CustomerBuilderConstants {
    val CUSTOMER_ID: UUID = UUID.fromString("11111111-1111-1111-1111-111111111111")
    val CUSTOMER_CREATED_AT: LocalDateTime = LocalDateTime.of(2026, 1, 1, 0, 0)
}

fun buildCustomer(
    customerId: UUID = CUSTOMER_ID,
    name: String = CUSTOMER_NAME,
    document: String = CUSTOMER_DOCUMENT,
    email: String = CUSTOMER_EMAIL,
    phone: String? = CUSTOMER_PHONE,
    income: BigDecimal = CUSTOMER_INCOME,
    status: CustomerStatus = ACTIVE,
    createdAt: LocalDateTime = CUSTOMER_CREATED_AT,
    updatedAt: LocalDateTime? = null
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
        updatedAt = updatedAt
    )