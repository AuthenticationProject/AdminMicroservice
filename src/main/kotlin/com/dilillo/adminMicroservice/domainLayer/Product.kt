package com.dilillo.adminMicroservice.domainLayer

data class Product(val name: String, val description: String, val price: Double, val urlImage: String, val id: Int = 0)