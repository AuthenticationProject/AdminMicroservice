package com.dilillo.adminMicroservice.businessLayer.adapter

data class AddProductRequest(
    val name: String,
    val description: String,
    val price: Double,
    val urlImage: String
)