package com.creditjourney.customer.core.domain.exception

class CustomerAlreadyExistsException(
    document: String
) : RuntimeException("Customer already exists with document: $document")