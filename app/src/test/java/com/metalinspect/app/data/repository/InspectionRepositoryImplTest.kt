package com.metalinspect.app.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.metalinspect.app.data.database.dao.InspectionDao
import com.metalinspect.app.data.database.entities.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.*
import java.util.UUID

class InspectionRepositoryImplTest {
    
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    
    @Mock
    private lateinit var inspectionDao: InspectionDao
    
    private lateinit var repository: InspectionRepositoryImpl
    
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = InspectionRepositoryImpl(inspectionDao)
    }
    
    @Test
    fun createInspection_withValidData_returnsSuccessWithId() = runTest {
        // Given
        val inspection = createTestInspection()
        
        // When
        val result = repository.createInspection(inspection)
        
        // Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(inspection.id)
        verify(inspectionDao).insertInspection(inspection)
    }
    
    @Test
    fun createInspection_withDaoException_returnsFailure() = runTest {
        // Given
        val inspection = createTestInspection()
        val exception = RuntimeException("Database error")
        
        whenever(inspectionDao.insertInspection(inspection))
            .thenThrow(exception)
        
        // When
        val result = repository.createInspection(inspection)
        
        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
    }
    
    @Test
    fun getInspections_returnsFlowFromDao() = runTest {
        // Given
        val inspections = listOf(createTestInspection(), createTestInspection())
        whenever(inspectionDao.getAllInspections()).thenReturn(flowOf(inspections))
        
        // When
        val result = repository.getInspections()
        
        // Then
        result.collect { receivedInspections ->
            assertThat(receivedInspections).isEqualTo(inspections)
        }
        verify(inspectionDao).getAllInspections()
    }
    
    @Test
    fun updateInspection_callsDaoUpdate() = runTest {
        // Given
        val inspection = createTestInspection()
        
        // When
        val result = repository.updateInspection(inspection)
        
        // Then
        assertThat(result.isSuccess).isTrue()
        verify(inspectionDao).updateInspection(inspection)
    }
    
    @Test
    fun deleteInspection_callsDaoDelete() = runTest {
        // Given
        val inspectionId = "test-id"
        val inspection = createTestInspection(id = inspectionId)
        
        whenever(inspectionDao.getInspectionById(inspectionId))
            .thenReturn(inspection)
        
        // When
        val result = repository.deleteInspection(inspectionId)
        
        // Then
        assertThat(result.isSuccess).isTrue()
        verify(inspectionDao).getInspectionById(inspectionId)
        verify(inspectionDao).deleteInspection(inspection)
    }
    
    @Test
    fun deleteInspection_withNonExistentId_returnsFailure() = runTest {
        // Given
        val inspectionId = "non-existent-id"
        
        whenever(inspectionDao.getInspectionById(inspectionId))
            .thenReturn(null)
        
        // When
        val result = repository.deleteInspection(inspectionId)
        
        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()?.message).contains("not found")
        verify(inspectionDao).getInspectionById(inspectionId)
        verify(inspectionDao, never()).deleteInspection(any())
    }
    
    private fun createTestInspection(
        id: String = UUID.randomUUID().toString()
    ) = Inspection(
        id = id,
        lotNumber = "LOT-TEST-${System.currentTimeMillis()}",
        containerNumber = "CONT-$id",
        productTypeId = "steel-sheet",
        quantity = 100.0,
        weight = 2500.0,
        unit = "kg",
        portLocation = "Test Port",
        weatherConditions = "Clear",
        inspectorId = "inspector-1",
        status = InspectionStatus.DRAFT
    )
}