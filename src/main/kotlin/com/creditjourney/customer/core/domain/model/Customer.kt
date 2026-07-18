package com.creditjourney.customer.core.domain.model

import com.creditjourney.customer.core.common.messages.CustomerMessages.CUSTOMER_NAME_MUST_NOT_BE_BLANK
import com.creditjourney.customer.core.domain.valueobject.Document
import com.creditjourney.customer.core.domain.valueobject.Email
import com.creditjourney.customer.core.domain.valueobject.Income
import java.time.LocalDateTime
import java.util.UUID

data class Customer(
    val customerId: UUID,
    val name: String,
    val document: Document,
    val email: Email,
    val phone: String?,
    val income: Income,
    val status: CustomerStatus,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime? = null,
) {
    init {
        require(name.isNotBlank()) {
            CUSTOMER_NAME_MUST_NOT_BE_BLANK
        }
    }

    companion object {
        fun create(
            name: String,
            document: Document,
            email: Email,
            phone: String?,
            income: Income,
        ): Customer =
            Customer(
                customerId = UUID.randomUUID(),
                name = name.trim(),
                document = document,
                email = email,
                phone = phone?.trim(),
                income = income,
                status = CustomerStatus.ACTIVE,
                createdAt = LocalDateTime.now(),
            )
    }

    fun changeStatus(status: CustomerStatus): Customer =
        copy(
            status = status,
            updatedAt = LocalDateTime.now(),
        )

    fun update(
        name: String?,
        email: Email?,
        phone: String?,
        income: Income?,
    ): Customer =
        copy(
            name = name?.trim() ?: this.name,
            email = email ?: this.email,
            phone = phone?.trim() ?: this.phone,
            income = income ?: this.income,
            updatedAt = LocalDateTime.now(),
        )
}
