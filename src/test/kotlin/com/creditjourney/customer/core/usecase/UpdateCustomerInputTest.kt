package com.creditjourney.customer.core.usecase

import com.creditjourney.customer.core.port.input.UpdateCustomerInput
import com.creditjourney.customer.core.usecase.builder.CustomerBuilderConstants.CUSTOMER_ID
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal

class UpdateCustomerInputTest {

    @Test
    fun `should create input with only name`() {

        val input = UpdateCustomerInput(
            customerId = CUSTOMER_ID,
            name = UPDATED_NAME
        )

        assertThat(input.customerId).isEqualTo(CUSTOMER_ID)
        assertThat(input.name).isEqualTo(UPDATED_NAME)
        assertThat(input.email).isNull()
        assertThat(input.phone).isNull()
        assertThat(input.income).isNull()
    }

    @Test
    fun `should create input with only email`() {

        val input = UpdateCustomerInput(
            customerId = CUSTOMER_ID,
            email = UPDATED_EMAIL
        )

        assertThat(input.email).isEqualTo(UPDATED_EMAIL)
    }

    @Test
    fun `should create input with only phone`() {

        val input = UpdateCustomerInput(
            customerId = CUSTOMER_ID,
            phone = UPDATED_PHONE
        )

        assertThat(input.phone).isEqualTo(UPDATED_PHONE)
    }

    @Test
    fun `should create input with only income`() {

        val input = UpdateCustomerInput(
            customerId = CUSTOMER_ID,
            income = UPDATED_INCOME
        )

        assertThat(input.income).isEqualByComparingTo(UPDATED_INCOME)
    }

    @Test
    fun `should throw exception when no field is informed using default values`() {

        val exception = assertThrows<IllegalArgumentException> {
            UpdateCustomerInput(
                customerId = CUSTOMER_ID
            )
        }

        assertThat(exception.message).isEqualTo(AT_LEAST_ONE_FIELD_ERROR)
    }

    @Test
    fun `should throw exception when phone is blank`() {

        val exception = assertThrows<IllegalArgumentException> {
            UpdateCustomerInput(
                customerId = CUSTOMER_ID,
                phone = BLANK_PHONE
            )
        }

        assertThat(exception.message).isEqualTo(PHONE_BLANK_ERROR)
    }

    companion object {
        const val UPDATED_NAME = "Maria Souza"
        const val UPDATED_EMAIL = "maria@email.com"
        const val UPDATED_PHONE = "11999999999"
        const val BLANK_PHONE = " "
        const val AT_LEAST_ONE_FIELD_ERROR = "At least one field must be informed"
        const val PHONE_BLANK_ERROR = "Phone must not be blank"

        val UPDATED_INCOME: BigDecimal = BigDecimal("2500.00")
    }
}