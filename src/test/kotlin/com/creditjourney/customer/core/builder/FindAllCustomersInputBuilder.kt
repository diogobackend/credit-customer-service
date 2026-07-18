package com.creditjourney.customer.core.builder

import com.creditjourney.customer.core.domain.model.CustomerStatus
import com.creditjourney.customer.core.port.input.FindAllCustomersInput
import java.math.BigDecimal

fun buildFindAllCustomersInput(
    page: Int = 0,
    size: Int = 30,
    status: CustomerStatus? = null,
    search: String? = null,
    name: String? = null,
    minIncome: BigDecimal? = null,
    maxIncome: BigDecimal? = null,
): FindAllCustomersInput =
    FindAllCustomersInput(
        page = page,
        size = size,
        status = status,
        search = search,
        name = name,
        minIncome = minIncome,
        maxIncome = maxIncome,
    )
