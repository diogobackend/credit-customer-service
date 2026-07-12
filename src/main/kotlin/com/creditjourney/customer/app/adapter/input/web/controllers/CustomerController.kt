package com.creditjourney.customer.app.adapter.input.web.controllers

import com.creditjourney.customer.app.adapter.input.web.swagger.CustomerApi
import com.creditjourney.customer.app.adapter.input.web.mappers.toInput
import com.creditjourney.customer.app.adapter.input.web.mappers.toResponse
import com.creditjourney.customer.app.adapter.input.web.requests.CreateCustomerRequest
import com.creditjourney.customer.app.adapter.input.web.responses.CustomerResponse
import com.creditjourney.customer.core.port.input.CreateCustomerPort
import com.creditjourney.customer.core.port.input.FindCustomerByIdPort
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/customers")
class CustomerController(
    private val createCustomerPort: CreateCustomerPort,
    private val findCustomerByIdPort: FindCustomerByIdPort
) : CustomerApi {

    @PostMapping
    override fun create(
        @Valid @RequestBody request: CreateCustomerRequest
    ): ResponseEntity<CustomerResponse> {
        val customer = createCustomerPort.create(request.toInput())

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(customer.toResponse())
    }

    @GetMapping("/{customerId}")
    override fun findById(
        @PathVariable customerId: UUID
    ): ResponseEntity<CustomerResponse> {
        val customer = findCustomerByIdPort.findById(customerId)

        return ResponseEntity.ok(customer.toResponse())
    }
}