package com.metalinspect.app.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Index
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Entity(
    tableName = "checklist_items",
    indices = [Index(value = ["category", "is_active"])]
)
@Parcelize
data class ChecklistItem(
    @PrimaryKey
    val id: String,
    
    @ColumnInfo(name = "category")
    val category: ChecklistCategory,
    
    @ColumnInfo(name = "question")
    val question: String,
    
    @ColumnInfo(name = "input_type")
    val inputType: ChecklistInputType,
    
    @ColumnInfo(name = "options")
    val options: String? = null, // JSON array for radio/multi-select
    
    @ColumnInfo(name = "is_required")
    val isRequired: Boolean = false,
    
    @ColumnInfo(name = "sequence_order")
    val sequenceOrder: Int = 0,
    
    @ColumnInfo(name = "validation_rules")
    val validationRules: String? = null, // JSON object
    
    @ColumnInfo(name = "is_active")
    val isActive: Boolean = true
) : Parcelable

enum class ChecklistCategory {
    LOADING,
    UNLOADING,
    QUALITY_CONTROL,
    SAFETY_COMPLIANCE
}

enum class ChecklistInputType {
    TEXT,
    NUMBER,
    BOOLEAN,
    RADIO,
    MULTI_SELECT,
    DATE
}
