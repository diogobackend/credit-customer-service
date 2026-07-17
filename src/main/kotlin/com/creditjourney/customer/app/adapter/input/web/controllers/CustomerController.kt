package com.creditjourney.customer.app.adapter.input.web.controllers

import com.creditjourney.customer.app.adapter.input.web.swagger.CustomerApi
import com.creditjourney.customer.app.adapter.input.web.mappers.toInput
import com.creditjourney.customer.app.adapter.input.web.mappers.toResponse
import com.creditjourney.customer.app.adapter.input.web.requests.CreateCustomerRequest
import com.creditjourney.customer.app.adapter.input.web.requests.UpdateCustomerRequest
import com.creditjourney.customer.app.adapter.input.web.responses.CustomerResponse
import com.creditjourney.customer.app.adapter.input.web.responses.CustomerSliceResponse
import com.creditjourney.customer.core.domain.model.CustomerStatus
import com.creditjourney.customer.core.port.CreateCustomerPort
import com.creditjourney.customer.core.port.DeleteCustomerPort
import com.creditjourney.customer.core.port.input.FindAllCustomersInput
import com.creditjourney.customer.core.port.FindAllCustomersPort
import com.creditjourney.customer.core.port.FindCustomerByIdPort
import com.creditjourney.customer.core.port.UpdateCustomerPort
import com.creditjourney.customer.core.port.input.DeleteCustomerInput
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import java.util.UUID

@RestController
@RequestMapping("/api/v1/customers")
class CustomerController(
    private val createCustomerPort: CreateCustomerPort,
    private val findCustomerByIdPort: FindCustomerByIdPort,
    private val findAllCustomersPort: FindAllCustomersPort,
    private val deleteCustomerPort: DeleteCustomerPort,
    private val updateCustomerPort: UpdateCustomerPort
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

    @GetMapping
    override fun findAll(
        @RequestParam(required = false) status: CustomerStatus?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "30") size: Int,
        @RequestParam(required = false) search: String?,
        @RequestParam(required = false) name: String?,
        @RequestParam(required = false) minIncome: BigDecimal?,
        @RequestParam(required = false) maxIncome: BigDecimal?
    ): ResponseEntity<CustomerSliceResponse> {
        val result = findAllCustomersPort.findAll(
            FindAllCustomersInput(
                page = page,
                size = size,
                status = status,
                search = search,
                name = name,
                minIncome = minIncome,
                maxIncome = maxIncome
            )
        )

        return ResponseEntity.ok(result.toResponse())
    }

    @GetMapping("/{customerId}")
    override fun findById(
        @PathVariable customerId: UUID
    ): ResponseEntity<CustomerResponse> {
        val customer = findCustomerByIdPort.findById(customerId)

        return ResponseEntity.ok(customer.toResponse())
    }

    @DeleteMapping("/{customerId}")
    override fun delete(
        @PathVariable customerId: UUID,
        @RequestParam(defaultValue = "INACTIVE") status: CustomerStatus
    ): ResponseEntity<Void> {
        deleteCustomerPort.delete(
            DeleteCustomerInput(
                customerId = customerId,
                status = status
            )
        )

        return ResponseEntity.noContent().build()
    }

    @PatchMapping("/{customerId}")
    override fun update(
        @PathVariable customerId: UUID,
        @RequestBody request: UpdateCustomerRequest
    ): ResponseEntity<CustomerResponse> =
        ResponseEntity.ok(
            updateCustomerPort.update(
                request.toInput(customerId)
            ).toResponse()
        )
}