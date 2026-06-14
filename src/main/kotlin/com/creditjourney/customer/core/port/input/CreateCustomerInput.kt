package com.creditjourney.customer.core.port.input

import java.math.BigDecimal

data class CreateCustomerInput(
    val name: String,
    val document: String,
    val email: String,
    val phone: String?,
    val income: BigDecimal
)