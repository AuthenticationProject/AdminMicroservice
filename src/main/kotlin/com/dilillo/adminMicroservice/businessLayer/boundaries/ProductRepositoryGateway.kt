package com.dilillo.adminMicroservice.businessLayer.boundaries

import com.dilillo.adminMicroservice.domainLayer.Product

/**
 * Product repository gateway interface
 */
interface ProductRepositoryGateway {

    fun getAllProduct(): List<Product>

    fun addProduct(product: Product): Result<Int>

    fun removeProduct(id: Int): Result<Boolean>

}