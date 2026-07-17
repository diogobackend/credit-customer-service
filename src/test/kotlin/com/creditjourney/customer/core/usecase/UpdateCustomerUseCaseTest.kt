package com.creditjourney.customer.core.usecase

import com.creditjourney.customer.core.domain.exception.CustomerAlreadyExistsException
import com.creditjourney.customer.core.domain.exception.CustomerNotFoundException
import com.creditjourney.customer.core.port.FindCustomerByIdPort
import com.creditjourney.customer.core.port.output.CustomerRepositoryPort
import com.creditjourney.customer.core.usecase.builder.CustomerBuilderConstants.CUSTOMER_ID
import com.creditjourney.customer.core.usecase.builder.CustomerInputBuilderConstants.CUSTOMER_EMAIL
import com.creditjourney.customer.core.usecase.builder.CustomerInputBuilderConstants.CUSTOMER_PHONE
import com.creditjourney.customer.core.usecase.builder.buildCustomer
import com.creditjourney.customer.core.usecase.builder.buildUpdateCustomerInput
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
import java.math.BigDecimal

@ExtendWith(MockKExtension::class)
class UpdateCustomerUseCaseTest(
    @param:MockK
    private val findCustomerByIdPort: FindCustomerByIdPort,

    @param:MockK
    private val customerRepositoryPort: CustomerRepositoryPort,

    @param:InjectMockKs
    private val updateCustomerUseCase: UpdateCustomerUseCase
) {

    @AfterEach
    fun tearDown() {
        clearMocks(findCustomerByIdPort, customerRepositoryPort)
    }

    @Test
    fun `should update customer successfully`() {

        val input = buildUpdateCustomerInput()
        val customer = buildCustomer()

        every { findCustomerByIdPort.findById(CUSTOMER_ID) } returns customer
        every { customerRepositoryPort.existsByEmail(match { it.value == input.email }) } returns false
        every { customerRepositoryPort.existsByPhone(input.phone!!) } returns false
        every { customerRepositoryPort.save(any()) } answers { firstArg() }

        val result = updateCustomerUseCase.update(input)

        assertThat(result.customerId).isEqualTo(CUSTOMER_ID)
        assertThat(result.name).isEqualTo("Maria Souza")
        assertThat(result.email.value).isEqualTo("maria.souza@email.com")
        assertThat(result.phone).isEqualTo("11988887777")
        assertThat(result.income.value).isEqualByComparingTo(BigDecimal("2500.00"))
        assertThat(result.updatedAt).isNotNull()

        verify(exactly = 1) { findCustomerByIdPort.findById(CUSTOMER_ID) }
        verify(exactly = 1) { customerRepositoryPort.existsByEmail(match { it.value == input.email }) }
        verify(exactly = 1) { customerRepositoryPort.existsByPhone(input.phone!!) }
        verify(exactly = 1) { customerRepositoryPort.save(any()) }
    }

    @Test
    fun `should update only customer name successfully`() {

        val input = buildUpdateCustomerInput(
            name = "Maria Silva",
            email = null,
            phone = null,
            income = null
        )

        val customer = buildCustomer()

        every { findCustomerByIdPort.findById(CUSTOMER_ID) } returns customer
        every { customerRepositoryPort.save(any()) } answers { firstArg() }

        val result = updateCustomerUseCase.update(input)

        assertThat(result.name).isEqualTo("Maria Silva")
        assertThat(result.email.value).isEqualTo(CUSTOMER_EMAIL)
        assertThat(result.phone).isEqualTo(CUSTOMER_PHONE)

        verify(exactly = 1) { findCustomerByIdPort.findById(CUSTOMER_ID) }
        verify(exactly = 0) { customerRepositoryPort.existsByEmail(any()) }
        verify(exactly = 0) { customerRepositoryPort.existsByPhone(any()) }
        verify(exactly = 1) { customerRepositoryPort.save(any()) }
    }

    @Test
    fun `should update customer with same email and phone successfully`() {

        val input = buildUpdateCustomerInput(
            name = null,
            email = CUSTOMER_EMAIL,
            phone = CUSTOMER_PHONE,
            income = null
        )

        val customer = buildCustomer()

        every { findCustomerByIdPort.findById(CUSTOMER_ID) } returns customer
        every { customerRepositoryPort.save(any()) } answers { firstArg() }

        val result = updateCustomerUseCase.update(input)

        assertThat(result.email.value).isEqualTo(CUSTOMER_EMAIL)
        assertThat(result.phone).isEqualTo(CUSTOMER_PHONE)

        verify(exactly = 1) { findCustomerByIdPort.findById(CUSTOMER_ID) }
        verify(exactly = 0) { customerRepositoryPort.existsByEmail(any()) }
        verify(exactly = 0) { customerRepositoryPort.existsByPhone(any()) }
        verify(exactly = 1) { customerRepositoryPort.save(any()) }
    }

    @Test
    fun `should throw exception when customer is not found`() {

        val input = buildUpdateCustomerInput()

        every { findCustomerByIdPort.findById(CUSTOMER_ID) } throws CustomerNotFoundException(CUSTOMER_ID)

        val exception = assertThrows<CustomerNotFoundException> {
            updateCustomerUseCase.update(input)
        }

        assertThat(exception.message).isEqualTo("Customer not found with customerId: $CUSTOMER_ID")

        verify(exactly = 1) { findCustomerByIdPort.findById(CUSTOMER_ID) }
        verify(exactly = 0) { customerRepositoryPort.save(any()) }
    }

    @Test
    fun `should throw exception when email already exists`() {

        val input = buildUpdateCustomerInput(
            email = "duplicado@email.com",
            phone = null
        )

        val customer = buildCustomer()

        every { findCustomerByIdPort.findById(CUSTOMER_ID) } returns customer
        every { customerRepositoryPort.existsByEmail(match { it.value == "duplicado@email.com" }) } returns true

        val exception = assertThrows<CustomerAlreadyExistsException> {
            updateCustomerUseCase.update(input)
        }

        assertThat(exception.message).isEqualTo("Customer already exists with email: duplicado@email.com")

        verify(exactly = 1) { findCustomerByIdPort.findById(CUSTOMER_ID) }
        verify(exactly = 1) { customerRepositoryPort.existsByEmail(match { it.value == "duplicado@email.com" }) }
        verify(exactly = 0) { customerRepositoryPort.save(any()) }
    }

    @Test
    fun `should throw exception when phone already exists`() {

        val input = buildUpdateCustomerInput(
            email = null,
            phone = "11977776666"
        )

        val customer = buildCustomer()

        every { findCustomerByIdPort.findById(CUSTOMER_ID) } returns customer
        every { customerRepositoryPort.existsByPhone("11977776666") } returns true

        val exception = assertThrows<CustomerAlreadyExistsException> {
            updateCustomerUseCase.update(input)
        }

        assertThat(exception.message).isEqualTo("Customer already exists with phone: 11977776666")

        verify(exactly = 1) { findCustomerByIdPort.findById(CUSTOMER_ID) }
        verify(exactly = 1) { customerRepositoryPort.existsByPhone("11977776666") }
        verify(exactly = 0) { customerRepositoryPort.save(any()) }
    }

    @Test
    fun `should throw exception when no field is informed`() {

        val exception = assertThrows<IllegalArgumentException> {
            buildUpdateCustomerInput(
                name = null,
                email = null,
                phone = null,
                income = null
            )
        }

        assertThat(exception.message).isEqualTo("At least one field must be informed")
    }

    @Test
    fun `should throw exception when phone is blank`() {

        val exception = assertThrows<IllegalArgumentException> {
            buildUpdateCustomerInput(
                phone = " "
            )
        }

        assertThat(exception.message).isEqualTo("Phone must not be blank")
    }

    @Test
    fun `should throw exception when email is invalid`() {

        val input = buildUpdateCustomerInput(
            email = "email-invalido",
            phone = null
        )

        val customer = buildCustomer()

        every { findCustomerByIdPort.findById(CUSTOMER_ID) } returns customer

        val exception = assertThrows<IllegalArgumentException> {
            updateCustomerUseCase.update(input)
        }

        assertThat(exception.message).isEqualTo("Email must be valid")

        verify(exactly = 0) { customerRepositoryPort.save(any()) }
    }

    @Test
    fun `should throw exception when income is negative`() {

        val input = buildUpdateCustomerInput(
            email = null,
            phone = null,
            income = BigDecimal("-1.00")
        )

        val customer = buildCustomer()

        every { findCustomerByIdPort.findById(CUSTOMER_ID) } returns customer

        val exception = assertThrows<IllegalArgumentException> {
            updateCustomerUseCase.update(input)
        }

        assertThat(exception.message).isEqualTo("Income must not be negative")

        verify(exactly = 0) { customerRepositoryPort.save(any()) }
    }
}