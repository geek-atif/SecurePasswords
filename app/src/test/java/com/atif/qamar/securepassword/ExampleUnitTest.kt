package com.atif.qamar.securepassword

import org.junit.Test

import org.junit.Assert.*
import java.security.GeneralSecurityException
import java.security.MessageDigest
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testMyData() {
        var encryptionData =  getEncryptionData("123456")
        println("encryptionData : ${encryptionData}")
        var decryptData = getDecryptData(encryptionData)
        println("decryptData : ${decryptData}")

    }



    fun getEncryptionData(data : String ): String{
        var result =""
        try {
            val key = generateKey()
            val cph = Cipher.getInstance("AES")
            cph.init(Cipher.ENCRYPT_MODE, key)
            val encVal = cph.doFinal(data.toByteArray())
            result  = Base64.getEncoder().encode(encVal).toString()
            //result = (android.util.Base64.encode(encVal, android.util.Base64.DEFAULT)).toString()
        } catch (e: GeneralSecurityException) {
        }
        return  result
    }


    fun getDecryptData(data : String ): String{
        var result =""
        try {
            val key = generateKey()
            val cph = Cipher.getInstance("AES")
            cph.init(Cipher.DECRYPT_MODE, key)
            //val decodedValue = (android.util.Base64.decode(data, android.util.Base64.DEFAULT))
            val decodedValue = Base64.getDecoder().decode(data)
            val decValue = cph.doFinal(decodedValue)
            result = decValue.toString()
            return  result
        } catch (e: GeneralSecurityException) {
        }
        return  result
    }

    private fun generateKey() : SecretKeySpec {
        val data = "atif123"
        val  message  = data.toByteArray()
        val digest: MessageDigest = MessageDigest.getInstance("SHA-256")
        digest.update(message)
        val key = digest.digest()
        val secretKeySpec = SecretKeySpec(key, "AES")
        return secretKeySpec
        //result =  (android.util.Base64.encode(digest.digest(), android.util.Base64.DEFAULT)).toString()
    }
}

