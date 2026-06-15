package com.creditjourney.customer.core.domain.exception

class CustomerAlreadyExistsException(
    field: String,
    value: String
) : RuntimeException("Customer already exists with $field: $value")