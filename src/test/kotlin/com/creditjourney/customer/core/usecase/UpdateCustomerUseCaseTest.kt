package com.creditjourney.customer.core.usecase

import com.creditjourney.customer.core.domain.exception.CustomerAlreadyExistsException
import com.creditjourney.customer.core.domain.exception.CustomerNotFoundException
import com.creditjourney.customer.core.port.FindCustomerByIdPort
import com.creditjourney.customer.core.port.output.CustomerRepositoryPort
import com.creditjourney.customer.core.builder.buildCustomer
import com.creditjourney.customer.core.builder.buildUpdateCustomerInput
import com.creditjourney.customer.core.common.messages.CustomerMessages.AT_LEAST_ONE_FIELD_MUST_BE_INFORMED
import com.creditjourney.customer.core.common.messages.CustomerMessages.CUSTOMER_ALREADY_EXISTS_WITH
import com.creditjourney.customer.core.common.messages.CustomerMessages.CUSTOMER_EMAIL
import com.creditjourney.customer.core.common.messages.CustomerMessages.CUSTOMER_EMAIL_UPDATED
import com.creditjourney.customer.core.common.messages.CustomerMessages.CUSTOMER_ID
import com.creditjourney.customer.core.common.messages.CustomerMessages.CUSTOMER_INCOME_UPDATED
import com.creditjourney.customer.core.common.messages.CustomerMessages.CUSTOMER_NAME_UPDATED
import com.creditjourney.customer.core.common.messages.CustomerMessages.CUSTOMER_NOT_FOUND_WITH_CUSTOMER_ID
import com.creditjourney.customer.core.common.messages.CustomerMessages.CUSTOMER_PHONE
import com.creditjourney.customer.core.common.messages.CustomerMessages.CUSTOMER_PHONE_UPDATED
import com.creditjourney.customer.core.common.messages.CustomerMessages.EMAIL_MUST_BE_VALID
import com.creditjourney.customer.core.common.messages.CustomerMessages.INCOME_MUST_NOT_BE_NEGATIVE
import com.creditjourney.customer.core.common.messages.CustomerMessages.PHONE_MUST_NOT_BE_BLANK
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
        assertThat(result.name).isEqualTo(CUSTOMER_NAME_UPDATED)
        assertThat(result.email.value).isEqualTo(CUSTOMER_EMAIL_UPDATED)
        assertThat(result.phone).isEqualTo(CUSTOMER_PHONE_UPDATED)
        assertThat(result.income.value).isEqualByComparingTo(CUSTOMER_INCOME_UPDATED)
        assertThat(result.updatedAt).isNotNull()

        verify(exactly = 1) { findCustomerByIdPort.findById(CUSTOMER_ID) }
        verify(exactly = 1) { customerRepositoryPort.existsByEmail(match { it.value == input.email }) }
        verify(exactly = 1) { customerRepositoryPort.existsByPhone(input.phone!!) }
        verify(exactly = 1) { customerRepositoryPort.save(any()) }
    }

    @Test
    fun `should update only customer name successfully`() {

        val input = buildUpdateCustomerInput(
            name = CUSTOMER_NAME_UPDATED,
            email = null,
            phone = null,
            income = null
        )

        val customer = buildCustomer()

        every { findCustomerByIdPort.findById(CUSTOMER_ID) } returns customer
        every { customerRepositoryPort.save(any()) } answers { firstArg() }

        val result = updateCustomerUseCase.update(input)

        assertThat(result.name).isEqualTo(CUSTOMER_NAME_UPDATED)
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

        assertThat(exception.message).isEqualTo("$CUSTOMER_NOT_FOUND_WITH_CUSTOMER_ID: $CUSTOMER_ID")

        verify(exactly = 1) { findCustomerByIdPort.findById(CUSTOMER_ID) }
        verify(exactly = 0) { customerRepositoryPort.save(any()) }
    }

    @Test
    fun `should throw exception when email already exists`() {

        val input = buildUpdateCustomerInput(
            email = CUSTOMER_EMAIL_UPDATED,
            phone = null
        )

        val customer = buildCustomer()

        every { findCustomerByIdPort.findById(CUSTOMER_ID) } returns customer
        every { customerRepositoryPort.existsByEmail(match { it.value == CUSTOMER_EMAIL_UPDATED }) } returns true

        val exception = assertThrows<CustomerAlreadyExistsException> {
            updateCustomerUseCase.update(input)
        }

        assertThat(exception.message)
            .isEqualTo("$CUSTOMER_ALREADY_EXISTS_WITH email: $CUSTOMER_EMAIL_UPDATED")

        verify(exactly = 1) { findCustomerByIdPort.findById(CUSTOMER_ID) }
        verify(exactly = 1) { customerRepositoryPort.existsByEmail(match { it.value == CUSTOMER_EMAIL_UPDATED }) }
        verify(exactly = 0) { customerRepositoryPort.save(any()) }
    }

    @Test
    fun `should throw exception when phone already exists`() {

        val input = buildUpdateCustomerInput(
            email = null,
            phone = CUSTOMER_PHONE_UPDATED
        )

        val customer = buildCustomer()

        every { findCustomerByIdPort.findById(CUSTOMER_ID) } returns customer
        every { customerRepositoryPort.existsByPhone(CUSTOMER_PHONE_UPDATED) } returns true

        val exception = assertThrows<CustomerAlreadyExistsException> {
            updateCustomerUseCase.update(input)
        }

        assertThat(exception.message).isEqualTo("$CUSTOMER_ALREADY_EXISTS_WITH phone: $CUSTOMER_PHONE_UPDATED")

        verify(exactly = 1) { findCustomerByIdPort.findById(CUSTOMER_ID) }
        verify(exactly = 1) { customerRepositoryPort.existsByPhone(CUSTOMER_PHONE_UPDATED) }
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

        assertThat(exception.message).isEqualTo(AT_LEAST_ONE_FIELD_MUST_BE_INFORMED)
    }

    @Test
    fun `should throw exception when phone is blank`() {

        val exception = assertThrows<IllegalArgumentException> {
            buildUpdateCustomerInput(
                phone = " "
            )
        }

        assertThat(exception.message).isEqualTo(PHONE_MUST_NOT_BE_BLANK)
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

        assertThat(exception.message).isEqualTo(EMAIL_MUST_BE_VALID)

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

        assertThat(exception.message).isEqualTo(INCOME_MUST_NOT_BE_NEGATIVE)

        verify(exactly = 0) { customerRepositoryPort.save(any()) }
    }
}