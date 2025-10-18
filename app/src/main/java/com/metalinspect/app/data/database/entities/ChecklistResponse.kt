package com.metalinspect.app.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Index
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Entity(
    tableName = "checklist_responses",
    indices = [
        Index(value = ["inspection_id"]),
        Index(value = ["checklist_item_id"])
    ]
)
@Parcelize
data class ChecklistResponse(
    @PrimaryKey
    val id: String,
    
    @ColumnInfo(name = "inspection_id")
    val inspectionId: String,
    
    @ColumnInfo(name = "checklist_item_id")
    val checklistItemId: String,
    
    @ColumnInfo(name = "response_value")
    val responseValue: String,
    
    @ColumnInfo(name = "response_notes")
    val responseNotes: String? = null,
    
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
) : Parcelable
