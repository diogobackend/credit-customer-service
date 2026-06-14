package com.creditjourney.customer.app.adapter.output.persistence.repository

import com.creditjourney.customer.app.adapter.output.persistence.entity.CustomerEntity
import org.hibernate.validator.constraints.UUID
import org.springframework.data.jpa.repository.JpaRepository

interface CustomerJpaRepository : JpaRepository<CustomerEntity, UUID> {
    fun existsByDocument(document: String): Boolean
}