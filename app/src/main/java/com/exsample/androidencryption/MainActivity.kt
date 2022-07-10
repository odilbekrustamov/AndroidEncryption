package com.exsample.androidencryption

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.exsample.androidencryption.encryption.Asymmetric
import com.exsample.androidencryption.encryption.Asymmetric.Companion.decryptMessage
import com.exsample.androidencryption.encryption.Asymmetric.Companion.encryptMessage
import com.exsample.androidencryption.encryption.Symmetric.decrypt
import com.exsample.androidencryption.encryption.Symmetric.encrypt
import com.exsample.androidencryption.keystore.KeyStore
import com.exsample.androidencryption.keystore.KeyStoreTask
import java.util.*

class MainActivity : AppCompatActivity() {
    private  val TAG = "MainActivity"
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        justTest()

    }

    private fun justTest2() {
        val pairKey = KeyStore(this).createNewKeys("FootZoneApplication")
        Log.d("TAG", "justTest2: ${pairKey?.public}")
        Log.d("TAG", "justTest2: ${pairKey?.private}")
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun justTest() {
        val keyStoreTask = KeyStoreTask(this).instance()
        val generatedKey = keyStoreTask.generateSecretKey()
        Log.d("TAG", "onCreate: $generatedKey")

        val secretKey = keyStoreTask.getSecretKey()
        Log.d("TAG", "onCreate: $secretKey")

        val encryptedText = keyStoreTask.encrypt("Odilbek")
        Log.d("TAG", "justTest: $encryptedText")

        val decryptedText = keyStoreTask.decrypt(encryptedText!!)
        Log.d("TAG", "justTest: $decryptedText")
    }

   @RequiresApi(Build.VERSION_CODES.O)
    private fun testAsymmetric() {
        val secretText = "Omad yordur dovyuraklarga!!!"
        val keyPairGenerator = Asymmetric()
        // Generate private and public key
        val privateKey: String =
            Base64.getEncoder().encodeToString(keyPairGenerator.privateKey.encoded)
        val publicKey: String =
            Base64.getEncoder().encodeToString(keyPairGenerator.publicKey.encoded)
        Log.d(TAG, "Private Key: $privateKey")
        Log.d(TAG, "Public Key: $publicKey")
        // Encrypt secret text using public key
        val encryptedValue = encryptMessage(secretText, publicKey)
        Log.d(TAG, "Encrypted Value: $encryptedValue")
        // Decrypt
        val decryptedText = decryptMessage(encryptedValue, privateKey)
        Log.d(TAG, "Decrypted output: $decryptedText")
    }



    @RequiresApi(Build.VERSION_CODES.O)
    private fun testSymmetric() {
        //secret text
        val originalString = "Pdp academy"
        // Encryption
        val encryptedString = encrypt(originalString)
        // Decryption
        val decryptedString = decrypt(encryptedString)

        Log.d(TAG, "Original String: $originalString")
        Log.d(TAG, "Encrypted String: $encryptedString")
        Log.d(TAG, "Decrypted String: $decryptedString")
    }
}