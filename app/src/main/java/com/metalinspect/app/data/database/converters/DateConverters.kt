package com.metalinspect.app.data.database.converters

import androidx.room.TypeConverter
import com.metalinspect.app.data.database.entities.*

class DateConverters {
    
    @TypeConverter
    fun fromInspectionStatus(status: InspectionStatus): String {
        return status.name
    }
    
    @TypeConverter
    fun toInspectionStatus(status: String): InspectionStatus {
        return InspectionStatus.valueOf(status)
    }
    
    @TypeConverter
    fun fromDefectCategory(category: DefectCategory): String {
        return category.name
    }
    
    @TypeConverter
    fun toDefectCategory(category: String): DefectCategory {
        return DefectCategory.valueOf(category)
    }
    
    @TypeConverter
    fun fromDefectSeverity(severity: DefectSeverity): String {
        return severity.name
    }
    
    @TypeConverter
    fun toDefectSeverity(severity: String): DefectSeverity {
        return DefectSeverity.valueOf(severity)
    }
    
    @TypeConverter
    fun fromChecklistCategory(category: ChecklistCategory): String {
        return category.name
    }
    
    @TypeConverter
    fun toChecklistCategory(category: String): ChecklistCategory {
        return ChecklistCategory.valueOf(category)
    }
    
    @TypeConverter
    fun fromChecklistInputType(inputType: ChecklistInputType): String {
        return inputType.name
    }
    
    @TypeConverter
    fun toChecklistInputType(inputType: String): ChecklistInputType {
        return ChecklistInputType.valueOf(inputType)
    }
}