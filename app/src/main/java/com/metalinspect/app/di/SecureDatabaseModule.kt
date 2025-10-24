package com.metalinspect.app.di

import android.content.Context
import com.metalinspect.app.data.database.MetalInspectDatabase
import com.metalinspect.app.security.DbKeyProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SecureDatabaseModule {
    @Provides
    @Singleton
    fun provideSecureDatabase(@ApplicationContext context: Context): MetalInspectDatabase {
        // Use Keystore-derived key in release, no encryption in debug
        val passphrase: String? = if (com.metalinspect.app.BuildConfig.DEBUG) {
            null
        } else {
            val key = DbKeyProvider.getOrCreateDbKey(context)
            android.util.Base64.encodeToString(key, android.util.Base64.NO_WRAP)
        }
        return MetalInspectDatabase.getDatabase(context, passphrase)
    }
}
