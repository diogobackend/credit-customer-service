package com.creditjourney.customer.core.port.output

import com.creditjourney.customer.core.domain.model.Customer
import com.creditjourney.customer.core.domain.model.CustomerSlice
import com.creditjourney.customer.core.domain.model.CustomerStatus
import com.creditjourney.customer.core.domain.valueobject.Document
import com.creditjourney.customer.core.domain.valueobject.Email
import java.math.BigDecimal
import java.util.UUID

interface CustomerRepositoryPort {

    fun existsByDocument(document: Document): Boolean
    fun existsByEmail(email: Email): Boolean
    fun existsByPhone(phone: String): Boolean
    fun save(customer: Customer): Customer
    fun findById(customerId: UUID): Customer?
    fun findAll(
            page: Int,
            size: Int,
            status: CustomerStatus?,
            search: String?,
            name: String?,
            minIncome: BigDecimal?,
            maxIncome: BigDecimal?
        ): CustomerSlice

    fun deleteById(customerId: UUID)
}