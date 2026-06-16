package com.creditjourney.customer.app.adapter.input.web.swagger

import com.creditjourney.customer.app.adapter.input.web.requests.CreateCustomerRequest
import com.creditjourney.customer.app.adapter.input.web.responses.CustomerResponse
import com.creditjourney.customer.app.adapter.input.web.responses.ErrorResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody

@Tag(
    name = "Customers",
    description = "Endpoints para cadastro e gerenciamento de clientes"
)
interface CustomerApi {

    @Operation(
        summary = "Criar cliente",
        description = "Cadastra um novo cliente na plataforma de jornada de crédito."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Cliente criado com sucesso",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = CustomerResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Requisição inválida",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ErrorResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "409",
                description = "Cliente já cadastrado",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ErrorResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Erro inesperado",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ErrorResponse::class)
                    )
                ]
            )
        ]
    )
    fun create(
        @Valid @RequestBody request: CreateCustomerRequest
    ): ResponseEntity<CustomerResponse>
}