package com.metalinspect.app.presentation

import app.cash.turbine.test
import com.metalinspect.app.domain.model.Inspection
import com.metalinspect.app.domain.repository.InspectionRepository
import com.metalinspect.app.domain.usecase.CreateInspectionUseCase
import com.metalinspect.app.domain.usecase.DeleteInspectionUseCase
import com.metalinspect.app.domain.usecase.GetInspectionsUseCase
import com.metalinspect.app.presentation.viewmodel.InspectionListViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Date

class InspectionListViewModelTest {
    @Test
    fun loadsInspections_and_updatesState() = runBlocking {
        val sample = listOf(
            Inspection(
                id = "a", vesselName = "V1", cargoDescription = "Steel", inspectionDate = Date(),
                inspectorName = "I", location = "L", weatherConditions = "W",
                cargoQuantity = 1.0, cargoUnit = "MT", inspectionType = "loading",
                qualityGrade = null, moistureContent = null, contaminationLevel = null,
                overallCondition = "Good", notes = null
            )
        )
        val repo = object : InspectionRepository by FakeRepo() {
            override fun getAllInspections() = MutableStateFlow(sample)
        }
        val vm = InspectionListViewModel(
            GetInspectionsUseCase(repo),
            CreateInspectionUseCase(repo),
            DeleteInspectionUseCase(repo)
        )
        assertEquals(1, vm.uiState.value.inspections.size)
    }
}
