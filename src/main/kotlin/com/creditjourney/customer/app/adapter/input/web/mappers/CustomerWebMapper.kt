package com.creditjourney.customer.app.adapter.input.web.mappers

import com.creditjourney.customer.app.adapter.input.web.requests.CreateCustomerRequest
import com.creditjourney.customer.app.adapter.input.web.requests.UpdateCustomerRequest
import com.creditjourney.customer.app.adapter.input.web.responses.CustomerResponse
import com.creditjourney.customer.app.adapter.input.web.responses.CustomerSliceResponse
import com.creditjourney.customer.core.domain.model.Customer
import com.creditjourney.customer.core.domain.model.CustomerSlice
import com.creditjourney.customer.core.port.input.CreateCustomerInput
import com.creditjourney.customer.core.port.input.UpdateCustomerInput
import java.util.UUID

fun CreateCustomerRequest.toInput(): CreateCustomerInput =
    CreateCustomerInput(
        name = name,
        document = document,
        email = email,
        phone = phone,
        income = income
    )

fun Customer.toResponse(): CustomerResponse =
    CustomerResponse(
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

fun CustomerSlice.toResponse(): CustomerSliceResponse =
    CustomerSliceResponse(
        content = content.map { it.toResponse() },
        page = page,
        size = size,
        hasNext = hasNext,
        totalElements = totalElements
    )

fun UpdateCustomerRequest.toInput(customerId: UUID): UpdateCustomerInput =
    UpdateCustomerInput(
        customerId = customerId,
        name = name,
        email = email,
        phone = phone,
        income = income
    )