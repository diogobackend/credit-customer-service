package com.creditjourney.customer.core.usecase.builder

import com.creditjourney.customer.core.domain.model.CustomerStatus
import com.creditjourney.customer.core.port.input.FindAllCustomersInput

fun buildFindAllCustomersInput(
    page: Int = 0,
    size: Int = 30,
    status: CustomerStatus? = null,
    search: String? = null,
    name: String? = null
): FindAllCustomersInput =
    FindAllCustomersInput(
        page = page,
        size = size,
        status = status,
        search = search,
        name = name
    )