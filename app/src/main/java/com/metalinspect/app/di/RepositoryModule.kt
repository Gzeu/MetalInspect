package com.metalinspect.app.di

import com.metalinspect.app.data.repository.InspectionRepositoryImpl
import com.metalinspect.app.domain.repository.InspectionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module providing repository implementations
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    /**
     * Binds InspectionRepositoryImpl to InspectionRepository interface
     */
    @Binds
    @Singleton
    abstract fun bindInspectionRepository(
        inspectionRepositoryImpl: InspectionRepositoryImpl
    ): InspectionRepository
}