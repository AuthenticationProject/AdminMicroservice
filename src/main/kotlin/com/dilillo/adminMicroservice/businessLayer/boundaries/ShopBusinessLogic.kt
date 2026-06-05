package com.dilillo.adminMicroservice.businessLayer.boundaries

import com.dilillo.adminMicroservice.businessLayer.adapter.AddProductRequest
import com.dilillo.adminMicroservice.domainLayer.Product

/**
 * Shop business logic for use cases related to the shop
 */
interface ShopBusinessLogic {

    fun addProduct(request: AddProductRequest): Result<Int>

    fun removeProduct(productId: Int): Result<Boolean>

    fun getAllProducts(): List<Product>

}