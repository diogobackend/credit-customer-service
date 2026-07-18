package com.creditjourney.customer.core.usecase

import com.creditjourney.customer.core.builder.buildCustomer
import com.creditjourney.customer.core.common.messages.CustomerMessages.CUSTOMER_ID
import com.creditjourney.customer.core.common.messages.CustomerMessages.CUSTOMER_NOT_FOUND_WITH_CUSTOMER_ID
import com.creditjourney.customer.core.domain.exception.CustomerNotFoundException
import com.creditjourney.customer.core.port.output.CustomerRepositoryPort
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class FindCustomerByIdUseCaseTest(
    @param:MockK
    private val customerRepositoryPort: CustomerRepositoryPort,
    @param:InjectMockKs
    private val findCustomerByIdUseCase: FindCustomerByIdUseCase,
) {
    @AfterEach
    fun tearDown() {
        clearMocks(customerRepositoryPort)
    }

    @Test
    fun `should find customer by id successfully`() {
        val customer = buildCustomer()

        every { customerRepositoryPort.findById(CUSTOMER_ID) } returns customer

        val result = findCustomerByIdUseCase.findById(CUSTOMER_ID)

        assertThat(result).isEqualTo(customer)

        verify(exactly = 1) {
            customerRepositoryPort.findById(CUSTOMER_ID)
        }
    }

    @Test
    fun `should throw exception when customer is not found`() {
        every { customerRepositoryPort.findById(CUSTOMER_ID) } returns null

        val exception =
            assertThrows<CustomerNotFoundException> {
                findCustomerByIdUseCase.findById(CUSTOMER_ID)
            }

        assertThat(exception.message)
            .isEqualTo("$CUSTOMER_NOT_FOUND_WITH_CUSTOMER_ID: $CUSTOMER_ID")

        verify(exactly = 1) {
            customerRepositoryPort.findById(CUSTOMER_ID)
        }
    }
}
