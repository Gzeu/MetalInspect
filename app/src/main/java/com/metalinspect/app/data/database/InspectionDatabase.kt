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
        const val DATABASE_VERSION = 1

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
            prepopulateProductTypes(db)
        }

        private fun prepopulateProductTypes(db: SupportSQLiteDatabase) {
            val timestamp = System.currentTimeMillis()
            
            val productTypes = listOf(
                "INSERT INTO product_types (id, name, description, is_active, created_at) VALUES ('sheet', 'Steel Sheet', 'Flat steel products', 1, $timestamp)",
                "INSERT INTO product_types (id, name, description, is_active, created_at) VALUES ('pipe', 'Steel Pipe', 'Tubular steel products', 1, $timestamp)",
                "INSERT INTO product_types (id, name, description, is_active, created_at) VALUES ('bar', 'Steel Bar', 'Long steel products', 1, $timestamp)",
                "INSERT INTO product_types (id, name, description, is_active, created_at) VALUES ('profile', 'Steel Profile', 'Structural steel sections', 1, $timestamp)",
                "INSERT INTO product_types (id, name, description, is_active, created_at) VALUES ('coil', 'Steel Coil', 'Coiled steel products', 1, $timestamp)",
                "INSERT INTO product_types (id, name, description, is_active, created_at) VALUES ('plate', 'Steel Plate', 'Thick steel plates', 1, $timestamp)"
            )
            
            productTypes.forEach { query ->
                db.execSQL(query)
            }
        }
    }
}