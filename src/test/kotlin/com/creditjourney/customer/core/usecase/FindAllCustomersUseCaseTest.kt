package com.creditjourney.customer.core.usecase

import com.creditjourney.customer.core.domain.model.CustomerSlice
import com.creditjourney.customer.core.port.input.FindAllCustomersInput
import com.creditjourney.customer.core.port.output.CustomerRepositoryPort
import com.creditjourney.customer.core.usecase.builder.buildCustomer
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
class FindAllCustomersUseCaseTest(
    @param:MockK
    private val customerRepositoryPort: CustomerRepositoryPort,

    @param:InjectMockKs
    private val findAllCustomersUseCase: FindAllCustomersUseCase
) {

    @AfterEach
    fun tearDown() {
        clearMocks(customerRepositoryPort)
    }

    @Test
    fun `should find all customers successfully`() {

        val input = FindAllCustomersInput(page = 0, size = 30)
        val customerSlice = com.creditjourney.customer.core.domain.model.CustomerSlice(
            content = listOf(buildCustomer()),
            page = 0,
            size = 30,
            hasNext = false
        )

        every { customerRepositoryPort.findAll(page = 0, size = 30) } returns customerSlice

        val result = findAllCustomersUseCase.findAll(input)

        assertThat(result).isEqualTo(customerSlice)

        verify(exactly = 1) {
            customerRepositoryPort.findAll(page = 0, size = 30)
        }
    }

    @Test
    fun `should return empty list when there are no customers`() {

        val input = FindAllCustomersInput(page = 0, size = 30)
        val customerSlice = com.creditjourney.customer.core.domain.model.CustomerSlice(
            content = emptyList(),
            page = 0,
            size = 30,
            hasNext = false
        )

        every { customerRepositoryPort.findAll(page = 0, size = 30) } returns customerSlice

        val result = findAllCustomersUseCase.findAll(input)

        assertThat(result.content).isEmpty()
        assertThat(result.hasNext).isFalse()

        verify(exactly = 1) {
            customerRepositoryPort.findAll(page = 0, size = 30)
        }
    }

    @Test
    fun `should throw exception when page is negative`() {

        val exception = assertThrows<IllegalArgumentException> {
            FindAllCustomersInput(page = -1, size = 30)
        }

        assertThat(exception.message).isEqualTo("Page must not be negative")
    }

    @Test
    fun `should throw exception when size is zero`() {

        val exception = assertThrows<IllegalArgumentException> {
            FindAllCustomersInput(page = 0, size = 0)
        }

        assertThat(exception.message).isEqualTo("Size must be between 1 and 100")
    }

    @Test
    fun `should throw exception when size is greater than one hundred`() {

        val exception = assertThrows<IllegalArgumentException> {
            FindAllCustomersInput(page = 0, size = 101)
        }

        assertThat(exception.message).isEqualTo("Size must be between 1 and 100")
    }

    @Test
    fun `should create input with default values`() {

        val input = FindAllCustomersInput()

        assertThat(input.page).isEqualTo(0)
        assertThat(input.size).isEqualTo(30)
    }

    @Test
    fun `should create customer slice successfully`() {

        val customerSlice = CustomerSlice(
            content = listOf(buildCustomer()),
            page = 0,
            size = 30,
            hasNext = false
        )

        assertThat(customerSlice.content).hasSize(1)
        assertThat(customerSlice.page).isEqualTo(0)
        assertThat(customerSlice.size).isEqualTo(30)
        assertThat(customerSlice.hasNext).isFalse()
    }
}