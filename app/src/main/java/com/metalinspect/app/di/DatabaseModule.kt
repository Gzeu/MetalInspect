package com.metalinspect.app.di

import android.content.Context
import com.metalinspect.app.BuildConfig
import com.metalinspect.app.data.database.MetalInspectDatabase
import com.metalinspect.app.data.database.dao.DefectDao
import com.metalinspect.app.data.database.dao.InspectionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module providing database-related dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    /**
     * Provides the main database instance with encryption support
     */
    @Provides
    @Singleton
    fun provideMetalInspectDatabase(
        @ApplicationContext context: Context
    ): MetalInspectDatabase {
        // Use encryption in release builds only
        val passphrase = if (BuildConfig.DEBUG) {
            null // No encryption in debug for easier development
        } else {
            // In production, derive passphrase from secure sources
            // This could be from Android Keystore, user authentication, etc.
            BuildConfig.DB_ENCRYPTION_KEY
        }
        
        return MetalInspectDatabase.getDatabase(
            context = context,
            passphrase = passphrase
        )
    }
    
    /**
     * Provides InspectionDao instance
     */
    @Provides
    fun provideInspectionDao(
        database: MetalInspectDatabase
    ): InspectionDao {
        return database.inspectionDao()
    }
    
    /**
     * Provides DefectDao instance
     */
    @Provides
    fun provideDefectDao(
        database: MetalInspectDatabase
    ): DefectDao {
        return database.defectDao()
    }
}