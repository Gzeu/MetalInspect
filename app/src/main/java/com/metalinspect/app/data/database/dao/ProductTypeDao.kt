package com.metalinspect.app.data.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.metalinspect.app.data.database.entities.ProductType

@Dao
interface ProductTypeDao {
    
    @Query("SELECT * FROM product_types WHERE is_active = 1 ORDER BY name ASC")
    fun getActiveProductTypes(): Flow<List<ProductType>>
    
    @Query("SELECT * FROM product_types ORDER BY name ASC")
    fun getAllProductTypes(): Flow<List<ProductType>>
    
    @Query("SELECT * FROM product_types WHERE id = :id")
    suspend fun getProductTypeById(id: String): ProductType?
    
    @Query("SELECT * FROM product_types WHERE name = :name LIMIT 1")
    suspend fun getProductTypeByName(name: String): ProductType?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductType(productType: ProductType)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductTypes(productTypes: List<ProductType>)
    
    @Update
    suspend fun updateProductType(productType: ProductType)
    
    @Delete
    suspend fun deleteProductType(productType: ProductType)
    
    @Query("UPDATE product_types SET is_active = :isActive WHERE id = :id")
    suspend fun updateProductTypeStatus(id: String, isActive: Boolean)
    
    @Query("SELECT COUNT(*) FROM product_types WHERE is_active = 1")
    suspend fun getActiveProductTypeCount(): Int
    
    @Query("SELECT COUNT(*) FROM product_types")
    suspend fun getProductTypeCount(): Int
}