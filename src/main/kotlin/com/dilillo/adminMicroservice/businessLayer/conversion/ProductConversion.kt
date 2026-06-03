package com.dilillo.adminMicroservice.businessLayer.conversion

import com.dilillo.adminMicroservice.domainLayer.Product
import com.dilillo.adminMicroservice.interfaceAdaptersLayer.persistence.entity.ProductEntity

fun ProductEntity.toProduct(): Product {
    return Product(
        this.name!!,
        this.description!!,
        this.price!!,
        this.urlImage!!
    )
}

fun Product.toEntity(): ProductEntity {
    val product = ProductEntity()
    product.build(
        this.name,
        this.description,
        this.price,
        this.urlImage
    )
    return product
}