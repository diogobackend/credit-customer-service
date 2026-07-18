package com.creditjourney.customer.app.adapter.output.persistence.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "customers")
class CustomerEntity(
    @Id
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "customer_id", nullable = false, length = 36)
    var customerId: UUID,
    @Column(name = "name", nullable = false, length = 150)
    var name: String,
    @Column(name = "document", nullable = false, unique = true, length = 11)
    var document: String,
    @Column(name = "email", nullable = false, unique = true, length = 150)
    var email: String,
    @Column(name = "phone", unique = true, length = 20)
    var phone: String?,
    @Column(name = "income", nullable = false, precision = 19, scale = 2)
    var income: BigDecimal,
    @Column(name = "status", nullable = false, length = 30)
    var status: String,
    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime,
    @Column(name = "updated_at")
    var updatedAt: LocalDateTime?,
)
