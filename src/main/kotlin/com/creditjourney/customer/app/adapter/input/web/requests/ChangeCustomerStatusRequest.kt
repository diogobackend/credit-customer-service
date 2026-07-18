package com.creditjourney.customer.app.adapter.input.web.requests

import com.creditjourney.customer.core.domain.model.CustomerStatus

data class ChangeCustomerStatusRequest(
    val status: CustomerStatus,
)
