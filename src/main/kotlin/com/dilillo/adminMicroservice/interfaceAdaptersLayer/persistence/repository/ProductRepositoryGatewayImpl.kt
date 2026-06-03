package com.dilillo.adminMicroservice.interfaceAdaptersLayer.persistence.repository

import com.dilillo.adminMicroservice.businessLayer.boundaries.ProductRepositoryGateway
import com.dilillo.adminMicroservice.businessLayer.conversion.toEntity
import com.dilillo.adminMicroservice.businessLayer.conversion.toProduct
import com.dilillo.adminMicroservice.domainLayer.Product

class ProductRepositoryGatewayImpl(
    private val productRepository: ProductRepository
): ProductRepositoryGateway {
    override fun getAllProduct(): List<Product> {
        return this.productRepository.findAll().map { it.toProduct() }
    }

    override fun addProduct(product: Product): Result<Int> {
        val result = this.productRepository.save(
            product.toEntity()
        )
        return Result.success(result.id!!)
    }

    override fun removeProduct(id: Int): Result<Boolean> {
        return this.productRepository.removeById(id)
    }
}