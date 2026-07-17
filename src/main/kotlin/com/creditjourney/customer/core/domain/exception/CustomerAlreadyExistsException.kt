package com.creditjourney.customer.core.domain.exception

import com.creditjourney.customer.core.common.messages.CustomerMessages.CUSTOMER_ALREADY_EXISTS_WITH

class CustomerAlreadyExistsException(
    field: String,
    value: String
) : RuntimeException("$CUSTOMER_ALREADY_EXISTS_WITH $field: $value")