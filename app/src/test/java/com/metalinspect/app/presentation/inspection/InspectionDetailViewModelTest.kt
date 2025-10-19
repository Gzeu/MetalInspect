package com.metalinspect.app.presentation.inspection.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.metalinspect.app.data.database.entities.*
import com.metalinspect.app.domain.models.InspectionWithDetails
import com.metalinspect.app.domain.usecases.inspection.GetInspectionUseCase
import com.metalinspect.app.domain.usecases.inspection.UpdateInspectionUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.*
import java.util.UUID

@ExperimentalCoroutinesApi
class InspectionDetailViewModelTest {
    
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    
    private val testDispatcher = UnconfinedTestDispatcher()
    
    @Mock
    private lateinit var getInspectionUseCase: GetInspectionUseCase
    
    @Mock
    private lateinit var updateInspectionUseCase: UpdateInspectionUseCase
    
    private lateinit var viewModel: InspectionDetailViewModel
    
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = InspectionDetailViewModel(getInspectionUseCase, updateInspectionUseCase)
    }
    
    @After
    fun teardown() {
        Dispatchers.resetMain()
    }
    
    @Test
    fun loadInspectionDetails_withValidId_updatesStateFlow() = runTest {
        // Given
        val inspectionId = "test-id"
        val inspectionDetails = createTestInspectionWithDetails()
        
        whenever(getInspectionUseCase.getInspectionWithDetails(inspectionId))
            .thenReturn(inspectionDetails)
        
        // When
        viewModel.loadInspectionDetails(inspectionId)
        
        // Then
        assertThat(viewModel.inspectionDetails.value).isEqualTo(inspectionDetails)
        assertThat(viewModel.isLoading.value).isFalse()
        verify(getInspectionUseCase).getInspectionWithDetails(inspectionId)
    }
    
    @Test
    fun loadInspectionDetails_withInvalidId_showsError() = runTest {
        // Given
        val inspectionId = "invalid-id"
        val exception = RuntimeException("Inspection not found")
        
        whenever(getInspectionUseCase.getInspectionWithDetails(inspectionId))
            .thenThrow(exception)
        
        // When
        viewModel.loadInspectionDetails(inspectionId)
        
        // Then
        assertThat(viewModel.inspectionDetails.value).isNull()
        assertThat(viewModel.error.value).contains("Failed to load inspection details")
        assertThat(viewModel.isLoading.value).isFalse()
    }
    
    @Test
    fun completeInspection_withValidId_showsSuccessMessage() = runTest {
        // Given
        val inspectionId = "test-id"
        val inspectionDetails = createTestInspectionWithDetails()
        
        whenever(updateInspectionUseCase.completeInspection(inspectionId))
            .thenReturn(Result.success(Unit))
        whenever(getInspectionUseCase.getInspectionWithDetails(inspectionId))
            .thenReturn(inspectionDetails)
        
        // When
        viewModel.completeInspection(inspectionId)
        
        // Then
        assertThat(viewModel.message.value).contains("completed successfully")
        verify(updateInspectionUseCase).completeInspection(inspectionId)
        verify(getInspectionUseCase).getInspectionWithDetails(inspectionId) // Refresh call
    }
    
    @Test
    fun completeInspection_withError_showsErrorMessage() = runTest {
        // Given
        val inspectionId = "test-id"
        val error = RuntimeException("Cannot complete inspection")
        
        whenever(updateInspectionUseCase.completeInspection(inspectionId))
            .thenReturn(Result.failure(error))
        
        // When
        viewModel.completeInspection(inspectionId)
        
        // Then
        assertThat(viewModel.error.value).contains("Cannot complete inspection")
        verify(updateInspectionUseCase).completeInspection(inspectionId)
        verify(getInspectionUseCase, never()).getInspectionWithDetails(any())
    }
    
    @Test
    fun initialState_hasCorrectDefaults() {
        // Then
        assertThat(viewModel.inspectionDetails.value).isNull()
        assertThat(viewModel.isLoading.value).isFalse()
        assertThat(viewModel.error.value).isNull()
        assertThat(viewModel.message.value).isNull()
    }
    
    private fun createTestInspectionWithDetails() = InspectionWithDetails(
        inspection = Inspection(
            id = "test-id",
            lotNumber = "LOT-TEST-001",
            containerNumber = "CONT-001",
            productTypeId = "steel-sheet",
            quantity = 100.0,
            weight = 2500.0,
            unit = "kg",
            portLocation = "Test Port",
            weatherConditions = "Clear",
            inspectorId = "inspector-1",
            status = InspectionStatus.IN_PROGRESS
        ),
        inspector = Inspector(
            id = "inspector-1",
            name = "Test Inspector",
            company = "Test Company",
            role = "Inspector",
            isActive = true
        ),
        productType = ProductType(
            id = "steel-sheet",
            name = "Steel Sheet",
            description = "Test product type",
            isActive = true
        ),
        defectRecords = emptyList(),
        photos = emptyList(),
        checklistResponses = emptyList(),
        totalDefects = 0,
        criticalDefects = 0,
        majorDefects = 0,
        minorDefects = 0,
        photoCount = 0,
        completionPercentage = 0f
    )
}