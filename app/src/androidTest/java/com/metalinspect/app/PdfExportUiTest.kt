package com.metalinspect.app

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.metalinspect.app.data.pdf.PdfReportGenerator
import com.metalinspect.app.domain.model.Defect
import com.metalinspect.app.domain.model.Inspection
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.util.Date

@RunWith(AndroidJUnit4::class)
class PdfExportUiTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(androidx.activity.ComponentActivity::class.java)

    @Test
    fun generatePdf_and_saveToCache_succeeds_under10MB() {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        val cacheFile = File(ctx.cacheDir, "test_export.pdf")
        cacheFile.outputStream().use { out ->
            val generator = PdfReportGenerator()
            val inspection = Inspection(
                id = "t1",
                vesselName = "Vessel",
                cargoDescription = "Steel coils",
                inspectionDate = Date(),
                inspectorName = "Tester",
                location = "Port",
                weatherConditions = "Clear",
                cargoQuantity = 1000.0,
                cargoUnit = "MT",
                inspectionType = "loading",
                qualityGrade = null,
                moistureContent = null,
                contaminationLevel = null,
                overallCondition = "Good",
                notes = "Sample"
            )
            val defects = listOf<Defect>()
            generator.generate(out, inspection, defects, emptyList())
        }
        assertTrue(cacheFile.exists())
        assertTrue("PDF should be < 10MB", cacheFile.length() < 10L * 1024 * 1024)
        cacheFile.delete()
    }
}
