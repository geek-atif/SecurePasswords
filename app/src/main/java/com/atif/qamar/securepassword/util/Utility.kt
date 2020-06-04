package com.atif.qamar.securepassword.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.security.GeneralSecurityException
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


/**
 * Created by Atif Qamar on 27-05-2020.
 */

class Utility {

    companion object {
        var TAG = "com.atif.qamar.securepassword.util.Utility"

        private val algorithm = "AES/CBC/NoPadding"
        private val keyValue = byteArrayOf(
            '0'.toByte(),
            '1'.toByte(),
            '2'.toByte(),
            '3'.toByte(),
            '4'.toByte(),
            '5'.toByte(),
            '6'.toByte(),
            '7'.toByte(),
            '8'.toByte(),
            '9'.toByte(),
            'a'.toByte(),
            'b'.toByte(),
            'c'.toByte(),
            'd'.toByte(),
            'e'.toByte(),
            'f'.toByte()
        )
        private val ivValue = byteArrayOf(
            'f'.toByte(),
            'e'.toByte(),
            'd'.toByte(),
            'c'.toByte(),
            'b'.toByte(),
            'a'.toByte(),
            '9'.toByte(),
            '8'.toByte(),
            '7'.toByte(),
            '6'.toByte(),
            '5'.toByte(),
            '4'.toByte(),
            '3'.toByte(),
            '2'.toByte(),
            '1'.toByte(),
            '0'.toByte()
        )

        private val ivspec: IvParameterSpec = IvParameterSpec(ivValue)
        private val keyspec = SecretKeySpec(keyValue, "AES")

        fun getCurrentTimeStamp() = System.currentTimeMillis()

        @Throws(Exception::class)
        fun encrypt(Data: String): String? {
            val c = Cipher.getInstance(algorithm)
            c.init(Cipher.ENCRYPT_MODE, keyspec, ivspec)
            val encVal = c.doFinal(Data.toByteArray())
            return android.util.Base64.encodeToString(encVal, android.util.Base64.DEFAULT)
        }

        @Throws(Exception::class)
        fun getDecryptData(encryptedData: String?): String? {
            val c = Cipher.getInstance(algorithm)
            c.init(Cipher.DECRYPT_MODE, keyspec, ivspec)
            val decordedValue: ByteArray =
                android.util.Base64.decode(encryptedData, android.util.Base64.DEFAULT)
            val decValue = c.doFinal(decordedValue)
            return String(decValue)
        }

        fun padString(source: String): String? {
            var source = source
            val paddingChar = ' '
            val size = 16
            val x = source.length % size
            val padLength = size - x
            for (i in 0 until padLength) {
                source += paddingChar
            }
            return source
        }

        fun generateHash(data: String): String {
            var hash = ""
            try {
                val digest = MessageDigest.getInstance("SHA-256");
                digest.update(data.toByteArray());
                hash =
                    android.util.Base64.encodeToString(digest.digest(), android.util.Base64.DEFAULT)
            } catch (ex: GeneralSecurityException) {
                Log.e(TAG, ex.message)
            }
            return hash
        }

        fun generateRandomString(
            length: Int,
            nextIsUppercase: Boolean,
            nextIsLowercase: Boolean,
            nextIsSpecial: Boolean,
            nextIsDigit: Boolean
        ): String? {
            Log.i(
                TAG, "generateRandomString() length : $length nextIsUppercase : $nextIsUppercase " +
                        "nextIsLowercase : $nextIsLowercase nextIsSpecial : $nextIsSpecial  nextIsDigit : $nextIsDigit"
            )
            val sb = StringBuilder()
            val rnd = SecureRandom()
            val uppercaseChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            val lowercaseChars = "abcdefghijklmnopqrstuvwxyz"
            val digit = "0123456789"
            val specialChars = "~`!@#$%^&*()-_=+[{]}\\\\|;:\\'\\\",<.>/?"
            var passwordStr = StringBuilder()
            var countDec = 0

            if (nextIsUppercase) {
                sb.append(uppercaseChars[rnd.nextInt(uppercaseChars.length)])
                countDec++
                passwordStr = passwordStr.append(uppercaseChars)
            }
            if (nextIsLowercase) {
                sb.append(lowercaseChars[rnd.nextInt(lowercaseChars.length)])
                countDec++
                passwordStr = passwordStr.append(lowercaseChars)
            }
            if (nextIsDigit) {
                sb.append(digit[rnd.nextInt(digit.length)])
                countDec++
                passwordStr = passwordStr.append(digit)
            }
            if (nextIsSpecial) {
                sb.append(specialChars[rnd.nextInt(specialChars.length)])
                countDec++
                passwordStr = passwordStr.append(specialChars)
            }
            for (i in 1..length - countDec) {
                val index = (passwordStr.length * Math.random()).toInt()
                sb.append(passwordStr[index])
            }
            return sb.toString()
        }

        fun hideKeyBoard(context: Context, view: View) {
            val imm: InputMethodManager? =
                context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun copy(context: Context, data: String) {
            val myClipboard =
                context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
            val myClip = ClipData.newPlainText("Password", data)
            myClipboard?.setPrimaryClip(myClip);
        }

    }
}