package com.metalinspect.app.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import android.content.Context
import com.metalinspect.app.data.database.dao.*
import com.metalinspect.app.data.database.entities.*
import com.metalinspect.app.data.database.converters.DateConverters
import net.sqlcipher.database.SupportFactory
import net.sqlcipher.database.SQLiteDatabase

@Database(
    entities = [
        Inspector::class,
        Inspection::class,
        ProductType::class,
        DefectRecord::class,
        InspectionPhoto::class,
        ChecklistItem::class,
        ChecklistResponse::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(DateConverters::class)
abstract class InspectionDatabase : RoomDatabase() {
    
    abstract fun inspectorDao(): InspectorDao
    abstract fun inspectionDao(): InspectionDao
    abstract fun productTypeDao(): ProductTypeDao
    abstract fun defectRecordDao(): DefectRecordDao
    abstract fun inspectionPhotoDao(): InspectionPhotoDao
    abstract fun checklistDao(): ChecklistDao

    companion object {
        const val DATABASE_NAME = "inspection_database"

        fun buildDatabase(
            context: Context,
            useEncryption: Boolean = false
        ): InspectionDatabase {
            val builder = Room.databaseBuilder(
                context.applicationContext,
                InspectionDatabase::class.java,
                DATABASE_NAME
            )
            .addCallback(DatabaseCallback())
            .addMigrations(MIGRATION_1_2)
            .fallbackToDestructiveMigration() // Remove in production
            
            if (useEncryption) {
                val passphrase = SQLiteDatabase.getBytes("MetalInspect2024".toCharArray())
                builder.openHelperFactory(SupportFactory(passphrase))
            }

            return builder.build()
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Example migration - add GPS coordinates
                database.execSQL(
                    "ALTER TABLE inspections ADD COLUMN latitude REAL DEFAULT 0.0"
                )
                database.execSQL(
                    "ALTER TABLE inspections ADD COLUMN longitude REAL DEFAULT 0.0"
                )
            }
        }
    }

    private class DatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            // Prepopulate with default product types
            prepopulateProductTypes(db)
            prepopulateDefectTypes(db)
        }

        private fun prepopulateProductTypes(db: SupportSQLiteDatabase) {
            val productTypes = listOf(
                "('sheet', 'Steel Sheet', 'Flat steel products', 1)",
                "('pipe', 'Steel Pipe', 'Tubular steel products', 1)",
                "('bar', 'Steel Bar', 'Long steel products', 1)",
                "('profile', 'Steel Profile', 'Structural steel sections', 1)",
                "('coil', 'Steel Coil', 'Coiled steel products', 1)",
                "('plate', 'Steel Plate', 'Thick steel plates', 1)"
            )
            
            productTypes.forEach { values ->
                db.execSQL(
                    """
                    INSERT INTO product_types (id, name, description, is_active) 
                    VALUES $values
                    """
                )
            }
        }

        private fun prepopulateDefectTypes(db: SupportSQLiteDatabase) {
            val defectTypes = listOf(
                // Surface Defects
                "INSERT INTO defect_categories VALUES ('surface_corrosion', 'Surface Corrosion', 'Rust or oxidation on surface', 'SURFACE')",
                "INSERT INTO defect_categories VALUES ('surface_scratches', 'Surface Scratches', 'Visible scratches or abrasions', 'SURFACE')",
                "INSERT INTO defect_categories VALUES ('surface_discoloration', 'Surface Discoloration', 'Color changes or staining', 'SURFACE')",
                
                // Dimensional Defects  
                "INSERT INTO defect_categories VALUES ('dim_deformation', 'Deformation', 'Shape distortion or bending', 'DIMENSIONAL')",
                "INSERT INTO defect_categories VALUES ('dim_oversized', 'Oversized', 'Dimensions exceed specifications', 'DIMENSIONAL')",
                "INSERT INTO defect_categories VALUES ('dim_undersized', 'Undersized', 'Dimensions below specifications', 'DIMENSIONAL')",
                
                // Material Defects
                "INSERT INTO defect_categories VALUES ('mat_missing', 'Missing Items', 'Missing pieces or components', 'MATERIAL')",
                "INSERT INTO defect_categories VALUES ('mat_wrong_grade', 'Wrong Grade', 'Incorrect material specification', 'MATERIAL')",
                "INSERT INTO defect_categories VALUES ('mat_contamination', 'Contamination', 'Foreign material present', 'MATERIAL')",
                
                // Packaging Defects
                "INSERT INTO defect_categories VALUES ('pack_damage', 'Packaging Damage', 'Damaged wrapping or protection', 'PACKAGING')",
                "INSERT INTO defect_categories VALUES ('pack_moisture', 'Moisture Exposure', 'Water damage or exposure', 'PACKAGING')",
                "INSERT INTO defect_categories VALUES ('pack_improper', 'Improper Packaging', 'Incorrect packaging method', 'PACKAGING')"
            )
            
            defectTypes.forEach { query ->
                db.execSQL(query)
            }
        }
    }
}
