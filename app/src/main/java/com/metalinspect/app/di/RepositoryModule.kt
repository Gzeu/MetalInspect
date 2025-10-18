package com.metalinspect.app.di

import com.metalinspect.app.data.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindInspectionRepository(
        inspectionRepositoryImpl: InspectionRepositoryImpl
    ): InspectionRepository

    @Binds
    @Singleton
    abstract fun bindInspectorRepository(
        inspectorRepositoryImpl: InspectorRepositoryImpl
    ): InspectorRepository

    @Binds
    @Singleton
    abstract fun bindProductRepository(
        productRepositoryImpl: ProductRepositoryImpl
    ): ProductRepository

    @Binds
    @Singleton
    abstract fun bindDefectRepository(
        defectRepositoryImpl: DefectRepositoryImpl
    ): DefectRepository

    @Binds
    @Singleton
    abstract fun bindPhotoRepository(
        photoRepositoryImpl: PhotoRepositoryImpl
    ): PhotoRepository
}
