package com.creditjourney.customer.app.adapter.input.web.requests

import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import java.math.BigDecimal

data class CreateCustomerRequest(

    @field:NotBlank
    @field:Size(max = 150)
    val name: String,

    @field:NotBlank
    @field:Pattern(regexp = "\\d{11}")
    val document: String,

    @field:NotBlank
    @field:Email
    @field:Size(max = 150)
    val email: String,

    @field:Size(max = 20)
    val phone: String?,

    @field:DecimalMin(value = "0.0", inclusive = true)
    val income: BigDecimal
)