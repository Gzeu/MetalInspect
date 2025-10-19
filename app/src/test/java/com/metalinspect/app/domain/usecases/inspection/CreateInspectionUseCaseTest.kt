package com.metalinspect.app.domain.usecases.inspection

import com.google.common.truth.Truth.assertThat
import com.metalinspect.app.data.database.entities.*
import com.metalinspect.app.data.repository.InspectionRepository
import com.metalinspect.app.domain.validators.InspectionValidator
import com.metalinspect.app.utils.ValidationResult
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.*
import java.util.UUID

class CreateInspectionUseCaseTest {
    
    @Mock
    private lateinit var repository: InspectionRepository
    
    @Mock
    private lateinit var validator: InspectionValidator
    
    private lateinit var useCase: CreateInspectionUseCaseImpl
    
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        useCase = CreateInspectionUseCaseImpl(repository, validator)
    }
    
    @Test
    fun createInspection_withValidData_returnsSuccess() = runTest {
        // Given
        val inspection = createValidInspection()
        val inspectionId = "generated-id"
        
        whenever(validator.validateInspection(inspection))
            .thenReturn(ValidationResult.Success)
        whenever(repository.createInspection(inspection))
            .thenReturn(Result.success(inspectionId))
        
        // When
        val result = useCase(inspection)
        
        // Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(inspectionId)
        verify(validator).validateInspection(inspection)
        verify(repository).createInspection(inspection)
    }
    
    @Test
    fun createInspection_withInvalidData_returnsValidationError() = runTest {
        // Given
        val inspection = createValidInspection()
        val validationError = "Invalid lot number format"
        
        whenever(validator.validateInspection(inspection))
            .thenReturn(ValidationResult.Error(validationError))
        
        // When
        val result = useCase(inspection)
        
        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()?.message).contains(validationError)
        verify(validator).validateInspection(inspection)
        verify(repository, never()).createInspection(any())
    }
    
    @Test
    fun createInspection_withRepositoryError_returnsRepositoryError() = runTest {
        // Given
        val inspection = createValidInspection()
        val repositoryError = RuntimeException("Database connection failed")
        
        whenever(validator.validateInspection(inspection))
            .thenReturn(ValidationResult.Success)
        whenever(repository.createInspection(inspection))
            .thenReturn(Result.failure(repositoryError))
        
        // When
        val result = useCase(inspection)
        
        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(repositoryError)
    }
    
    @Test
    fun createInspection_setsCorrectTimestamps() = runTest {
        // Given
        val inspection = createValidInspection()
        val captureSlot = slot<Inspection>()
        
        whenever(validator.validateInspection(any()))
            .thenReturn(ValidationResult.Success)
        whenever(repository.createInspection(capture(captureSlot)))
            .thenReturn(Result.success("id"))
        
        // When
        val startTime = System.currentTimeMillis()
        useCase(inspection)
        val endTime = System.currentTimeMillis()
        
        // Then
        val capturedInspection = captureSlot.captured
        assertThat(capturedInspection.createdAt).isAtLeast(startTime)
        assertThat(capturedInspection.createdAt).isAtMost(endTime)
        assertThat(capturedInspection.updatedAt).isEqualTo(capturedInspection.createdAt)
        assertThat(capturedInspection.status).isEqualTo(InspectionStatus.DRAFT)
    }
    
    private fun createValidInspection() = Inspection(
        id = UUID.randomUUID().toString(),
        lotNumber = "LOT-TEST-001",
        containerNumber = "CONT-001",
        productTypeId = "steel-sheet",
        quantity = 100.0,
        weight = 2500.0,
        unit = "kg",
        portLocation = "Test Port",
        weatherConditions = "Clear",
        inspectorId = "inspector-1",
        status = InspectionStatus.DRAFT,
        notes = "Test notes"
    )
}