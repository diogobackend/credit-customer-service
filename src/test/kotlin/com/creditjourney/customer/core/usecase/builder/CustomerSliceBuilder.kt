package com.creditjourney.customer.core.usecase.builder

import com.creditjourney.customer.core.domain.model.Customer
import com.creditjourney.customer.core.domain.model.CustomerSlice

fun buildCustomerSlice(
    content: List<Customer> = listOf(buildCustomer()),
    page: Int = 0,
    size: Int = 30,
    hasNext: Boolean = false,
    totalElements: Long = content.size.toLong()
): CustomerSlice =
    CustomerSlice(
        content = content,
        page = page,
        size = size,
        hasNext = hasNext,
        totalElements = totalElements
    )