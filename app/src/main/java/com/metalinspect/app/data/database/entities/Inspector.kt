package com.metalinspect.app.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Index
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Entity(
    tableName = "inspectors",
    indices = [Index(value = ["company", "name"])]
)
@Parcelize
data class Inspector(
    @PrimaryKey
    val id: String,
    
    @ColumnInfo(name = "name")
    val name: String,
    
    @ColumnInfo(name = "company")
    val company: String,
    
    @ColumnInfo(name = "role")
    val role: String,
    
    @ColumnInfo(name = "signature_image_path")
    val signatureImagePath: String? = null,
    
    @ColumnInfo(name = "is_active")
    val isActive: Boolean = false,
    
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),
    
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis()
) : Parcelable