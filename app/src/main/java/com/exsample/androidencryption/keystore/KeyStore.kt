package com.exsample.androidencryption.keystore

import android.content.Context
import android.security.KeyPairGeneratorSpec
import android.security.keystore.KeyProperties
import android.util.Log
import android.widget.Toast
import java.math.BigInteger
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.util.*
import javax.security.auth.x500.X500Principal


class KeyStore(private val context: Context) {
    private val keyStore: KeyStore =
        KeyStore.getInstance("AndroidKeyStore")

    init {
        keyStore.load(null)
    }

    fun createNewKeys(aliasText: String): KeyPair? {
        try {
            // Create new key if needed
            if (!keyStore.containsAlias(aliasText)) {
                val start: Calendar = Calendar.getInstance()
                val end: Calendar = Calendar.getInstance()
                end.add(Calendar.YEAR, 1)
                val spec = KeyPairGeneratorSpec.Builder(context)
                    .setAlias(aliasText)
                    .setSubject(X500Principal("CN=Sample Name, O=Android Authority"))
                    .setSerialNumber(BigInteger.ONE)
                    .setStartDate(start.time)
                    .setEndDate(end.time)
                    .build()
                val generator: KeyPairGenerator =
                    KeyPairGenerator.getInstance("RSA", "AndroidKeyStore")
                generator.initialize(spec)
                return generator.generateKeyPair()
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Exception " + e.message + " occured", Toast.LENGTH_LONG).show()
            Log.e("TAG", Log.getStackTraceString(e))
        }
        return null
    }
}