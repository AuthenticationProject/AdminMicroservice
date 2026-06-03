package com.dilillo.adminMicroservice.interfaceAdaptersLayer.persistence.repository

import com.dilillo.adminMicroservice.interfaceAdaptersLayer.persistence.entity.ProductEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository: JpaRepository<ProductEntity, Integer> {
    fun removeById(id: Int): Result<Boolean>
}