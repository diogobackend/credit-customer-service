package com.creditjourney.customer.core.port.output

import com.creditjourney.customer.core.domain.model.Customer
import com.creditjourney.customer.core.domain.valueobject.Document
import com.creditjourney.customer.core.domain.valueobject.Email

interface CustomerRepositoryPort {

    fun existsByDocument(document: Document): Boolean
    fun existsByEmail(email: Email): Boolean
    fun existsByPhone(phone: String): Boolean
    fun save(customer: Customer): Customer

}