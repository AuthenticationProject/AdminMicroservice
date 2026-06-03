package com.dilillo.adminMicroservice.interfaceAdaptersLayer.controller

import com.dilillo.adminMicroservice.businessLayer.adapter.AddProductRequest
import com.dilillo.adminMicroservice.businessLayer.boundaries.ShopBusinessLogic
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

    // Definisci la cartella di base (puoi anche metterla assoluta, es: "C:/my-app/uploads")
    private val rootFolder = Paths.get(System.getProperty("user.home"), "uploads")

    @GetMapping("/helloAdmin")
    fun hello(): ResponseEntity<String> {
        return ResponseEntity.ok("HELLO ADMIN :D ")
    }

    @PostMapping("/addProduct")
    fun addProduct(@RequestBody addProductRequest: AddProductRequest): ResponseEntity<Int> {
        val result = this.adminUseCase.addProduct(addProductRequest)
        return if (result.isSuccess)
            ResponseEntity.ok(result.getOrThrow())
        else
            ResponseEntity.internalServerError().build()
    }

    @DeleteMapping("/removeProduct/{productId}")
    fun removeProduct(@PathVariable productId: Int): ResponseEntity<Boolean> {
        val result = this.adminUseCase.removeProduct(productId)
        return if (result.isSuccess)
            ResponseEntity.ok(result.getOrThrow())
        else
            ResponseEntity.internalServerError().build()
    }

    @PostMapping("/loadImage")
    fun loadImage(
        @RequestParam("file") file: MultipartFile
    ): ResponseEntity<String> {
        if (!Files.exists(rootFolder)) {
            Files.createDirectories(rootFolder)
        }

        val nomeFileUnico = "${UUID.randomUUID()}_${file.originalFilename}"

        val percorsoDestinazione = rootFolder.resolve(nomeFileUnico)

        file.inputStream.use { inputStream ->
            Files.copy(inputStream, percorsoDestinazione, StandardCopyOption.REPLACE_EXISTING)
        }

        return ResponseEntity.ok("Image saved succesfully: ${percorsoDestinazione.toAbsolutePath()}")
    }

}