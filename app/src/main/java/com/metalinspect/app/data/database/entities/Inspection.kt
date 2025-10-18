package com.metalinspect.app.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Index
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Entity(
    tableName = "inspections",
    indices = [
        Index(value = ["lot_number"]),
        Index(value = ["status"]),
        Index(value = ["inspector_id"]),
        Index(value = ["created_at"]),
        Index(value = ["product_type_id"])
    ]
)
@Parcelize
data class Inspection(
    @PrimaryKey
    val id: String,
    
    @ColumnInfo(name = "lot_number")
    val lotNumber: String,
    
    @ColumnInfo(name = "container_number")
    val containerNumber: String? = null,
    
    @ColumnInfo(name = "product_type_id")
    val productTypeId: String,
    
    @ColumnInfo(name = "quantity")
    val quantity: Double,
    
    @ColumnInfo(name = "weight")
    val weight: Double,
    
    @ColumnInfo(name = "unit")
    val unit: String, // kg, tons, pieces
    
    @ColumnInfo(name = "port_location")
    val portLocation: String, // berth/warehouse
    
    @ColumnInfo(name = "weather_conditions")
    val weatherConditions: String,
    
    @ColumnInfo(name = "inspector_id")
    val inspectorId: String,
    
    @ColumnInfo(name = "status")
    val status: InspectionStatus,
    
    @ColumnInfo(name = "notes")
    val notes: String? = null,
    
    @ColumnInfo(name = "latitude")
    val latitude: Double? = null,
    
    @ColumnInfo(name = "longitude")
    val longitude: Double? = null,
    
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),
    
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis(),
    
    @ColumnInfo(name = "completed_at")
    val completedAt: Long? = null
) : Parcelable

enum class InspectionStatus {
    DRAFT,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED
}
