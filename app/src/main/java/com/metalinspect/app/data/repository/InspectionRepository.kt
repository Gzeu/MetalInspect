package com.metalinspect.app.data.repository

import kotlinx.coroutines.flow.Flow
import com.metalinspect.app.data.database.entities.Inspection
import com.metalinspect.app.data.database.entities.InspectionStatus
import com.metalinspect.app.domain.models.InspectionWithDetails
import com.metalinspect.app.domain.models.InspectionSummary

interface InspectionRepository {
    fun getAllInspections(): Flow<List<Inspection>>
    fun getInspectionsByStatus(status: InspectionStatus): Flow<List<Inspection>>
    fun getInspectionsByInspector(inspectorId: String): Flow<List<Inspection>>
    suspend fun getInspectionById(id: String): Inspection?
    suspend fun getInspectionWithDetails(id: String): InspectionWithDetails?
    fun searchInspections(query: String): Flow<List<Inspection>>
    fun getInspectionSummaries(): Flow<List<InspectionSummary>>
    
    suspend fun createInspection(inspection: Inspection): String
    suspend fun updateInspection(inspection: Inspection)
    suspend fun deleteInspection(inspection: Inspection)
    suspend fun updateInspectionStatus(id: String, status: InspectionStatus)
    
    suspend fun getInspectionCount(): Int
    suspend fun getInspectionCountByStatus(status: InspectionStatus): Int
}