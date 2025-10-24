package com.metalinspect.app.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.metalinspect.app.data.database.MetalInspectDatabase
import com.metalinspect.app.data.database.dao.DefectDao
import com.metalinspect.app.data.database.dao.InspectionDao
import com.metalinspect.app.data.database.entities.DefectEntity
import com.metalinspect.app.data.database.entities.InspectionEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.Date

class InspectionDaoTest {
    private lateinit var db: MetalInspectDatabase
    private lateinit var inspectionDao: InspectionDao
    private lateinit var defectDao: DefectDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, MetalInspectDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        inspectionDao = db.inspectionDao()
        defectDao = db.defectDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun insertAndQueryInspection() = runBlocking {
        val now = Date()
        val inspection = InspectionEntity(
            id = "i1",
            vesselName = "V1",
            cargoDescription = "Steel",
            inspectionDate = now,
            inspectorName = "John",
            location = "Port",
            weatherConditions = "Clear",
            cargoQuantity = 100.0,
            cargoUnit = "MT",
            inspectionType = "loading",
            qualityGrade = null,
            moistureContent = null,
            contaminationLevel = null,
            overallCondition = "Good",
            notes = null,
            isCompleted = false,
            isSynced = false,
            createdAt = now,
            updatedAt = now,
            photoPaths = emptyList()
        )
        inspectionDao.insertInspection(inspection)
        val fetched = inspectionDao.getInspectionById("i1")
        assertEquals("V1", fetched?.vesselName)
    }

    @Test
    fun relationInspectionWithDefects() = runBlocking {
        val now = Date()
        val i = InspectionEntity(
            id = "i2",
            vesselName = "Vessel",
            cargoDescription = "Coils",
            inspectionDate = now,
            inspectorName = "Jane",
            location = "Port",
            weatherConditions = "Clear",
            cargoQuantity = 200.0,
            cargoUnit = "MT",
            inspectionType = "discharge",
            qualityGrade = null,
            moistureContent = null,
            contaminationLevel = null,
            overallCondition = "Good",
            notes = null,
            isCompleted = true,
            isSynced = true,
            createdAt = now,
            updatedAt = now,
            photoPaths = emptyList()
        )
        inspectionDao.insertInspection(i)
        val d1 = DefectEntity(
            id = "d1", inspectionId = "i2", defectType = "rust", severity = "low",
            description = "minor rust", locationDescription = null,
            estimatedAffectedQuantity = null, estimatedAffectedPercentage = 1.0,
            photoPaths = emptyList(), recommendedAction = null, isCritical = false,
            createdAt = now, updatedAt = now
        )
        defectDao.insertDefect(d1)
        val rel = inspectionDao.getInspectionWithDefects("i2")
        assertEquals(1, rel?.defects?.size)
    }
}
