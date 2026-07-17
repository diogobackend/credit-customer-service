package com.creditjourney.customer.app.adapter.output.persistence.repository

import com.creditjourney.customer.app.adapter.output.persistence.entity.CustomerEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.math.BigDecimal
import java.util.UUID

interface CustomerJpaRepository : JpaRepository<CustomerEntity, UUID> {

    fun existsByDocument(document: String): Boolean
    fun existsByEmail(email: String): Boolean
    fun existsByPhone(phone: String): Boolean

    @Query(
        """
    SELECT c FROM CustomerEntity c
    WHERE (:status IS NULL OR c.status = :status)
      AND (
          :search IS NULL
          OR c.document = :search
          OR c.email = :search
          OR c.phone = :search
      )
      AND (
          :name IS NULL
          OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))
      )
      AND (:minIncome IS NULL OR c.income >= :minIncome)
      AND (:maxIncome IS NULL OR c.income <= :maxIncome)
    ORDER BY c.createdAt DESC, c.customerId DESC
    """
    )
    fun findAllCustomers(
        @Param("status") status: String?,
        @Param("search") search: String?,
        @Param("name") name: String?,
        @Param("minIncome") minIncome: BigDecimal?,
        @Param("maxIncome") maxIncome: BigDecimal?,
        pageable: Pageable
    ): Page<CustomerEntity>

}