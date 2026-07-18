package com.creditjourney.customer.core.domain.model

data class CustomerSlice(
    val content: List<Customer>,
    val page: Int,
    val size: Int,
    val hasNext: Boolean,
    val totalElements: Long,
)
