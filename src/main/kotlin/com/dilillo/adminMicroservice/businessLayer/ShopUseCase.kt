package com.dilillo.adminMicroservice.businessLayer

import com.dilillo.adminMicroservice.businessLayer.adapter.AddProductRequest
import com.dilillo.adminMicroservice.businessLayer.boundaries.ShopBusinessLogic
import com.dilillo.adminMicroservice.businessLayer.boundaries.ProductRepositoryGateway
import com.dilillo.adminMicroservice.domainLayer.Product

class ShopUseCase(
    private val productRepository: ProductRepositoryGateway,
): ShopBusinessLogic {

    override fun addProduct(request: AddProductRequest): Result<Int> {
        return this.productRepository.addProduct(
            Product(
                request.name,
                request.description,
                request.price,
                request.urlImage
            )
        )
    }

    override fun removeProduct(productId: Int): Result<Boolean> {
        return this.productRepository.removeProduct(productId)
    }

    override fun getAllProducts(): List<Product> {
        return this.productRepository.getAllProduct()
    }

}