package com.dilillo.adminMicroservice.interfaceAdaptersLayer.controller

import com.dilillo.adminMicroservice.businessLayer.adapter.ChangePasswordRequest
import com.dilillo.adminMicroservice.businessLayer.adapter.ChangeUsernameRequest
import com.dilillo.adminMicroservice.businessLayer.boundaries.ShopBusinessLogic
import com.dilillo.adminMicroservice.businessLayer.boundaries.BusinessLogic
import com.dilillo.adminMicroservice.domainLayer.Product
import com.dilillo.adminMicroservice.interfaceAdaptersLayer.notifications.EmailNotificationService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.nio.file.Files
import java.nio.file.Paths

@RestController
@RequestMapping("/api/user")
class UserController(
    private val businessLogic: BusinessLogic,
    private val productLogic: ShopBusinessLogic,
    private val emailNotificationService: EmailNotificationService
) {

    private val rootFolder = Paths.get(System.getProperty("user.home"), "uploads")

    @Operation(
        summary = "Username change",
        description = "User performs a username change request",
        requestBody = io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = [
                Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ChangeUsernameRequest::class)
                )
            ],
            required = true
        ),
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Username changed succesfully",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = String::class)
                    )
                ]
            ), ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = [Content()]
            )
        ]
    )
    @PostMapping("/changeUsername")
    fun changeUsername(@RequestBody request: ChangeUsernameRequest, @AuthenticationPrincipal emailDalToken: UserDetails): ResponseEntity<String> {
        println("L'utente che sta facendo la richiesta è: ${emailDalToken.username}")
        if(this.businessLogic.changeUsername(request.email, request.newUsername))
            return ResponseEntity.ok("Username changed: " + request.newUsername)
        else
            return ResponseEntity.internalServerError().build()
    }

    @Operation(
        summary = "Password change",
        description = "User performs a password change request",
        requestBody = io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = [
                Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ChangePasswordRequest::class)
                )
            ],
            required = true
        ),
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Password changed succesfully",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = String::class)
                    )
                ]
            ), ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = [Content()]
            )
        ]
    )
    @PostMapping("/changePassword")
    fun changePassword(@RequestBody request: ChangePasswordRequest, @AuthenticationPrincipal emailDalToken: UserDetails): ResponseEntity<String> {
        if(request.email != emailDalToken.username)
            return ResponseEntity.internalServerError().build()
        if(this.businessLogic.changePassword(request.email, request.newPassword)) {
            emailNotificationService.sendSimpleEmail(
                request.email,
                "Password changed",
                "Your password has been changed correctly, if it weren't you contact us.",
            )
            return ResponseEntity.ok("Password changed")
        }
        else
            return ResponseEntity.internalServerError().build()
    }

    @Operation(
        summary = "Get of all products",
        description = "User wants to retrieve all the products",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "List of products retrieved succesfully",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = Product::class)
                    )
                ]
            ), ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = [Content()]
            )
        ]
    )
    @GetMapping("/getProducts")
    fun getProducts(): ResponseEntity<List<Product>> {
        return ResponseEntity.ok(this.productLogic.getAllProducts())
    }

    @Operation(
        summary = "Get a specific image for a product",
        description = "User wants to retrieve the image of a specific product",
        parameters = [
            Parameter(
                name = "imageUrl",
                description = "Image url requested"
            )
        ],
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Image retrieved succesfully",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = Product::class)
                    )
                ]
            ), ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = [Content()]
            )
        ]
    )
    @GetMapping("/getImage/{imageUrl}")
    fun getImage(@PathVariable imageUrl: String): ResponseEntity<Resource> {
        try {
            val filePath = rootFolder.resolve(imageUrl)

            if (!Files.exists(filePath)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
            }

            val resource: Resource = FileSystemResource(filePath)

            val contentType = Files.probeContentType(filePath) ?: "application/octet-stream"

            return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"${resource.filename}\"")
                .body(resource)

        } catch (e: Exception) {
            return ResponseEntity.internalServerError().build()
        }
    }
}