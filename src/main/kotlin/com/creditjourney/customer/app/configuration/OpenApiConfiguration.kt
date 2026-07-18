package com.creditjourney.customer.app.configuration

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfiguration {
    @Bean
    fun openAPI(): OpenAPI =
        OpenAPI()
            .info(
                Info()
                    .title("Credit Customer Service API")
                    .description("API responsável pelo cadastro, consulta e gerenciamento de clientes.")
                    .version("v1"),
            )
}
