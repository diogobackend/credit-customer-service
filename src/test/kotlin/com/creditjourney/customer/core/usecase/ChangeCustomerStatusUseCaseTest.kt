package com.creditjourney.customer.core.usecase

import com.creditjourney.customer.core.builder.buildChangeCustomerStatusInput
import com.creditjourney.customer.core.builder.buildCustomer
import com.creditjourney.customer.core.common.messages.CustomerMessages.CUSTOMER_ID
import com.creditjourney.customer.core.common.messages.CustomerMessages.CUSTOMER_NOT_FOUND_WITH_CUSTOMER_ID
import com.creditjourney.customer.core.domain.exception.CustomerNotFoundException
import com.creditjourney.customer.core.domain.model.CustomerStatus
import com.creditjourney.customer.core.domain.model.CustomerStatus.ACTIVE
import com.creditjourney.customer.core.domain.model.CustomerStatus.BLOCKED
import com.creditjourney.customer.core.domain.model.CustomerStatus.INACTIVE
import com.creditjourney.customer.core.port.FindCustomerByIdPort
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
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@ExtendWith(MockKExtension::class)
class ChangeCustomerStatusUseCaseTest(
    @param:MockK
    private val findCustomerByIdPort: FindCustomerByIdPort,
    @param:MockK
    private val customerRepositoryPort: CustomerRepositoryPort,
    @param:InjectMockKs
    private val changeCustomerStatusUseCase: ChangeCustomerStatusUseCase,
) {
    @AfterEach
    fun tearDown() {
        clearMocks(findCustomerByIdPort, customerRepositoryPort)
    }

    @ParameterizedTest
    @MethodSource("customerStatusScenarios")
    fun `should change customer status successfully`(
        currentStatus: CustomerStatus,
        newStatus: CustomerStatus,
    ) {
        val input =
            buildChangeCustomerStatusInput(
                status = newStatus,
            )

        val customer =
            buildCustomer(
                status = currentStatus,
            )

        every { findCustomerByIdPort.findById(CUSTOMER_ID) } returns customer
        every { customerRepositoryPort.save(any()) } answers { firstArg() }

        val result = changeCustomerStatusUseCase.change(input)

        assertThat(result.customerId).isEqualTo(CUSTOMER_ID)
        assertThat(result.status).isEqualTo(newStatus)
        assertThat(result.updatedAt).isNotNull()

        verify(exactly = 1) { findCustomerByIdPort.findById(CUSTOMER_ID) }
        verify(exactly = 1) {
            customerRepositoryPort.save(
                match {
                    it.customerId == CUSTOMER_ID &&
                        it.status == newStatus &&
                        it.updatedAt != null
                },
            )
        }
    }

    @Test
    fun `should throw exception when customer is not found`() {
        val input = buildChangeCustomerStatusInput()

        every { findCustomerByIdPort.findById(CUSTOMER_ID) } throws CustomerNotFoundException(CUSTOMER_ID)

        val exception =
            assertThrows<CustomerNotFoundException> {
                changeCustomerStatusUseCase.change(input)
            }

        assertThat(exception.message).isEqualTo("$CUSTOMER_NOT_FOUND_WITH_CUSTOMER_ID: $CUSTOMER_ID")

        verify(exactly = 1) { findCustomerByIdPort.findById(CUSTOMER_ID) }
        verify(exactly = 0) { customerRepositoryPort.save(any()) }
    }

    companion object {
        @JvmStatic
        fun customerStatusScenarios(): List<Arguments> =
            listOf(
                Arguments.of(INACTIVE, ACTIVE),
                Arguments.of(ACTIVE, INACTIVE),
                Arguments.of(ACTIVE, BLOCKED),
            )
    }
}
