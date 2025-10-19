package com.metalinspect.app.data.database.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.metalinspect.app.data.database.InspectionDatabase
import com.metalinspect.app.data.database.entities.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.UUID

class InspectionDaoTest {
    
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    
    private lateinit var database: InspectionDatabase
    private lateinit var inspectionDao: InspectionDao
    private lateinit var inspectorDao: InspectorDao
    private lateinit var productTypeDao: ProductTypeDao
    
    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            InspectionDatabase::class.java
        ).allowMainThreadQueries().build()
        
        inspectionDao = database.inspectionDao()
        inspectorDao = database.inspectorDao()
        productTypeDao = database.productTypeDao()
    }
    
    @After
    fun teardown() {
        database.close()
    }
    
    @Test
    fun insertAndGetInspection_returnsCorrectData() = runTest {
        // Given
        val inspection = createTestInspection()
        
        // When
        inspectionDao.insertInspection(inspection)
        val retrieved = inspectionDao.getInspectionById(inspection.id)
        
        // Then
        assertThat(retrieved).isEqualTo(inspection)
    }
    
    @Test
    fun getAllInspections_returnsInDescendingOrderByCreatedAt() = runTest {
        // Given
        val inspection1 = createTestInspection(createdAt = 1000L)
        val inspection2 = createTestInspection(createdAt = 2000L)
        val inspection3 = createTestInspection(createdAt = 1500L)
        
        // When
        inspectionDao.insertInspections(listOf(inspection1, inspection2, inspection3))
        val allInspections = inspectionDao.getAllInspections().first()
        
        // Then
        assertThat(allInspections).hasSize(3)
        assertThat(allInspections[0].createdAt).isEqualTo(2000L)
        assertThat(allInspections[1].createdAt).isEqualTo(1500L)
        assertThat(allInspections[2].createdAt).isEqualTo(1000L)
    }
    
    @Test
    fun getInspectionsByStatus_filtersCorrectly() = runTest {
        // Given
        val draftInspection = createTestInspection(status = InspectionStatus.DRAFT)
        val completedInspection = createTestInspection(status = InspectionStatus.COMPLETED)
        
        // When
        inspectionDao.insertInspections(listOf(draftInspection, completedInspection))
        val draftInspections = inspectionDao.getInspectionsByStatus(InspectionStatus.DRAFT).first()
        
        // Then
        assertThat(draftInspections).hasSize(1)
        assertThat(draftInspections[0].status).isEqualTo(InspectionStatus.DRAFT)
    }
    
    @Test
    fun searchInspections_findsMatchingLotNumber() = runTest {
        // Given
        val inspection1 = createTestInspection(lotNumber = "LOT-STEEL-001")
        val inspection2 = createTestInspection(lotNumber = "LOT-PIPE-002")
        
        // When
        inspectionDao.insertInspections(listOf(inspection1, inspection2))
        val searchResults = inspectionDao.searchInspections("STEEL").first()
        
        // Then
        assertThat(searchResults).hasSize(1)
        assertThat(searchResults[0].lotNumber).isEqualTo("LOT-STEEL-001")
    }
    
    @Test
    fun updateInspectionStatus_updatesCorrectly() = runTest {
        // Given
        val inspection = createTestInspection(status = InspectionStatus.DRAFT)
        inspectionDao.insertInspection(inspection)
        
        // When
        val updatedAt = System.currentTimeMillis()
        inspectionDao.updateInspectionStatus(inspection.id, InspectionStatus.COMPLETED, updatedAt)
        
        // Then
        val updated = inspectionDao.getInspectionById(inspection.id)
        assertThat(updated?.status).isEqualTo(InspectionStatus.COMPLETED)
        assertThat(updated?.updatedAt).isEqualTo(updatedAt)
    }
    
    @Test
    fun getInspectionCountByStatus_returnsCorrectCount() = runTest {
        // Given
        val draftInspections = (1..3).map { createTestInspection(status = InspectionStatus.DRAFT) }
        val completedInspections = (1..2).map { createTestInspection(status = InspectionStatus.COMPLETED) }
        
        // When
        inspectionDao.insertInspections(draftInspections + completedInspections)
        
        // Then
        assertThat(inspectionDao.getInspectionCountByStatus(InspectionStatus.DRAFT)).isEqualTo(3)
        assertThat(inspectionDao.getInspectionCountByStatus(InspectionStatus.COMPLETED)).isEqualTo(2)
        assertThat(inspectionDao.getInspectionCount()).isEqualTo(5)
    }
    
    private fun createTestInspection(
        id: String = UUID.randomUUID().toString(),
        lotNumber: String = "LOT-TEST-${System.currentTimeMillis()}",
        status: InspectionStatus = InspectionStatus.DRAFT,
        createdAt: Long = System.currentTimeMillis()
    ) = Inspection(
        id = id,
        lotNumber = lotNumber,
        containerNumber = "CONT-$id",
        productTypeId = "steel-sheet",
        quantity = 100.0,
        weight = 2500.0,
        unit = "kg",
        portLocation = "Test Port",
        weatherConditions = "Clear",
        inspectorId = "inspector-1",
        status = status,
        notes = "Test inspection notes",
        createdAt = createdAt
    )
}