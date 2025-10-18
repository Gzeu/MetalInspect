package com.metalinspect.app.di

import android.content.Context
import com.metalinspect.app.data.pdf.PDFReportGenerator
import com.metalinspect.app.data.export.CSVExporter
import com.metalinspect.app.data.export.BackupManager
import com.metalinspect.app.utils.*
import com.metalinspect.app.domain.validators.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {
    
    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }
    
    @Provides
    @Singleton
    fun provideFileUtils(@ApplicationContext context: Context): FileUtils {
        return FileUtils(context)
    }
    
    @Provides
    @Singleton
    fun provideImageUtils(@ApplicationContext context: Context): ImageUtils {
        return ImageUtils(context)
    }
    
    @Provides
    @Singleton
    fun providePermissionUtils(@ApplicationContext context: Context): PermissionUtils {
        return PermissionUtils(context)
    }
    
    @Provides
    @Singleton
    fun provideCameraUtils(@ApplicationContext context: Context): CameraUtils {
        return CameraUtils(context)
    }
    
    @Provides
    @Singleton
    fun providePDFReportGenerator(@ApplicationContext context: Context): PDFReportGenerator {
        return PDFReportGenerator(context)
    }
    
    @Provides
    @Singleton
    fun provideCSVExporter(): CSVExporter {
        return CSVExporter()
    }
    
    @Provides
    @Singleton
    fun provideInspectionValidator(): InspectionValidator {
        return InspectionValidator()
    }
    
    @Provides
    @Singleton
    fun provideFormValidator(): FormValidator {
        return FormValidator()
    }
}