package com.creditjourney.customer.app.adapter.input.web.responses

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

data class CustomerResponse(
    val customerId: UUID,
    val name: String,
    val document: String,
    val email: String,
    val phone: String?,
    val income: BigDecimal,
    val status: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?
)