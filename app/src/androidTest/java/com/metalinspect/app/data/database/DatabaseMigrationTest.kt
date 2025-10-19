package com.metalinspect.app.data.database

import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DatabaseMigrationTest {
    
    companion object {
        private const val TEST_DB = "migration-test"
    }
    
    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        InspectionDatabase::class.java,
        listOf(),
        FrameworkSQLiteOpenHelperFactory()
    )
    
    @Test
    fun migrate1To2_containsCorrectData() {
        // Create database version 1
        var db = helper.createDatabase(TEST_DB, 1).apply {
            // Insert test data for version 1
            execSQL(
                "INSERT INTO inspections (id, lot_number, product_type_id, quantity, weight, unit, port_location, weather_conditions, inspector_id, status, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                arrayOf(
                    "test-id-1",
                    "LOT-001",
                    "steel-sheet",
                    100.0,
                    2500.0,
                    "kg",
                    "Port A",
                    "Clear",
                    "inspector-1",
                    "DRAFT",
                    System.currentTimeMillis(),
                    System.currentTimeMillis()
                )
            )
            close()
        }
        
        // Re-open the database with version 2 and provide migration
        db = helper.runMigrationsAndValidate(TEST_DB, 2, true)
        
        // Verify that the data is still there and new columns exist
        val cursor = db.query("SELECT * FROM inspections WHERE id = ?", arrayOf("test-id-1"))
        assertThat(cursor.moveToFirst()).isTrue()
        
        // Check that new columns exist with default values
        val latitudeIndex = cursor.getColumnIndex("latitude")
        val longitudeIndex = cursor.getColumnIndex("longitude")
        
        assertThat(latitudeIndex).isAtLeast(0)
        assertThat(longitudeIndex).isAtLeast(0)
        
        // Check default values
        assertThat(cursor.getDouble(latitudeIndex)).isEqualTo(0.0)
        assertThat(cursor.getDouble(longitudeIndex)).isEqualTo(0.0)
        
        cursor.close()
        db.close()
    }
    
    @Test
    fun createDatabase_insertsPreloadedData() {
        // Create the database
        val db = helper.createDatabase(TEST_DB, 1)
        
        // Verify that product types were preloaded
        val cursor = db.query("SELECT COUNT(*) FROM product_types")
        assertThat(cursor.moveToFirst()).isTrue()
        
        val count = cursor.getInt(0)
        assertThat(count).isGreaterThan(0)
        
        cursor.close()
        
        // Verify specific product types
        val productCursor = db.query("SELECT name FROM product_types WHERE id = ?", arrayOf("steel-sheet"))
        assertThat(productCursor.moveToFirst()).isTrue()
        assertThat(productCursor.getString(0)).isEqualTo("Steel Sheet")
        
        productCursor.close()
        db.close()
    }
    
    @Test
    fun databaseSchema_hasCorrectTables() {
        val db = helper.createDatabase(TEST_DB, 1)
        
        // Check that all expected tables exist
        val expectedTables = listOf(
            "inspectors",
            "inspections", 
            "product_types",
            "defect_records",
            "inspection_photos",
            "checklist_items",
            "checklist_responses"
        )
        
        expectedTables.forEach { tableName ->
            val cursor = db.query(
                "SELECT name FROM sqlite_master WHERE type='table' AND name=?",
                arrayOf(tableName)
            )
            assertThat(cursor.moveToFirst()).isTrue()
            assertThat(cursor.getString(0)).isEqualTo(tableName)
            cursor.close()
        }
        
        db.close()
    }
    
    @Test
    fun databaseIndexes_areCreatedCorrectly() {
        val db = helper.createDatabase(TEST_DB, 1)
        
        // Check that indexes exist for performance-critical queries
        val cursor = db.query(
            "SELECT name FROM sqlite_master WHERE type='index' AND name LIKE 'index_%'"
        )
        
        val indexes = mutableListOf<String>()
        while (cursor.moveToNext()) {
            indexes.add(cursor.getString(0))
        }
        cursor.close()
        
        // Verify key indexes exist
        val expectedIndexes = listOf(
            "index_inspections_lot_number",
            "index_inspections_status", 
            "index_inspections_inspector_id",
            "index_inspectors_company_name",
            "index_inspection_photos_inspection_id"
        )
        
        expectedIndexes.forEach { expectedIndex ->
            assertThat(indexes).contains(expectedIndex)
        }
        
        db.close()
    }
}