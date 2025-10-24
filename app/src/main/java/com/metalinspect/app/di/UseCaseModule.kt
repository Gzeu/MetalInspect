package com.metalinspect.app.di

import com.metalinspect.app.domain.repository.InspectionRepository
import com.metalinspect.app.domain.usecase.AddDefectUseCase
import com.metalinspect.app.domain.usecase.CreateInspectionUseCase
import com.metalinspect.app.domain.usecase.DeleteInspectionUseCase
import com.metalinspect.app.domain.usecase.GetInspectionsUseCase
import com.metalinspect.app.domain.usecase.UpdateInspectionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Hilt module providing use case instances
 */
@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    
    @Provides
    fun provideCreateInspectionUseCase(
        repository: InspectionRepository
    ): CreateInspectionUseCase {
        return CreateInspectionUseCase(repository)
    }
    
    @Provides
    fun provideAddDefectUseCase(
        repository: InspectionRepository
    ): AddDefectUseCase {
        return AddDefectUseCase(repository)
    }
    
    @Provides
    fun provideGetInspectionsUseCase(
        repository: InspectionRepository
    ): GetInspectionsUseCase {
        return GetInspectionsUseCase(repository)
    }
    
    @Provides
    fun provideUpdateInspectionUseCase(
        repository: InspectionRepository
    ): UpdateInspectionUseCase {
        return UpdateInspectionUseCase(repository)
    }
    
    @Provides
    fun provideDeleteInspectionUseCase(
        repository: InspectionRepository
    ): DeleteInspectionUseCase {
        return DeleteInspectionUseCase(repository)
    }
}