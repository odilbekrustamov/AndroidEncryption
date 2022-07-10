package com.exsample.androidencryption.keystore


import android.content.Context
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.annotation.RequiresApi
import com.exsample.androidencryption.R
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class KeyStoreTask(val context: Context) {

    private lateinit var iv: ByteArray
    private var _instance: KeyStoreTask? = null

    fun instance(): KeyStoreTask {
        return if (_instance == null) {
            _instance = KeyStoreTask(context)
            _instance!!
        } else _instance!!
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun generateSecretKey(): SecretKey {
        val keyGenerator =
            KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        val spec = KeyGenParameterSpec
            .Builder(
                context.getString(R.string.app_name),
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .build()

        keyGenerator.init(spec)
        return keyGenerator.generateKey()
    }

    fun getSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
        val secretKeyEntry =
            keyStore.getEntry(context.getString(R.string.app_name), null) as KeyStore.SecretKeyEntry
        return secretKeyEntry.secretKey
    }

    fun encrypt(data: String): ByteArray? {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey())
        iv = cipher.iv
        return cipher.doFinal(data.toByteArray())
    }

    fun decrypt(encrypted: ByteArray): String {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val spec = GCMParameterSpec(128, iv)
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), spec)
        val decoded = cipher.doFinal(encrypted)
        return String(decoded, Charsets.UTF_8)
    }
}