package com.creditjourney.customer.core.port.input

import com.creditjourney.customer.core.common.messages.CustomerMessages.AT_LEAST_ONE_FIELD_MUST_BE_INFORMED
import com.creditjourney.customer.core.common.messages.CustomerMessages.BLANK_PHONE
import com.creditjourney.customer.core.common.messages.CustomerMessages.CUSTOMER_EMAIL_UPDATED
import com.creditjourney.customer.core.common.messages.CustomerMessages.CUSTOMER_ID
import com.creditjourney.customer.core.common.messages.CustomerMessages.CUSTOMER_INCOME_UPDATED
import com.creditjourney.customer.core.common.messages.CustomerMessages.CUSTOMER_NAME_UPDATED
import com.creditjourney.customer.core.common.messages.CustomerMessages.CUSTOMER_PHONE_UPDATED
import com.creditjourney.customer.core.common.messages.CustomerMessages.PHONE_MUST_NOT_BE_BLANK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class UpdateCustomerInputTest {
    @Test
    fun `should create input with only name`() {
        val input =
            UpdateCustomerInput(
                customerId = CUSTOMER_ID,
                name = CUSTOMER_NAME_UPDATED,
            )

        assertThat(input.customerId).isEqualTo(CUSTOMER_ID)
        assertThat(input.name).isEqualTo(CUSTOMER_NAME_UPDATED)
        assertThat(input.email).isNull()
        assertThat(input.phone).isNull()
        assertThat(input.income).isNull()
    }

    @Test
    fun `should create input with only email`() {
        val input =
            UpdateCustomerInput(
                customerId = CUSTOMER_ID,
                email = CUSTOMER_EMAIL_UPDATED,
            )

        assertThat(input.email).isEqualTo(CUSTOMER_EMAIL_UPDATED)
    }

    @Test
    fun `should create input with only phone`() {
        val input =
            UpdateCustomerInput(
                customerId = CUSTOMER_ID,
                phone = CUSTOMER_PHONE_UPDATED,
            )

        assertThat(input.phone).isEqualTo(CUSTOMER_PHONE_UPDATED)
    }

    @Test
    fun `should create input with only income`() {
        val input =
            UpdateCustomerInput(
                customerId = CUSTOMER_ID,
                income = CUSTOMER_INCOME_UPDATED,
            )

        assertThat(input.income).isEqualByComparingTo(CUSTOMER_INCOME_UPDATED)
    }

    @Test
    fun `should throw exception when no field is informed using default values`() {
        val exception =
            assertThrows<IllegalArgumentException> {
                UpdateCustomerInput(
                    customerId = CUSTOMER_ID,
                )
            }

        assertThat(exception.message).isEqualTo(AT_LEAST_ONE_FIELD_MUST_BE_INFORMED)
    }

    @Test
    fun `should throw exception when phone is blank`() {
        val exception =
            assertThrows<IllegalArgumentException> {
                UpdateCustomerInput(
                    customerId = CUSTOMER_ID,
                    phone = BLANK_PHONE,
                )
            }

        assertThat(exception.message).isEqualTo(PHONE_MUST_NOT_BE_BLANK)
    }
}
