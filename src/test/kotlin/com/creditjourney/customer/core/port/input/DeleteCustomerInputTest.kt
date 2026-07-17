package com.creditjourney.customer.core.port.input
import com.creditjourney.customer.core.domain.model.CustomerStatus
import com.creditjourney.customer.core.common.messages.CustomerMessages.CUSTOMER_ID
import com.creditjourney.customer.core.common.messages.CustomerMessages.DELETE_STATUS_MUST_BE_INACTIVE_OR_BLOCKED
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DeleteCustomerInputTest {

    @Test
    fun `should create input with default status inactive`() {

        val input = DeleteCustomerInput(
            customerId = CUSTOMER_ID
        )

        assertThat(input.customerId).isEqualTo(CUSTOMER_ID)
        assertThat(input.status).isEqualTo(CustomerStatus.INACTIVE)
    }

    @Test
    fun `should create input with blocked status`() {

        val input = DeleteCustomerInput(
            customerId = CUSTOMER_ID,
            status = CustomerStatus.BLOCKED
        )

        assertThat(input.customerId).isEqualTo(CUSTOMER_ID)
        assertThat(input.status).isEqualTo(CustomerStatus.BLOCKED)
    }

    @Test
    fun `should throw exception when status is active`() {

        val exception = assertThrows<IllegalArgumentException> {
            DeleteCustomerInput(
                customerId = CUSTOMER_ID,
                status = CustomerStatus.ACTIVE
            )
        }

        assertThat(exception.message).isEqualTo(DELETE_STATUS_MUST_BE_INACTIVE_OR_BLOCKED)
    }
}