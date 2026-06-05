package com.dilillo.adminMicroservice.interfaceAdaptersLayer.persistence.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

/**
 * Product entity for db mapping of products
 */
@Entity
class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int? = null

    var name: String? = null

    var description: String? = null

    var price: Double? = null

    var urlImage: String? = null

    fun build(name: String, description: String, price: Double, urlImage: String) {
        this.name = name
        this.description = description
        this.price = price
        this.urlImage = urlImage
    }
}