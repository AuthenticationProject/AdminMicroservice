package com.dilillo.adminMicroservice.interfaceAdaptersLayer.controller

import com.dilillo.adminMicroservice.businessLayer.adapter.ChangePasswordRequest
import com.dilillo.adminMicroservice.businessLayer.adapter.ChangeUsernameRequest
import com.dilillo.adminMicroservice.businessLayer.adapter.ImageRequest
import com.dilillo.adminMicroservice.businessLayer.boundaries.ShopBusinessLogic
import com.dilillo.adminMicroservice.businessLayer.boundaries.BusinessLogic
import com.dilillo.adminMicroservice.domainLayer.Product
import com.dilillo.adminMicroservice.interfaceAdaptersLayer.notifications.EmailNotificationService
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

    @PostMapping("/changeUsername")
    fun changeUsername(@RequestBody request: ChangeUsernameRequest, @AuthenticationPrincipal emailDalToken: UserDetails): ResponseEntity<String> {
        println("L'utente che sta facendo la richiesta è: ${emailDalToken.username}")
        if(this.businessLogic.changeUsername(request.email, request.newUsername))
            //TODO Add email notification
            return ResponseEntity.ok("Username changed: " + request.newUsername)
        else
            return ResponseEntity.internalServerError().build()
    }

    @PostMapping("/changePassword")
    fun changePassword(@RequestBody request: ChangePasswordRequest, @AuthenticationPrincipal emailDalToken: UserDetails): ResponseEntity<String> {
        if(request.email != emailDalToken.username)
            return ResponseEntity.internalServerError().build() //TODO mettere errore not authorized 401
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

    @GetMapping("/getProducts")
    fun getProducts(): ResponseEntity<List<Product>> {
        return ResponseEntity.ok(this.productLogic.getAllProducts())
    }

    @GetMapping("/getImage/{imageUrl}")
    fun getImage(@PathVariable imageUrl: String): ResponseEntity<Resource> {
        try {
            val percorsoFile = rootFolder.resolve(imageUrl)

            if (!Files.exists(percorsoFile)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
            }

            val risorsa: Resource = FileSystemResource(percorsoFile)

            val contentType = Files.probeContentType(percorsoFile) ?: "application/octet-stream"

            return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"${risorsa.filename}\"")
                .body(risorsa)

        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }
}