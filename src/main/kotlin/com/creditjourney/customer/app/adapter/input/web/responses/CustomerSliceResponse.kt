package com.creditjourney.customer.app.adapter.input.web.responses

data class CustomerSliceResponse(
    val content: List<CustomerResponse>,
    val page: Int,
    val size: Int,
    val hasNext: Boolean,
    val totalElements: Long
)