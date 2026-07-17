package com.creditjourney.customer.app.adapter.output.persistence.repository

import com.creditjourney.customer.app.adapter.output.persistence.entity.CustomerEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID

interface CustomerJpaRepository : JpaRepository<CustomerEntity, UUID> {

    fun existsByDocument(document: String): Boolean
    fun existsByEmail(email: String): Boolean
    fun existsByPhone(phone: String): Boolean

    @Query(
        """
    SELECT c FROM CustomerEntity c
    WHERE (:status IS NULL OR c.status = :status)
    ORDER BY c.createdAt DESC, c.customerId DESC
    """
    )
    fun findAllCustomers(
        @Param("status") status: String?,
        pageable: Pageable
    ): Slice<CustomerEntity>

}