package com.metalinspect.app.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

/**
 * Room entity representing an inspection record
 * Maps to 'inspections' table in the database
 */
@Entity(
    tableName = "inspections"
)
@TypeConverters(DateConverter::class, StringListConverter::class)
data class InspectionEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    
    @ColumnInfo(name = "vessel_name")
    val vesselName: String,
    
    @ColumnInfo(name = "cargo_description")
    val cargoDescription: String,
    
    @ColumnInfo(name = "inspection_date")
    val inspectionDate: Date,
    
    @ColumnInfo(name = "inspector_name")
    val inspectorName: String,
    
    @ColumnInfo(name = "location")
    val location: String,
    
    @ColumnInfo(name = "weather_conditions")
    val weatherConditions: String,
    
    @ColumnInfo(name = "cargo_quantity")
    val cargoQuantity: Double,
    
    @ColumnInfo(name = "cargo_unit")
    val cargoUnit: String,
    
    @ColumnInfo(name = "inspection_type")
    val inspectionType: String, // "loading" or "discharge"
    
    @ColumnInfo(name = "quality_grade")
    val qualityGrade: String?,
    
    @ColumnInfo(name = "moisture_content")
    val moistureContent: Double?,
    
    @ColumnInfo(name = "contamination_level")
    val contaminationLevel: String?,
    
    @ColumnInfo(name = "overall_condition")
    val overallCondition: String,
    
    @ColumnInfo(name = "notes")
    val notes: String?,
    
    @ColumnInfo(name = "photo_paths")
    val photoPaths: List<String> = emptyList(),
    
    @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean = false,
    
    @ColumnInfo(name = "is_synced")
    val isSynced: Boolean = false,
    
    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date(),
    
    @ColumnInfo(name = "updated_at")
    val updatedAt: Date = Date()
)

/**
 * Type converter for Date objects
 */
class DateConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}

/**
 * Type converter for List<String> objects
 */
class StringListConverter {
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        return try {
            Gson().fromJson<List<String>>(
                value,
                object : TypeToken<List<String>>() {}.type
            ) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}