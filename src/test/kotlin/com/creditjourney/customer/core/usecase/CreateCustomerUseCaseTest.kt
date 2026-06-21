package com.creditjourney.customer.core.usecase

import com.creditjourney.customer.core.domain.exception.CustomerAlreadyExistsException
import com.creditjourney.customer.core.domain.model.Customer
import com.creditjourney.customer.core.domain.model.CustomerStatus
import com.creditjourney.customer.core.port.output.CustomerRepositoryPort
import com.creditjourney.customer.core.usecase.builder.buildCreateCustomerInput
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import io.mockk.verifyOrder
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigDecimal
import com.creditjourney.customer.core.usecase.builder.CustomerInputBuilderConstants.CUSTOMER_DOCUMENT
import com.creditjourney.customer.core.usecase.builder.CustomerInputBuilderConstants.CUSTOMER_EMAIL
import com.creditjourney.customer.core.usecase.builder.CustomerInputBuilderConstants.CUSTOMER_INCOME
import com.creditjourney.customer.core.usecase.builder.CustomerInputBuilderConstants.CUSTOMER_NAME
import com.creditjourney.customer.core.usecase.builder.CustomerInputBuilderConstants.CUSTOMER_PHONE

@ExtendWith(MockKExtension::class)
class CreateCustomerUseCaseTest(
    @param:MockK
    private val customerRepositoryPort: CustomerRepositoryPort,

    @param:InjectMockKs
    private val createCustomerUseCase: CreateCustomerUseCase
) {

    @BeforeEach
    fun setUp() {
        mockCustomerDoesNotExist()
        mockSaveCustomer()
    }

    @AfterEach
    fun tearDown() {
        clearMocks(customerRepositoryPort)
    }

    @Test
    fun `should create customer successfully`() {

        val input = buildCreateCustomerInput()
        val result = createCustomerUseCase.create(input)

        assertCustomerCreatedSuccessfully(result)

        verifyOrder {
            customerRepositoryPort.existsByDocument(match { it.value == CUSTOMER_DOCUMENT })
            customerRepositoryPort.existsByEmail(match { it.value == CUSTOMER_EMAIL })
            customerRepositoryPort.existsByPhone(CUSTOMER_PHONE)
            customerRepositoryPort.save(
                match {
                    it.name == CUSTOMER_NAME &&
                            it.document.value == CUSTOMER_DOCUMENT &&
                            it.email.value == CUSTOMER_EMAIL &&
                            it.phone == CUSTOMER_PHONE &&
                            it.income.value.compareTo(CUSTOMER_INCOME) == 0
                            it.status == CustomerStatus.ACTIVE
                }
            )
        }
    }

    @Test
    fun `should create customer without phone`() {

        val input = buildCreateCustomerInput(phone = null)
        val result = createCustomerUseCase.create(input)

        assertThat(result.phone).isNull()

        verify(exactly = 0) {
            customerRepositoryPort.existsByPhone(any())
        }

        verify(exactly = 1) {
            customerRepositoryPort.save(match { it.phone == null })
        }
    }

    @Test
    fun `should create customer trimming name and phone`() {

        val input = buildCreateCustomerInput(
            name = "  $CUSTOMER_NAME  ",
            phone = " $CUSTOMER_PHONE "
        )

        val result = createCustomerUseCase.create(input)

        assertThat(result.name).isEqualTo(CUSTOMER_NAME)
        assertThat(result.phone).isEqualTo(CUSTOMER_PHONE)

        verify(exactly = 1) {
            customerRepositoryPort.existsByPhone(CUSTOMER_PHONE)
        }

        verify(exactly = 1) {
            customerRepositoryPort.save(
                match {
                    it.name == CUSTOMER_NAME &&
                            it.phone == CUSTOMER_PHONE
                }
            )
        }
    }

    @Test
    fun `should throw exception when document already exists`() {

        val input = buildCreateCustomerInput()

        every {
            customerRepositoryPort.existsByDocument(match { it.value == CUSTOMER_DOCUMENT })
        } returns true


        val exception = assertThrows<CustomerAlreadyExistsException> {
            createCustomerUseCase.create(input)
        }


        assertThat(exception.message)
            .isEqualTo("Customer already exists with document: $CUSTOMER_DOCUMENT")

        verify(exactly = 1) {
            customerRepositoryPort.existsByDocument(match { it.value == CUSTOMER_DOCUMENT })
        }

        verify(exactly = 0) {
            customerRepositoryPort.existsByEmail(any())
            customerRepositoryPort.existsByPhone(any())
            customerRepositoryPort.save(any())
        }
    }

    @Test
    fun `should throw exception when email already exists`() {

        val input = buildCreateCustomerInput()

        every {
            customerRepositoryPort.existsByEmail(match { it.value == CUSTOMER_EMAIL })
        } returns true

        val exception = assertThrows<CustomerAlreadyExistsException> {
            createCustomerUseCase.create(input)
        }

        assertThat(exception.message)
            .isEqualTo("Customer already exists with email: $CUSTOMER_EMAIL")

        verifyOrder {
            customerRepositoryPort.existsByDocument(match { it.value == CUSTOMER_DOCUMENT })
            customerRepositoryPort.existsByEmail(match { it.value == CUSTOMER_EMAIL })
        }

        verify(exactly = 0) {
            customerRepositoryPort.existsByPhone(any())
            customerRepositoryPort.save(any())
        }
    }

    @Test
    fun `should throw exception when phone already exists`() {

        val input = buildCreateCustomerInput()

        every {
            customerRepositoryPort.existsByPhone(CUSTOMER_PHONE)
        } returns true

        val exception = assertThrows<CustomerAlreadyExistsException> {
            createCustomerUseCase.create(input)
        }

        assertThat(exception.message)
            .isEqualTo("Customer already exists with phone: $CUSTOMER_PHONE")

        verifyOrder {
            customerRepositoryPort.existsByDocument(match { it.value == CUSTOMER_DOCUMENT })
            customerRepositoryPort.existsByEmail(match { it.value == CUSTOMER_EMAIL })
            customerRepositoryPort.existsByPhone(CUSTOMER_PHONE)
        }

        verify(exactly = 0) {
            customerRepositoryPort.save(any())
        }
    }

    @Test
    fun `should throw exception when document is blank`() {

        val input = buildCreateCustomerInput(document = "")

        val exception = assertThrows<IllegalArgumentException> {
            createCustomerUseCase.create(input)
        }

        assertThat(exception.message)
            .isEqualTo("Document must not be blank")

        verifyCustomerRepositoryWasNotCalled()
    }

    @Test
    fun `should throw exception when document contains letters`() {

        val input = buildCreateCustomerInput(document = "123abc78900")

        val exception = assertThrows<IllegalArgumentException> {
            createCustomerUseCase.create(input)
        }

        assertThat(exception.message)
            .isEqualTo("Document must contain only digits")

        verifyCustomerRepositoryWasNotCalled()
    }

    @Test
    fun `should throw exception when document has invalid size`() {

        val input = buildCreateCustomerInput(document = "123")

        val exception = assertThrows<IllegalArgumentException> {
            createCustomerUseCase.create(input)
        }

        assertThat(exception.message)
            .isEqualTo("Document must have 11 digits")

        verifyCustomerRepositoryWasNotCalled()
    }

    @Test
    fun `should throw exception when email is blank`() {

        val input = buildCreateCustomerInput(email = "")

        val exception = assertThrows<IllegalArgumentException> {
            createCustomerUseCase.create(input)
        }

        assertThat(exception.message)
            .isEqualTo("Email must not be blank")

        verifyCustomerRepositoryWasNotCalled()
    }

    @Test
    fun `should throw exception when email is invalid`() {

        val input = buildCreateCustomerInput(email = "email-invalido")

        val exception = assertThrows<IllegalArgumentException> {
            createCustomerUseCase.create(input)
        }

        assertThat(exception.message)
            .isEqualTo("Email must be valid")

        verifyCustomerRepositoryWasNotCalled()
    }

    @Test
    fun `should throw exception when income is negative`() {

        val input = buildCreateCustomerInput(income = BigDecimal("-1.00"))

        val exception = assertThrows<IllegalArgumentException> {
            createCustomerUseCase.create(input)
        }

        assertThat(exception.message)
            .isEqualTo("Income must not be negative")

        verify(exactly = 1) {
            customerRepositoryPort.existsByDocument(match { it.value == CUSTOMER_DOCUMENT })
            customerRepositoryPort.existsByEmail(match { it.value == CUSTOMER_EMAIL })
            customerRepositoryPort.existsByPhone(CUSTOMER_PHONE)
        }

        verify(exactly = 0) {
            customerRepositoryPort.save(any())
        }
    }

    @Test
    fun `should throw exception when name is blank`() {

        val input = buildCreateCustomerInput(name = "")

        val exception = assertThrows<IllegalArgumentException> {
            createCustomerUseCase.create(input)
        }

        assertThat(exception.message)
            .isEqualTo("Customer name must not be blank")

        verify(exactly = 1) {
            customerRepositoryPort.existsByDocument(match { it.value == CUSTOMER_DOCUMENT })
            customerRepositoryPort.existsByEmail(match { it.value == CUSTOMER_EMAIL })
            customerRepositoryPort.existsByPhone(CUSTOMER_PHONE)
        }

        verify(exactly = 0) {
            customerRepositoryPort.save(any())
        }
    }

    @Test
    fun `should create customer when phone is blank`() {

        val input = buildCreateCustomerInput(phone = "   ")

        val result = createCustomerUseCase.create(input)

        assertThat(result.phone).isEqualTo("")

        verify(exactly = 0) {
            customerRepositoryPort.existsByPhone(any())
        }

        verify(exactly = 1) {
            customerRepositoryPort.save(match { it.phone == "" })
        }
    }

    @Test
    fun `should throw exception when document has more than eleven digits`() {

        val input = buildCreateCustomerInput(document = "123456789001")

        val exception = assertThrows<IllegalArgumentException> {
            createCustomerUseCase.create(input)
        }

        assertThat(exception.message)
            .isEqualTo("Document must have 11 digits")

        verifyCustomerRepositoryWasNotCalled()
    }

    @Test
    fun `should throw exception when name contains only blank spaces`() {

        val input = buildCreateCustomerInput(name = "   ")

        val exception = assertThrows<IllegalArgumentException> {
            createCustomerUseCase.create(input)
        }

        assertThat(exception.message)
            .isEqualTo("Customer name must not be blank")

        verify(exactly = 1) {
            customerRepositoryPort.existsByDocument(match { it.value == CUSTOMER_DOCUMENT })
            customerRepositoryPort.existsByEmail(match { it.value == CUSTOMER_EMAIL })
            customerRepositoryPort.existsByPhone(CUSTOMER_PHONE)
        }

        verify(exactly = 0) {
            customerRepositoryPort.save(any())
        }
    }

    @Test
    fun `should create customer when income is zero`() {

        val input = buildCreateCustomerInput(income = BigDecimal.ZERO)

        val result = createCustomerUseCase.create(input)

        assertThat(result.income.value).isEqualByComparingTo(BigDecimal.ZERO)

        verify(exactly = 1) {
            customerRepositoryPort.save(
                match {
                    it.income.value.compareTo(BigDecimal.ZERO) == 0
                }
            )
        }
    }

    private fun verifyCustomerRepositoryWasNotCalled() {

        verify(exactly = 0) {
            customerRepositoryPort.existsByDocument(any())
            customerRepositoryPort.existsByEmail(any())
            customerRepositoryPort.existsByPhone(any())
            customerRepositoryPort.save(any())
        }
    }

    private fun mockCustomerDoesNotExist() {

        every { customerRepositoryPort.existsByDocument(match { it.value == CUSTOMER_DOCUMENT }) } returns false
        every { customerRepositoryPort.existsByEmail(match { it.value == CUSTOMER_EMAIL }) } returns false
        every { customerRepositoryPort.existsByPhone(CUSTOMER_PHONE) } returns false
    }

    private fun mockSaveCustomer() {
        every { customerRepositoryPort.save(any()) } answers { firstArg() }
    }

    private fun assertCustomerCreatedSuccessfully(result: Customer) {
        assertThat(result.customerId).isNotNull()
        assertThat(result.name).isEqualTo(CUSTOMER_NAME)
        assertThat(result.document.value).isEqualTo(CUSTOMER_DOCUMENT)
        assertThat(result.email.value).isEqualTo(CUSTOMER_EMAIL)
        assertThat(result.phone).isEqualTo(CUSTOMER_PHONE)
        assertThat(result.income.value).isEqualByComparingTo(CUSTOMER_INCOME)
        assertThat(result.status).isEqualTo(CustomerStatus.ACTIVE)
        assertThat(result.createdAt).isNotNull()
        assertThat(result.updatedAt).isNull()
    }
}