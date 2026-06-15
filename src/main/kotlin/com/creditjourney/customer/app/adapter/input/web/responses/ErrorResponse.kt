package com.creditjourney.customer.app.adapter.input.web.responses

data class ErrorResponse(
    val status: Int,
    val error: String,
    val message: String
)