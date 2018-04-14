package com.jason.returnoftheping.util

import android.util.Log
import java.security.MessageDigest

/**
 * Created by Jason on 9/24/17.
 */
class HelperUtil {

    private val TAG = HelperUtil::class.java.name

    fun sha256(input: String?): String? {
        var result: String? = input
        input?.let {
            try {
                val md = MessageDigest.getInstance("SHA-256")
                md.update(input.toByteArray(charset("UTF-8")))
                val digest = md.digest()
                result = String.format("%064x", java.math.BigInteger(1, digest))
            } catch (e: Exception) {
                Log.e(TAG, "Error hashing password: ", e)
            }
        }
        return result
    }
}