package com.creditjourney.customer.app.adapter.output.persistence

import com.creditjourney.customer.app.adapter.output.persistence.mapper.toDomain
import com.creditjourney.customer.app.adapter.output.persistence.mapper.toEntity
import com.creditjourney.customer.app.adapter.output.persistence.repository.CustomerJpaRepository
import com.creditjourney.customer.core.domain.model.Customer
import com.creditjourney.customer.core.domain.valueobject.Document
import com.creditjourney.customer.core.port.output.CustomerRepositoryPort
import org.springframework.stereotype.Component

@Component
class CustomerPersistenceAdapter(
    private val customerJpaRepository: CustomerJpaRepository
) : CustomerRepositoryPort {

    override fun existsByDocument(document: Document): Boolean =
        customerJpaRepository.existsByDocument(document.value)

    override fun save(customer: Customer): Customer =
        customerJpaRepository.save(customer.toEntity()).toDomain()
}