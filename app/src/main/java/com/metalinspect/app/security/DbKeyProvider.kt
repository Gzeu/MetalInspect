package com.metalinspect.app.security

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/**
 * Derives and stores an app-specific database key using Android Keystore.
 * A random 32-byte key is generated once, wrapped by a Keystore AES key, and stored.
 */
object DbKeyProvider {
    private const val ANDROID_KEYSTORE = "AndroidKeyStore"
    private const val MASTER_KEY_ALIAS = "metalinspect_master_aes"
    private const val PREFS = "metalinspect.secure.prefs"
    private const val WRAPPED_DB_KEY = "wrapped_db_key"
    private const val GCM_IV = "gcm_iv"

    fun getOrCreateDbKey(context: Context): ByteArray {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val wrapped = prefs.getString(WRAPPED_DB_KEY, null)?.let { android.util.Base64.decode(it, android.util.Base64.DEFAULT) }
        val iv = prefs.getString(GCM_IV, null)?.let { android.util.Base64.decode(it, android.util.Base64.DEFAULT) }

        val masterKey = getOrCreateMasterKey()
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")

        if (wrapped != null && iv != null) {
            cipher.init(Cipher.DECRYPT_MODE, masterKey, GCMParameterSpec(128, iv))
            return cipher.doFinal(wrapped)
        }

        // Generate random 32-byte DB key
        val dbKey = java.security.SecureRandom().let { rnd ->
            ByteArray(32).apply { rnd.nextBytes(this) }
        }

        // Wrap and store
        cipher.init(Cipher.ENCRYPT_MODE, masterKey)
        val wrappedNew = cipher.doFinal(dbKey)
        val usedIv = cipher.iv

        prefs.edit()
            .putString(WRAPPED_DB_KEY, android.util.Base64.encodeToString(wrappedNew, android.util.Base64.NO_WRAP))
            .putString(GCM_IV, android.util.Base64.encodeToString(usedIv, android.util.Base64.NO_WRAP))
            .apply()

        return dbKey
    }

    private fun getOrCreateMasterKey(): SecretKey {
        val ks = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
        ks.getKey(MASTER_KEY_ALIAS, null)?.let { return it as SecretKey }

        val keyGen = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
        keyGen.init(
            KeyGenParameterSpec.Builder(
                MASTER_KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setRandomizedEncryptionRequired(true)
                .setUserAuthenticationRequired(false)
                .build()
        )
        return keyGen.generateKey()
    }
}
