package com.dilillo.adminMicroservice.interfaceAdaptersLayer.controller

import com.dilillo.adminMicroservice.businessLayer.adapter.AddProductRequest
import com.dilillo.adminMicroservice.businessLayer.boundaries.ShopBusinessLogic
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.UUID

@CrossOrigin
@RestController
@RequestMapping("/api/admin")
class ManagementController(
    private val adminUseCase: ShopBusinessLogic
) {

    private val rootFolder = Paths.get(System.getProperty("user.home"), "uploads")

    @GetMapping("/helloAdmin")
    fun hello(): ResponseEntity<String> {
        return ResponseEntity.ok("HELLO ADMIN :D ")
    }

    @Operation(
        summary = "Add product request",
        description = "Admin requests the insertion of a new product",
        requestBody = io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = [
                Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = AddProductRequest::class)
                )
            ],
            required = true
        ),
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Operation done succesfully",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = Int::class)
                    )
                ]
            ), ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = [Content()]
            )
        ]
    )
    @PostMapping("/addProduct")
    fun addProduct(@RequestBody addProductRequest: AddProductRequest): ResponseEntity<Int> {
        val result = this.adminUseCase.addProduct(addProductRequest)
        return if (result.isSuccess)
            ResponseEntity.ok(result.getOrThrow())
        else
            ResponseEntity.internalServerError().build()
    }

    @Operation(
        summary = "Product deletion request",
        description = "Admin performs a product deletion request",
        parameters = [
            Parameter(
                name = "productId",
                description = "Licence id to be updated",
                `in` = ParameterIn.PATH
            )
        ],
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Deleted succesfully",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = Boolean::class)
                    )
                ]
            ), ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = [Content()]
            )
        ]
    )
    @DeleteMapping("/removeProduct/{productId}")
    fun removeProduct(@PathVariable productId: Int): ResponseEntity<Boolean> {
        val result = this.adminUseCase.removeProduct(productId)
        return if (result.isSuccess)
            ResponseEntity.ok(result.getOrThrow())
        else
            ResponseEntity.internalServerError().build()
    }

    @Operation(
        summary = "Load image request for a new product",
        description = "Admin requests the insertion of a image for a new product",
        parameters = [
            Parameter(
                name = "file",
                description = "Image file uploaded"
            )
        ],
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Operation done succesfully",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = Int::class)
                    )
                ]
            ), ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = [Content()]
            )
        ]
    )
    @PostMapping("/loadImage")
    fun loadImage(
        @RequestParam("file") file: MultipartFile
    ): ResponseEntity<String> {
        if (!Files.exists(rootFolder)) {
            Files.createDirectories(rootFolder)
        }

        val uri = "${UUID.randomUUID()}_${file.originalFilename}"

        val destinationPath = rootFolder.resolve(uri)

        file.inputStream.use { inputStream ->
            Files.copy(inputStream, destinationPath, StandardCopyOption.REPLACE_EXISTING)
        }

        return ResponseEntity.ok(uri)
    }

}