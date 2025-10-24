package com.metalinspect.app.domain

import com.metalinspect.app.domain.model.Inspection
import com.metalinspect.app.domain.repository.InspectionRepository
import com.metalinspect.app.domain.usecase.CreateInspectionUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Date

class CreateInspectionUseCaseTest {
    @Test
    fun createInspection_returnsSuccess() = runBlocking {
        val fakeRepo = object : InspectionRepository by FakeRepo() {
            override suspend fun createInspection(inspection: Inspection) = Result.success(inspection.id)
        }
        val useCase = CreateInspectionUseCase(fakeRepo)
        val insp = Inspection(
            id = "x1", vesselName = "Vessel", cargoDescription = "Steel", inspectionDate = Date(),
            inspectorName = "John", location = "Port", weatherConditions = "Clear",
            cargoQuantity = 10.0, cargoUnit = "MT", inspectionType = "loading",
            qualityGrade = null, moistureContent = null, contaminationLevel = null,
            overallCondition = "Good", notes = null
        )
        val result = useCase(insp)
        assertTrue(result.isSuccess)
    }
}

private open class FakeRepo : InspectionRepository {
    override suspend fun createInspection(inspection: Inspection) = Result.failure(Exception("not impl"))
    override suspend fun createDefect(defect: com.metalinspect.app.domain.model.Defect) = Result.failure(Exception("not impl"))
    override suspend fun createDefects(defects: List<com.metalinspect.app.domain.model.Defect>) = Result.failure(Exception("not impl"))
    override suspend fun getInspectionById(id: String) = Result.success(null)
    override fun getInspectionByIdFlow(id: String) = kotlinx.coroutines.flow.flow { emit(null) }
    override fun getAllInspections() = kotlinx.coroutines.flow.flow { emit(emptyList<Inspection>()) }
    override fun getInspectionsByStatus(isCompleted: Boolean) = kotlinx.coroutines.flow.flow { emit(emptyList<Inspection>()) }
    override fun getInspectionsByType(type: String) = kotlinx.coroutines.flow.flow { emit(emptyList<Inspection>()) }
    override fun getInspectionsByVessel(vesselName: String) = kotlinx.coroutines.flow.flow { emit(emptyList<Inspection>()) }
    override fun getInspectionsByDateRange(startDate: Date, endDate: Date) = kotlinx.coroutines.flow.flow { emit(emptyList<Inspection>()) }
    override fun getInspectionsByInspector(inspectorName: String) = kotlinx.coroutines.flow.flow { emit(emptyList<Inspection>()) }
    override fun searchInspections(query: String) = kotlinx.coroutines.flow.flow { emit(emptyList<Inspection>()) }
    override suspend fun getInspectionWithDefects(id: String) = Result.success(null)
    override fun getAllInspectionsWithDefects() = kotlinx.coroutines.flow.flow { emit(emptyList<Pair<Inspection, List<com.metalinspect.app.domain.model.Defect>>>()) }
    override suspend fun getDefectById(id: String) = Result.success(null)
    override suspend fun getDefectsByInspectionId(inspectionId: String) = Result.success(emptyList<com.metalinspect.app.domain.model.Defect>())
    override fun getDefectsByInspectionIdFlow(inspectionId: String) = kotlinx.coroutines.flow.flow { emit(emptyList<com.metalinspect.app.domain.model.Defect>()) }
    override fun getAllDefects() = kotlinx.coroutines.flow.flow { emit(emptyList<com.metalinspect.app.domain.model.Defect>()) }
    override fun getDefectsByType(type: String) = kotlinx.coroutines.flow.flow { emit(emptyList<com.metalinspect.app.domain.model.Defect>()) }
    override fun getDefectsBySeverity(severity: String) = kotlinx.coroutines.flow.flow { emit(emptyList<com.metalinspect.app.domain.model.Defect>()) }
    override fun getCriticalDefects() = kotlinx.coroutines.flow.flow { emit(emptyList<com.metalinspect.app.domain.model.Defect>()) }
    override suspend fun updateInspection(inspection: Inspection) = Result.success(Unit)
    override suspend fun updateDefect(defect: com.metalinspect.app.domain.model.Defect) = Result.success(Unit)
    override suspend fun updateInspectionStatus(id: String, isCompleted: Boolean) = Result.success(Unit)
    override suspend fun updateSyncStatus(ids: List<String>, isSynced: Boolean) = Result.success(Unit)
    override suspend fun deleteInspection(id: String) = Result.success(Unit)
    override suspend fun deleteDefect(id: String) = Result.success(Unit)
    override suspend fun deleteDefectsByInspectionId(inspectionId: String) = Result.success(Unit)
    override suspend fun getInspectionStatistics() = Result.success(emptyMap<String, Any>())
    override suspend fun getDefectStatistics() = Result.success(emptyMap<String, Any>())
    override suspend fun getPendingSyncInspections() = Result.success(emptyList<Inspection>())
}
