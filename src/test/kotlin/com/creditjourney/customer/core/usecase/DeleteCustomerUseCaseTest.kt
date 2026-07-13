package com.creditjourney.customer.core.usecase

import com.creditjourney.customer.core.domain.exception.CustomerNotFoundException
import com.creditjourney.customer.core.domain.model.CustomerStatus.INACTIVE
import com.creditjourney.customer.core.port.FindCustomerByIdPort
import com.creditjourney.customer.core.port.output.CustomerRepositoryPort
import com.creditjourney.customer.core.usecase.builder.CustomerBuilderConstants.CUSTOMER_ID
import com.creditjourney.customer.core.usecase.builder.buildCustomer
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

        deleteCustomerUseCase.delete(CUSTOMER_ID)

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

        every {
            findCustomerByIdPort.findById(CUSTOMER_ID)
        } throws CustomerNotFoundException(CUSTOMER_ID)

        val exception = assertThrows<CustomerNotFoundException> {
            deleteCustomerUseCase.delete(CUSTOMER_ID)
        }

        assertThat(exception.message)
            .isEqualTo("Customer not found with customerId: $CUSTOMER_ID")

        verify(exactly = 1) {
            findCustomerByIdPort.findById(CUSTOMER_ID)
        }

        verify(exactly = 0) {
            customerRepositoryPort.save(any())
        }
    }

    @Test
    fun `should keep customer inactive when deleting inactive customer`() {

        val customer = buildCustomer(status = INACTIVE)

        every { findCustomerByIdPort.findById(CUSTOMER_ID) } returns customer
        every { customerRepositoryPort.save(any()) } answers { firstArg() }

        deleteCustomerUseCase.delete(CUSTOMER_ID)

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
}