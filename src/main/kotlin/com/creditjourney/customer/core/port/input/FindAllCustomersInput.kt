package com.creditjourney.customer.core.port.input

data class FindAllCustomersInput(
    val page: Int = 0,
    val size: Int = 30
) {
    init {
        require(page >= 0) { "Page must not be negative" }
        require(size in 1..100) { "Size must be between 1 and 100" }
    }
}