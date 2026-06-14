package com.creditjourney.customer.app.adapter.output.persistence.mapper

import com.creditjourney.customer.app.adapter.output.persistence.entity.CustomerEntity
import com.creditjourney.customer.core.domain.model.Customer
import com.creditjourney.customer.core.domain.model.CustomerStatus
import com.creditjourney.customer.core.domain.valueobject.Document
import com.creditjourney.customer.core.domain.valueobject.Email
import com.creditjourney.customer.core.domain.valueobject.Income

fun Customer.toEntity(): CustomerEntity =
    CustomerEntity(
        customerId = customerId,
        name = name,
        document = document.value,
        email = email.value,
        phone = phone,
        income = income.value,
        status = status.name,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

fun CustomerEntity.toDomain(): Customer =
    Customer(
        customerId = customerId,
        name = name,
        document = Document(document),
        email = Email(email),
        phone = phone,
        income = Income(income),
        status = CustomerStatus.valueOf(status),
        createdAt = createdAt,
        updatedAt = updatedAt
    )