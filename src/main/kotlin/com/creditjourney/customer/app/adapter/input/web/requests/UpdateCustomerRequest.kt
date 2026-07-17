package com.creditjourney.customer.app.adapter.input.web.requests

import java.math.BigDecimal

data class UpdateCustomerRequest(
    val name: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val income: BigDecimal? = null
)
