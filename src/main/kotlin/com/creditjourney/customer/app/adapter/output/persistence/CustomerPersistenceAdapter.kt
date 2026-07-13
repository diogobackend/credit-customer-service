package com.creditjourney.customer.app.adapter.output.persistence

import com.creditjourney.customer.app.adapter.output.persistence.mapper.toDomain
import com.creditjourney.customer.app.adapter.output.persistence.mapper.toEntity
import com.creditjourney.customer.app.adapter.output.persistence.repository.CustomerJpaRepository
import com.creditjourney.customer.core.domain.model.Customer
import com.creditjourney.customer.core.domain.model.CustomerSlice
import com.creditjourney.customer.core.domain.valueobject.Document
import com.creditjourney.customer.core.domain.valueobject.Email
import com.creditjourney.customer.core.port.output.CustomerRepositoryPort
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class CustomerPersistenceAdapter(
    private val customerJpaRepository: CustomerJpaRepository
) : CustomerRepositoryPort {

    override fun existsByDocument(document: Document): Boolean =
        customerJpaRepository.existsByDocument(document.value)

    override fun existsByEmail(email: Email): Boolean =
        customerJpaRepository.existsByEmail(email.value)

    override fun existsByPhone(phone: String): Boolean =
        customerJpaRepository.existsByPhone(phone)

    override fun save(customer: Customer): Customer =
        customerJpaRepository.save(customer.toEntity()).toDomain()

    override fun findById(customerId: UUID): Customer? =
        customerJpaRepository.findById(customerId)
            .map { it.toDomain() }
            .orElse(null)

    override fun findAll(page: Int, size: Int): CustomerSlice {
        val pageable = PageRequest.of(page, size)
        val result = customerJpaRepository.findAllCustomers(pageable)

        return CustomerSlice(
            content = result.content.map { it.toDomain() },
            page = page,
            size = size,
            hasNext = result.hasNext()
        )
    }
}