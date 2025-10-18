package com.metalinspect.app.domain.usecases.inspection

import kotlinx.coroutines.flow.Flow
import com.metalinspect.app.data.database.entities.Inspection
import com.metalinspect.app.data.database.entities.InspectionStatus
import com.metalinspect.app.data.repository.InspectionRepository
import com.metalinspect.app.domain.models.InspectionWithDetails
import com.metalinspect.app.domain.models.InspectionSummary
import javax.inject.Inject

interface GetInspectionUseCase {
    fun getAllInspections(): Flow<List<Inspection>>
    fun getInspectionsByStatus(status: InspectionStatus): Flow<List<Inspection>>
    fun getInspectionsByInspector(inspectorId: String): Flow<List<Inspection>>
    suspend fun getInspectionById(id: String): Inspection?
    suspend fun getInspectionWithDetails(id: String): InspectionWithDetails?
    fun getInspectionSummaries(): Flow<List<InspectionSummary>>
    fun searchInspections(query: String): Flow<List<Inspection>>
}

class GetInspectionUseCaseImpl @Inject constructor(
    private val inspectionRepository: InspectionRepository
) : GetInspectionUseCase {
    
    override fun getAllInspections(): Flow<List<Inspection>> {
        return inspectionRepository.getAllInspections()
    }
    
    override fun getInspectionsByStatus(status: InspectionStatus): Flow<List<Inspection>> {
        return inspectionRepository.getInspectionsByStatus(status)
    }
    
    override fun getInspectionsByInspector(inspectorId: String): Flow<List<Inspection>> {
        return inspectionRepository.getInspectionsByInspector(inspectorId)
    }
    
    override suspend fun getInspectionById(id: String): Inspection? {
        return inspectionRepository.getInspectionById(id)
    }
    
    override suspend fun getInspectionWithDetails(id: String): InspectionWithDetails? {
        return inspectionRepository.getInspectionWithDetails(id)
    }
    
    override fun getInspectionSummaries(): Flow<List<InspectionSummary>> {
        return inspectionRepository.getInspectionSummaries()
    }
    
    override fun searchInspections(query: String): Flow<List<Inspection>> {
        return inspectionRepository.searchInspections(query)
    }
}