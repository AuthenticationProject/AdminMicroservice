package com.dilillo.adminMicroservice.businessLayer.adapter

/**
 * For new product request
 */
data class AddProductRequest(
    val name: String,
    val description: String,
    val price: Double,
    val urlImage: String
)