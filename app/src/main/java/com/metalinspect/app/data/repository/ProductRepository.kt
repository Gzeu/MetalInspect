package com.metalinspect.app.data.repository

import kotlinx.coroutines.flow.Flow
import com.metalinspect.app.data.database.entities.ProductType

interface ProductRepository {
    fun getActiveProductTypes(): Flow<List<ProductType>>
    fun getAllProductTypes(): Flow<List<ProductType>>
    suspend fun getProductTypeById(id: String): ProductType?
    suspend fun getProductTypeByName(name: String): ProductType?
    
    suspend fun createProductType(productType: ProductType): String
    suspend fun updateProductType(productType: ProductType)
    suspend fun deleteProductType(productType: ProductType)
    suspend fun updateProductTypeStatus(id: String, isActive: Boolean)
    
    suspend fun getActiveProductTypeCount(): Int
}