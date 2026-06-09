package com.creditjourney.customer.core.domain.valueobject

data class Document(
    val value: String
) {
    init {
        require(value.isNotBlank()) {
            "Document must not be blank"
        }

        require(value.all { it.isDigit() }) {
            "Document must contain only digits"
        }

        require(value.length == 11) {
            "Document must have 11 digits"
        }
    }
}