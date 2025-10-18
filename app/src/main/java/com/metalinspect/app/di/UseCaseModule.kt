package com.metalinspect.app.di

import com.metalinspect.app.domain.usecases.inspection.*
import com.metalinspect.app.domain.usecases.defects.*
import com.metalinspect.app.domain.usecases.photos.*
import com.metalinspect.app.domain.usecases.reports.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    abstract fun bindCreateInspectionUseCase(
        createInspectionUseCaseImpl: CreateInspectionUseCaseImpl
    ): CreateInspectionUseCase

    @Binds
    abstract fun bindGetInspectionUseCase(
        getInspectionUseCaseImpl: GetInspectionUseCaseImpl
    ): GetInspectionUseCase

    @Binds
    abstract fun bindUpdateInspectionUseCase(
        updateInspectionUseCaseImpl: UpdateInspectionUseCaseImpl
    ): UpdateInspectionUseCase

    @Binds
    abstract fun bindDeleteInspectionUseCase(
        deleteInspectionUseCaseImpl: DeleteInspectionUseCaseImpl
    ): DeleteInspectionUseCase

    @Binds
    abstract fun bindAddDefectUseCase(
        addDefectUseCaseImpl: AddDefectUseCaseImpl
    ): AddDefectUseCase

    @Binds
    abstract fun bindGetDefectCategoriesUseCase(
        getDefectCategoriesUseCaseImpl: GetDefectCategoriesUseCaseImpl
    ): GetDefectCategoriesUseCase

    @Binds
    abstract fun bindCapturePhotoUseCase(
        capturePhotoUseCaseImpl: CapturePhotoUseCaseImpl
    ): CapturePhotoUseCase

    @Binds
    abstract fun bindManagePhotosUseCase(
        managePhotosUseCaseImpl: ManagePhotosUseCaseImpl
    ): ManagePhotosUseCase

    @Binds
    abstract fun bindGenerateReportUseCase(
        generateReportUseCaseImpl: GenerateReportUseCaseImpl
    ): GenerateReportUseCase
}
