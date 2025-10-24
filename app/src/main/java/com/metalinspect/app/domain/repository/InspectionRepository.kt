package com.metalinspect.app.domain.repository

import com.metalinspect.app.domain.model.Defect
import com.metalinspect.app.domain.model.Inspection
import kotlinx.coroutines.flow.Flow
import java.util.Date

/**
 * Repository contract for inspection-related operations
 */
interface InspectionRepository {
    // Create
    suspend fun createInspection(inspection: Inspection): Result<String>
    suspend fun createDefect(defect: Defect): Result<String>
    suspend fun createDefects(defects: List<Defect>): Result<List<String>>

    // Read
    suspend fun getInspectionById(id: String): Result<Inspection?>
    fun getInspectionByIdFlow(id: String): Flow<Inspection?>
    fun getAllInspections(): Flow<List<Inspection>>
    fun getInspectionsByStatus(isCompleted: Boolean): Flow<List<Inspection>>
    fun getInspectionsByType(type: String): Flow<List<Inspection>>
    fun getInspectionsByVessel(vesselName: String): Flow<List<Inspection>>
    fun getInspectionsByDateRange(startDate: Date, endDate: Date): Flow<List<Inspection>>
    fun getInspectionsByInspector(inspectorName: String): Flow<List<Inspection>>
    fun searchInspections(query: String): Flow<List<Inspection>>
    suspend fun getInspectionWithDefects(id: String): Result<Pair<Inspection, List<Defect>>?>
    fun getAllInspectionsWithDefects(): Flow<List<Pair<Inspection, List<Defect>>>>

    // Defects
    suspend fun getDefectById(id: String): Result<Defect?>
    suspend fun getDefectsByInspectionId(inspectionId: String): Result<List<Defect>>
    fun getDefectsByInspectionIdFlow(inspectionId: String): Flow<List<Defect>>
    fun getAllDefects(): Flow<List<Defect>>
    fun getDefectsByType(type: String): Flow<List<Defect>>
    fun getDefectsBySeverity(severity: String): Flow<List<Defect>>
    fun getCriticalDefects(): Flow<List<Defect>>

    // Update
    suspend fun updateInspection(inspection: Inspection): Result<Unit>
    suspend fun updateDefect(defect: Defect): Result<Unit>
    suspend fun updateInspectionStatus(id: String, isCompleted: Boolean): Result<Unit>
    suspend fun updateSyncStatus(ids: List<String>, isSynced: Boolean): Result<Unit>

    // Delete
    suspend fun deleteInspection(id: String): Result<Unit>
    suspend fun deleteDefect(id: String): Result<Unit>
    suspend fun deleteDefectsByInspectionId(inspectionId: String): Result<Unit>

    // Statistics
    suspend fun getInspectionStatistics(): Result<Map<String, Any>>
    suspend fun getDefectStatistics(): Result<Map<String, Any>>

    // Sync
    suspend fun getPendingSyncInspections(): Result<List<Inspection>>
}
