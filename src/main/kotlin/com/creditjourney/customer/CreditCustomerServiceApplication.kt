package com.creditjourney.customer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CreditCustomerServiceApplication

fun main(args: Array<String>) {
	runApplication<CreditCustomerServiceApplication>(*args)
}
