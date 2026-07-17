package com.creditjourney.customer.core.usecase

import com.creditjourney.customer.core.domain.model.Customer
import com.creditjourney.customer.core.domain.model.CustomerStatus.INACTIVE
import com.creditjourney.customer.core.domain.model.CustomerStatus.ACTIVE
import com.creditjourney.customer.core.port.input.FindAllCustomersInput
import com.creditjourney.customer.core.port.output.CustomerRepositoryPort
import com.creditjourney.customer.core.builder.buildCustomer
import com.creditjourney.customer.core.builder.buildCustomerSlice
import com.creditjourney.customer.core.builder.buildFindAllCustomersInput
import com.creditjourney.customer.core.common.messages.CustomerMessages.CUSTOMER_DOCUMENT
import com.creditjourney.customer.core.common.messages.CustomerMessages.CUSTOMER_EMAIL
import com.creditjourney.customer.core.common.messages.CustomerMessages.CUSTOMER_PHONE
import com.creditjourney.customer.core.common.messages.CustomerMessages.MAX_INCOME_MUST_NOT_BE_NEGATIVE
import com.creditjourney.customer.core.common.messages.CustomerMessages.MIN_INCOME_MUST_BE_LESS_THAN_OR_EQUAL_TO_MAX_INCOME
import com.creditjourney.customer.core.common.messages.CustomerMessages.MIN_INCOME_MUST_NOT_BE_NEGATIVE
import com.creditjourney.customer.core.common.messages.CustomerMessages.PAGE_MUST_NOT_BE_NEGATIVE
import com.creditjourney.customer.core.common.messages.CustomerMessages.SIZE_MUST_BE_BETWEEN_ONE_AND_ONE_HUNDRED
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
import java.math.BigDecimal

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

        val input = buildFindAllCustomersInput()
        val customerSlice = buildCustomerSlice()

        every {
            customerRepositoryPort.findAll(
                page = 0,
                size = 30,
                status = null,
                search = null,
                name = null,
                minIncome = null,
                maxIncome = null
            )
        } returns customerSlice

        val result = findAllCustomersUseCase.findAll(input)

        assertThat(result).isEqualTo(customerSlice)

        verify(exactly = 1) {
            customerRepositoryPort.findAll(
                page = 0,
                size = 30,
                status = null,
                search = null,
                name = null,
                minIncome = null,
                maxIncome = null
            )
        }
    }

    @Test
    fun `should return empty list when there are no customers`() {

        val input = buildFindAllCustomersInput()
        val customerSlice = buildCustomerSlice(
            content = emptyList()
        )

        every { customerRepositoryPort.findAll(
            page = 0,
            size = 30,
            status = null,
            search = null,
            name = null,
            minIncome = null,
            maxIncome = null
        ) } returns customerSlice

        val result = findAllCustomersUseCase.findAll(input)

        assertThat(result.content).isEmpty()
        assertThat(result.hasNext).isFalse()

        verify(exactly = 1) {
            customerRepositoryPort.findAll(
                page = 0,
                size = 30,
                status = null,
                search = null,
                name = null,
                minIncome = null,
                maxIncome = null
            )
        }
    }

    @Test
    fun `should throw exception when page is negative`() {

        val exception = assertThrows<IllegalArgumentException> {
            buildFindAllCustomersInput(page = -1)
        }

        assertThat(exception.message).isEqualTo(PAGE_MUST_NOT_BE_NEGATIVE)
    }

    @Test
    fun `should throw exception when size is zero`() {

        val exception = assertThrows<IllegalArgumentException> {
            buildFindAllCustomersInput(size = 0)
        }

        assertThat(exception.message).isEqualTo(SIZE_MUST_BE_BETWEEN_ONE_AND_ONE_HUNDRED)
    }

    @Test
    fun `should throw exception when size is greater than one hundred`() {

        val exception = assertThrows<IllegalArgumentException> {
            buildFindAllCustomersInput(size = 101)
        }

        assertThat(exception.message).isEqualTo(SIZE_MUST_BE_BETWEEN_ONE_AND_ONE_HUNDRED)
    }

    @Test
    fun `should create input with default values`() {

        val input = FindAllCustomersInput()

        assertThat(input.page).isEqualTo(0)
        assertThat(input.size).isEqualTo(30)
        assertThat(input.status).isNull()
        assertThat(input.search).isNull()
        assertThat(input.name).isNull()
        assertThat(input.minIncome).isNull()
        assertThat(input.maxIncome).isNull()
    }

    @Test
    fun `should create customer slice successfully`() {

        val customerSlice = buildCustomerSlice(
            totalElements = 1
        )

        assertThat(customerSlice.content).hasSize(1)
        assertThat(customerSlice.page).isEqualTo(0)
        assertThat(customerSlice.size).isEqualTo(30)
        assertThat(customerSlice.hasNext).isFalse()
        assertThat(customerSlice.totalElements).isEqualTo(1)
    }

    @Test
    fun `should find active customers successfully`() {

        val input = buildFindAllCustomersInput(
            status = ACTIVE
        )

        val customerSlice = buildCustomerSlice(
            content = listOf(buildCustomer(status = ACTIVE))
        )

        every {
            customerRepositoryPort.findAll(
                page = 0,
                size = 30,
                status = ACTIVE,
                search = null,
                name = null,
                minIncome = null,
                maxIncome = null
            )
        } returns customerSlice

        val result = findAllCustomersUseCase.findAll(input)

        assertThat(result).isEqualTo(customerSlice)
        assertThat(result.content).allMatch { it.status == ACTIVE }

        verify(exactly = 1) {
            customerRepositoryPort.findAll(
                page = 0,
                size = 30,
                status = ACTIVE,
                search = null,
                name = null,
                minIncome = null,
                maxIncome = null
            )
        }
    }

    @Test
    fun `should find inactive customers successfully`() {

        val input = buildFindAllCustomersInput(
            status = INACTIVE
        )

        val customerSlice = buildCustomerSlice(
            content = listOf(buildCustomer(status = INACTIVE))
        )

        every {
            customerRepositoryPort.findAll(
                page = 0,
                size = 30,
                status = INACTIVE,
                search = null,
                name = null,
                minIncome = null,
                maxIncome = null
            )
        } returns customerSlice

        val result = findAllCustomersUseCase.findAll(input)

        assertThat(result).isEqualTo(customerSlice)
        assertThat(result.content).allMatch { it.status == INACTIVE }

        verify(exactly = 1) {
            customerRepositoryPort.findAll(
                page = 0,
                size = 30,
                status = INACTIVE,
                search = null,
                name = null,
                minIncome = null,
                maxIncome = null
            )
        }
    }

    @Test
    fun `should trim name filter before searching`() {

        val input = buildFindAllCustomersInput(
            name = "  Ma  "
        )

        val customerSlice = buildCustomerSlice(
            content = listOf(buildCustomer(name = "Maria"))
        )

        every {
            customerRepositoryPort.findAll(
                page = 0,
                size = 30,
                status = null,
                search = null,
                name = "Ma",
                minIncome = null,
                maxIncome = null
            )
        } returns customerSlice

        val result = findAllCustomersUseCase.findAll(input)

        assertThat(result).isEqualTo(customerSlice)

        verify(exactly = 1) {
            customerRepositoryPort.findAll(
                page = 0,
                size = 30,
                status = null,
                search = null,
                name = "Ma",
                minIncome = null,
                maxIncome = null
            )
        }
    }

    @Test
    fun `should find customers by partial name successfully`() {

        val name = "Ma"

        val input = buildFindAllCustomersInput(
            name = name
        )

        val customerSlice = buildCustomerSlice(
            content = listOf(
                buildCustomer(name = "Maria"),
                buildCustomer(name = "Mariana"),
                buildCustomer(name = "Marinalva")
            )
        )

        every {
            customerRepositoryPort.findAll(
                page = 0,
                size = 30,
                status = null,
                search = null,
                name = name,
                minIncome = null,
                maxIncome = null
            )
        } returns customerSlice

        val result = findAllCustomersUseCase.findAll(input)

        assertThat(result).isEqualTo(customerSlice)
        assertThat(result.content).allMatch { it.name.contains(name) }

        verify(exactly = 1) {
            customerRepositoryPort.findAll(
                page = 0,
                size = 30,
                status = null,
                search = null,
                name = name,
                minIncome = null,
                maxIncome = null
            )
        }
    }

    @Test
    fun `should search with null name when name filter is null`() {

        val input = buildFindAllCustomersInput()

        val customerSlice = buildCustomerSlice(
            content = emptyList()
        )

        every {
            customerRepositoryPort.findAll(
                page = 0,
                size = 30,
                status = null,
                search = null,
                name = null,
                minIncome = null,
                maxIncome = null
            )
        } returns customerSlice

        val result = findAllCustomersUseCase.findAll(input)

        assertThat(result).isEqualTo(customerSlice)

        verify(exactly = 1) {
            customerRepositoryPort.findAll(
                page = 0,
                size = 30,
                status = null,
                search = null,
                name = null,
                minIncome = null,
                maxIncome = null
            )
        }
    }

    @Test
    fun `should search with null name when name filter is blank`() {

        val input = buildFindAllCustomersInput(
            name = "  "
        )

        val customerSlice = buildCustomerSlice(
            content = emptyList()
        )

        every {
            customerRepositoryPort.findAll(
                page = 0,
                size = 30,
                status = null,
                search = null,
                name = null,
                minIncome = null,
                maxIncome = null
            )
        } returns customerSlice

        val result = findAllCustomersUseCase.findAll(input)

        assertThat(result).isEqualTo(customerSlice)

        verify(exactly = 1) {
            customerRepositoryPort.findAll(
                page = 0,
                size = 30,
                status = null,
                search = null,
                name = null,
                minIncome = null,
                maxIncome = null
            )
        }
    }

    @Test
    fun `should find customers with min income successfully`() {

        val input = buildFindAllCustomersInput(
            minIncome = BigDecimal("500.00")
        )

        val customerSlice = buildCustomerSlice(
            content = listOf(buildCustomer(income = BigDecimal("1000.00")))
        )

        every {
            customerRepositoryPort.findAll(
                page = 0,
                size = 30,
                status = null,
                search = null,
                name = null,
                minIncome = BigDecimal("500.00"),
                maxIncome = null
            )
        } returns customerSlice

        val result = findAllCustomersUseCase.findAll(input)

        assertThat(result).isEqualTo(customerSlice)

        verify(exactly = 1) {
            customerRepositoryPort.findAll(
                page = 0,
                size = 30,
                status = null,
                search = null,
                name = null,
                minIncome = BigDecimal("500.00"),
                maxIncome = null
            )
        }
    }

    @Test
    fun `should find customers with max income successfully`() {

        val input = buildFindAllCustomersInput(
            maxIncome = BigDecimal("100.00")
        )

        val customerSlice = buildCustomerSlice(
            content = listOf(buildCustomer(income = BigDecimal("50.00")))
        )

        every {
            customerRepositoryPort.findAll(
                page = 0,
                size = 30,
                status = null,
                search = null,
                name = null,
                minIncome = null,
                maxIncome = BigDecimal("100.00")
            )
        } returns customerSlice

        val result = findAllCustomersUseCase.findAll(input)

        assertThat(result).isEqualTo(customerSlice)

        verify(exactly = 1) {
            customerRepositoryPort.findAll(
                page = 0,
                size = 30,
                status = null,
                search = null,
                name = null,
                minIncome = null,
                maxIncome = BigDecimal("100.00")
            )
        }
    }

    @Test
    fun `should find customers with min and max income successfully`() {

        val input = buildFindAllCustomersInput(
            minIncome = BigDecimal("100.00"),
            maxIncome = BigDecimal("500.00")
        )

        val customerSlice = buildCustomerSlice(
            content = listOf(buildCustomer(income = BigDecimal("300.00")))
        )

        every {
            customerRepositoryPort.findAll(
                page = 0,
                size = 30,
                status = null,
                search = null,
                name = null,
                minIncome = BigDecimal("100.00"),
                maxIncome = BigDecimal("500.00")
            )
        } returns customerSlice

        val result = findAllCustomersUseCase.findAll(input)

        assertThat(result).isEqualTo(customerSlice)

        verify(exactly = 1) {
            customerRepositoryPort.findAll(
                page = 0,
                size = 30,
                status = null,
                search = null,
                name = null,
                minIncome = BigDecimal("100.00"),
                maxIncome = BigDecimal("500.00")
            )
        }
    }

    @Test
    fun `should throw exception when min income is negative`() {

        val exception = assertThrows<IllegalArgumentException> {
            buildFindAllCustomersInput(
                minIncome = BigDecimal("-1.00")
            )
        }

        assertThat(exception.message).isEqualTo(MIN_INCOME_MUST_NOT_BE_NEGATIVE)
    }

    @Test
    fun `should throw exception when max income is negative`() {

        val exception = assertThrows<IllegalArgumentException> {
            buildFindAllCustomersInput(
                maxIncome = BigDecimal("-1.00")
            )
        }

        assertThat(exception.message).isEqualTo(MAX_INCOME_MUST_NOT_BE_NEGATIVE)
    }

    @Test
    fun `should throw exception when min income is greater than max income`() {

        val exception = assertThrows<IllegalArgumentException> {
            buildFindAllCustomersInput(
                minIncome = BigDecimal("500.00"),
                maxIncome = BigDecimal("100.00")
            )
        }

        assertThat(exception.message)
            .isEqualTo(MIN_INCOME_MUST_BE_LESS_THAN_OR_EQUAL_TO_MAX_INCOME)
    }

    @Test
    fun `should return total elements successfully`() {

        val input = buildFindAllCustomersInput()

        val customerSlice = buildCustomerSlice(
            content = listOf(
                buildCustomer(name = "Maria"),
                buildCustomer(name = "João")
            ),
            totalElements = 5
        )

        every {
            customerRepositoryPort.findAll(
                page = 0,
                size = 30,
                status = null,
                search = null,
                name = null,
                minIncome = null,
                maxIncome = null
            )
        } returns customerSlice

        val result = findAllCustomersUseCase.findAll(input)

        assertThat(result.totalElements).isEqualTo(5)
        assertThat(result.content).hasSize(2)

        verify(exactly = 1) {
            customerRepositoryPort.findAll(
                page = 0,
                size = 30,
                status = null,
                search = null,
                name = null,
                minIncome = null,
                maxIncome = null
            )
        }
    }

    @ParameterizedTest
    @MethodSource("searchFilters")
    fun `should find customers by search successfully`(
        search: String,
        assertion: (Customer) -> Boolean
    ) {

        val input = buildFindAllCustomersInput(
            search = search
        )

        val customerSlice = buildCustomerSlice(
            content = listOf(buildCustomer()),
            page = 0,
            size = 30,
            hasNext = false
        )

        every {
            customerRepositoryPort.findAll(
                page = 0,
                size = 30,
                status = null,
                search = search,
                name = null,
                minIncome = null,
                maxIncome = null
            )
        } returns customerSlice

        val result = findAllCustomersUseCase.findAll(input)

        assertThat(result).isEqualTo(customerSlice)
        assertThat(result.content).allMatch(assertion)

        verify(exactly = 1) {
            customerRepositoryPort.findAll(
                page = 0,
                size = 30,
                status = null,
                search = search,
                name = null,
                minIncome = null,
                maxIncome = null
            )
        }
    }

    companion object {

        @JvmStatic
        fun searchFilters(): List<Arguments> =
            listOf(
                Arguments.of(
                    CUSTOMER_DOCUMENT,
                    { customer: Customer -> customer.document.value == CUSTOMER_DOCUMENT }
                ),
                Arguments.of(
                    CUSTOMER_EMAIL,
                    { customer: Customer -> customer.email.value == CUSTOMER_EMAIL }
                ),
                Arguments.of(
                    CUSTOMER_PHONE,
                    { customer: Customer -> customer.phone == CUSTOMER_PHONE }
                )
            )
    }
}