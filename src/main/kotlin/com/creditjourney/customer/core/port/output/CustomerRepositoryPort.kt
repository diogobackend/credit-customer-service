package com.creditjourney.customer.core.port.output

import com.creditjourney.customer.core.domain.model.Customer
import com.creditjourney.customer.core.domain.valueobject.Document

interface CustomerRepositoryPort {
    fun existsByDocument(document: Document): Boolean
    fun save(customer: Customer): Customer
}