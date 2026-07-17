package com.creditjourney.customer.core.port.input

import com.creditjourney.customer.core.domain.model.CustomerStatus

data class FindAllCustomersInput(
    val page: Int = 0,
    val size: Int = 30,
    val status: CustomerStatus? = null,
    val search: String? = null,
    val name: String? = null
) {
    init {
        require(page >= 0) { "Page must not be negative" }
        require(size in 1..100) { "Size must be between 1 and 100" }
    }
}