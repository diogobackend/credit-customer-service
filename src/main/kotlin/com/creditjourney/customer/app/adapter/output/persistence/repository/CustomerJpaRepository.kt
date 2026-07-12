package com.creditjourney.customer.app.adapter.output.persistence.repository

import com.creditjourney.customer.app.adapter.output.persistence.entity.CustomerEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface CustomerJpaRepository : JpaRepository<CustomerEntity, UUID> {

    fun existsByDocument(document: String): Boolean
    fun existsByEmail(email: String): Boolean
    fun existsByPhone(phone: String): Boolean

}