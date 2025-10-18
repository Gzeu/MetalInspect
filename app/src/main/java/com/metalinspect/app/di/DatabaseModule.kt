package com.metalinspect.app.di

import android.content.Context
import androidx.room.Room
import com.metalinspect.app.BuildConfig
import com.metalinspect.app.data.database.InspectionDatabase
import com.metalinspect.app.data.database.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideInspectionDatabase(
        @ApplicationContext context: Context
    ): InspectionDatabase {
        return InspectionDatabase.buildDatabase(
            context = context,
            useEncryption = BuildConfig.DATABASE_ENCRYPTION
        )
    }

    @Provides
    fun provideInspectorDao(database: InspectionDatabase): InspectorDao {
        return database.inspectorDao()
    }

    @Provides
    fun provideInspectionDao(database: InspectionDatabase): InspectionDao {
        return database.inspectionDao()
    }

    @Provides
    fun provideProductTypeDao(database: InspectionDatabase): ProductTypeDao {
        return database.productTypeDao()
    }

    @Provides
    fun provideDefectRecordDao(database: InspectionDatabase): DefectRecordDao {
        return database.defectRecordDao()
    }

    @Provides
    fun provideInspectionPhotoDao(database: InspectionDatabase): InspectionPhotoDao {
        return database.inspectionPhotoDao()
    }

    @Provides
    fun provideChecklistDao(database: InspectionDatabase): ChecklistDao {
        return database.checklistDao()
    }
}
