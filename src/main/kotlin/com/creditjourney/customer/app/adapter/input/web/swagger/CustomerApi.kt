package com.creditjourney.customer.app.adapter.input.web.swagger

import com.creditjourney.customer.app.adapter.input.web.requests.CreateCustomerRequest
import com.creditjourney.customer.app.adapter.input.web.requests.UpdateCustomerRequest
import com.creditjourney.customer.app.adapter.input.web.responses.CustomerResponse
import com.creditjourney.customer.app.adapter.input.web.responses.CustomerSliceResponse
import com.creditjourney.customer.app.adapter.input.web.responses.ErrorResponse
import com.creditjourney.customer.core.domain.model.CustomerStatus
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import java.math.BigDecimal
import java.util.UUID

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

    @Operation(
        summary = "Consultar cliente por ID",
        description = "Consulta os dados cadastrais de um cliente pelo customerId."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Cliente encontrado",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = CustomerResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "400",
                description = "customerId inválido",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = ErrorResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Cliente não encontrado",
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
    fun findById(
        @Parameter(
            description = "Identificador único do cliente",
            example = "0416adad-f623-4622-a6ae-cabd86aab1ae"
        )
        @PathVariable customerId: UUID
    ): ResponseEntity<CustomerResponse>

    @Operation(
        summary = "Listar clientes",
        description = "Lista clientes cadastrados usando paginação performática sem count total."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Clientes listados com sucesso",
                content = [
                    Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = Schema(implementation = CustomerSliceResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Parâmetros de paginação inválidos",
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
    fun findAll(
        @Parameter(description = "Status do cliente", example = "ACTIVE")
        @RequestParam(required = false) status: CustomerStatus?,

        @Parameter(description = "Número da página", example = "0")
        @RequestParam(defaultValue = "0") page: Int,

        @Parameter(description = "Quantidade de registros por página", example = "30")
        @RequestParam(defaultValue = "30") size: Int,

        @Parameter(description = "Busca por documento, e-mail ou telefone", example = "fer@example.com")
        @RequestParam(required = false) search: String?,

        @Parameter(description = "Busca parcial por nome", example = "Ma")
        @RequestParam(required = false) name: String?,

        @Parameter(description = "Renda mínima", example = "500")
        @RequestParam(required = false) minIncome: BigDecimal?,

        @Parameter(description = "Renda máxima", example = "1000")
        @RequestParam(required = false) maxIncome: BigDecimal?

    ): ResponseEntity<CustomerSliceResponse>

    @Operation(
        summary = "Desabilitar cliente",
        description = "Altera o status do cliente para INACTIVE ou BLOCKED"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Status alterado com sucesso"),
            ApiResponse(responseCode = "400", description = "Status inválido"),
            ApiResponse(responseCode = "404", description = "Cliente não encontrado")
        ]
    )
    @DeleteMapping("/{customerId}")
    fun delete(
        @Parameter(description = "ID do cliente", example = "11111111-1111-1111-1111-111111111111")
        @PathVariable customerId: UUID,

        @Parameter(description = "Novo status do cliente", example = "INACTIVE")
        @RequestParam(defaultValue = "INACTIVE") status: CustomerStatus
    ): ResponseEntity<Void>

    @Operation(
        summary = "Editar cliente",
        description = "Atualiza parcialmente os dados editáveis de um cliente"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso"),
            ApiResponse(responseCode = "400", description = "Requisição inválida"),
            ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            ApiResponse(responseCode = "409", description = "E-mail ou telefone já cadastrado")
        ]
    )
    @PatchMapping("/{customerId}")
    fun update(
        @Parameter(description = "ID do cliente", example = "11111111-1111-1111-1111-111111111111")
        @PathVariable customerId: UUID,

        @RequestBody request: UpdateCustomerRequest
    ): ResponseEntity<CustomerResponse>
}