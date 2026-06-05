package com.dilillo.adminMicroservice.interfaceAdaptersLayer.persistence.repository

import com.dilillo.adminMicroservice.interfaceAdaptersLayer.persistence.entity.ProductEntity
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Spring MySql repository for products
 */
interface ProductRepository: JpaRepository<ProductEntity, Integer> {
    fun removeById(id: Int): Result<Boolean>
}