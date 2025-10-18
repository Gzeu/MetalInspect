package com.metalinspect.app.data.repository

import kotlinx.coroutines.flow.Flow
import com.metalinspect.app.data.database.dao.ProductTypeDao
import com.metalinspect.app.data.database.entities.ProductType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepositoryImpl @Inject constructor(
    private val productTypeDao: ProductTypeDao
) : ProductRepository {
    
    override fun getActiveProductTypes(): Flow<List<ProductType>> {
        return productTypeDao.getActiveProductTypes()
    }
    
    override fun getAllProductTypes(): Flow<List<ProductType>> {
        return productTypeDao.getAllProductTypes()
    }
    
    override suspend fun getProductTypeById(id: String): ProductType? {
        return productTypeDao.getProductTypeById(id)
    }
    
    override suspend fun getProductTypeByName(name: String): ProductType? {
        return productTypeDao.getProductTypeByName(name)
    }
    
    override suspend fun createProductType(productType: ProductType): String {
        productTypeDao.insertProductType(productType)
        return productType.id
    }
    
    override suspend fun updateProductType(productType: ProductType) {
        productTypeDao.updateProductType(productType)
    }
    
    override suspend fun deleteProductType(productType: ProductType) {
        productTypeDao.deleteProductType(productType)
    }
    
    override suspend fun updateProductTypeStatus(id: String, isActive: Boolean) {
        productTypeDao.updateProductTypeStatus(id, isActive)
    }
    
    override suspend fun getActiveProductTypeCount(): Int {
        return productTypeDao.getActiveProductTypeCount()
    }
}