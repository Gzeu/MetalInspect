package com.metalinspect.app.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Index
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Entity(
    tableName = "product_types",
    indices = [Index(value = ["name"], unique = true)]
)
@Parcelize
data class ProductType(
    @PrimaryKey
    val id: String,
    
    @ColumnInfo(name = "name")
    val name: String,
    
    @ColumnInfo(name = "description")
    val description: String,
    
    @ColumnInfo(name = "standard_dimensions")
    val standardDimensions: String? = null,
    
    @ColumnInfo(name = "standard_grades")
    val standardGrades: String? = null, // JSON array
    
    @ColumnInfo(name = "is_active")
    val isActive: Boolean = true,
    
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
) : Parcelable