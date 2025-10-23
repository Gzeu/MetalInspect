package com.metalinspect.app.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.metalinspect.app.data.database.dao.DefectDao
import com.metalinspect.app.data.database.dao.InspectionDao
import com.metalinspect.app.data.database.entities.DefectEntity
import com.metalinspect.app.data.database.entities.InspectionEntity
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

/**
 * Room database for MetalInspect application
 * Provides encrypted offline storage for inspection data
 */
@Database(
    entities = [
        InspectionEntity::class,
        DefectEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class MetalInspectDatabase : RoomDatabase() {
    
    abstract fun inspectionDao(): InspectionDao
    abstract fun defectDao(): DefectDao
    
    companion object {
        private const val DATABASE_NAME = "metal_inspect.db"
        
        @Volatile
        private var INSTANCE: MetalInspectDatabase? = null
        
        /**
         * Get database instance with encryption support
         */
        fun getDatabase(
            context: Context,
            passphrase: String? = null
        ): MetalInspectDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = buildDatabase(context, passphrase)
                INSTANCE = instance
                instance
            }
        }
        
        /**
         * Build database with optional encryption
         */
        private fun buildDatabase(
            context: Context,
            passphrase: String? = null
        ): MetalInspectDatabase {
            val builder = Room.databaseBuilder(
                context.applicationContext,
                MetalInspectDatabase::class.java,
                DATABASE_NAME
            )
            
            // Add encryption if passphrase is provided
            passphrase?.let { phrase ->
                val factory = SupportFactory(
                    SQLiteDatabase.getBytes(phrase.toCharArray())
                )
                builder.openHelperFactory(factory)
            }
            
            return builder
                .addMigrations(*getAllMigrations())
                .addCallback(DatabaseCallback())
                .fallbackToDestructiveMigration() // For development only
                .build()
        }
        
        /**
         * Get all database migrations
         */
        private fun getAllMigrations(): Array<Migration> {
            return arrayOf(
                // Future migrations will be added here
                // MIGRATION_1_2,
                // MIGRATION_2_3,
            )
        }
        
        /**
         * Clear database instance (for testing)
         */
        fun clearInstance() {
            INSTANCE?.close()
            INSTANCE = null
        }
    }
    
    /**
     * Database callback for initialization tasks
     */
    private class DatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            // Add any initialization logic here
            // For example, creating indexes, triggers, or inserting default data
        }
        
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            // Add any logic to run every time the database is opened
            // For example, enabling foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON")
        }
    }
}

// Future migration example (commented out)
/*
private val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Add migration logic here
        database.execSQL("ALTER TABLE inspections ADD COLUMN new_column TEXT")
    }
}
*/