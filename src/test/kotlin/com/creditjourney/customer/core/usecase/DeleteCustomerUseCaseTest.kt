package com.creditjourney.customer.core.usecase

import com.creditjourney.customer.core.domain.exception.CustomerNotFoundException
import com.creditjourney.customer.core.domain.model.CustomerStatus.INACTIVE
import com.creditjourney.customer.core.domain.model.CustomerStatus.BLOCKED
import com.creditjourney.customer.core.domain.model.CustomerStatus.ACTIVE
import com.creditjourney.customer.core.port.FindCustomerByIdPort
import com.creditjourney.customer.core.port.output.CustomerRepositoryPort
import com.creditjourney.customer.core.builder.buildCustomer
import com.creditjourney.customer.core.builder.buildDeleteCustomerInput
import com.creditjourney.customer.core.common.messages.CustomerMessages.CUSTOMER_ID
import com.creditjourney.customer.core.common.messages.CustomerMessages.CUSTOMER_NOT_FOUND_WITH_CUSTOMER_ID
import com.creditjourney.customer.core.common.messages.CustomerMessages.DELETE_STATUS_MUST_BE_INACTIVE_OR_BLOCKED
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import io.mockk.verifyOrder
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class DeleteCustomerUseCaseTest(
    @param:MockK
    private val findCustomerByIdPort: FindCustomerByIdPort,

    @param:MockK
    private val customerRepositoryPort: CustomerRepositoryPort,

    @param:InjectMockKs
    private val deleteCustomerUseCase: DeleteCustomerUseCase
) {

    @AfterEach
    fun tearDown() {
        clearMocks(findCustomerByIdPort, customerRepositoryPort)
    }

    @Test
    fun `should delete customer successfully`() {

        val customer = buildCustomer()

        every { findCustomerByIdPort.findById(CUSTOMER_ID) } returns customer
        every { customerRepositoryPort.save(any()) } answers { firstArg() }

        deleteCustomerUseCase.delete(
            buildDeleteCustomerInput()
        )

        verifyOrder {
            findCustomerByIdPort.findById(CUSTOMER_ID)
            customerRepositoryPort.save(
                match {
                    it.customerId == CUSTOMER_ID &&
                            it.status == INACTIVE &&
                            it.updatedAt != null
                }
            )
        }
    }

    @Test
    fun `should throw exception when customer is not found`() {

        val input = buildDeleteCustomerInput()

        every { findCustomerByIdPort.findById(CUSTOMER_ID) } throws CustomerNotFoundException(CUSTOMER_ID)

        val exception = assertThrows<CustomerNotFoundException> {
            deleteCustomerUseCase.delete(input)
        }

        assertThat(exception.message).isEqualTo("$CUSTOMER_NOT_FOUND_WITH_CUSTOMER_ID: $CUSTOMER_ID")

        verify(exactly = 1) { findCustomerByIdPort.findById(CUSTOMER_ID) }
        verify(exactly = 0) { customerRepositoryPort.save(any()) }
    }

    @Test
    fun `should keep customer inactive when deleting inactive customer`() {

        val customer = buildCustomer(status = INACTIVE)

        every { findCustomerByIdPort.findById(CUSTOMER_ID) } returns customer
        every { customerRepositoryPort.save(any()) } answers { firstArg() }

        deleteCustomerUseCase.delete(buildDeleteCustomerInput())

        verify(exactly = 1) {
            customerRepositoryPort.save(
                match {
                    it.customerId == CUSTOMER_ID &&
                            it.status == INACTIVE &&
                            it.updatedAt != null
                }
            )
        }
    }

    @Test
    fun `should delete customer as inactive successfully`() {

        val input = buildDeleteCustomerInput()
        val customer = buildCustomer()

        every { findCustomerByIdPort.findById(CUSTOMER_ID) } returns customer
        every { customerRepositoryPort.save(any()) } answers { firstArg() }

        deleteCustomerUseCase.delete(input)

        verify(exactly = 1) { findCustomerByIdPort.findById(CUSTOMER_ID) }
        verify(exactly = 1) {
            customerRepositoryPort.save(
                match {
                    it.customerId == CUSTOMER_ID &&
                            it.status == INACTIVE &&
                            it.updatedAt != null
                }
            )
        }
    }

    @Test
    fun `should delete customer as blocked successfully`() {

        val input = buildDeleteCustomerInput(
            status = BLOCKED
        )

        val customer = buildCustomer()

        every { findCustomerByIdPort.findById(CUSTOMER_ID) } returns customer
        every { customerRepositoryPort.save(any()) } answers { firstArg() }

        deleteCustomerUseCase.delete(input)

        verify(exactly = 1) { findCustomerByIdPort.findById(CUSTOMER_ID) }
        verify(exactly = 1) {
            customerRepositoryPort.save(
                match {
                    it.customerId == CUSTOMER_ID &&
                            it.status == BLOCKED &&
                            it.updatedAt != null
                }
            )
        }
    }

    @Test
    fun `should throw exception when delete status is active`() {

        val exception = assertThrows<IllegalArgumentException> {
            buildDeleteCustomerInput(
                status = ACTIVE
            )
        }

        assertThat(exception.message).isEqualTo(DELETE_STATUS_MUST_BE_INACTIVE_OR_BLOCKED)
    }
}