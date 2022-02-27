package com.mauricio.marvel.utils.network

import com.mauricio.marvel.BuildConfig
import java.math.BigInteger
import java.security.MessageDigest
import java.sql.Timestamp

object MarvelAuthenticationUtils {
    const val apiKey = BuildConfig.PUBLIC_API_KEY
    private const val privateKey = BuildConfig.PRIVATE_API_KEY
    private var timestamp = Timestamp(System.currentTimeMillis()).time.toString()

    fun getTimestamp(): String {
        timestamp = Timestamp(System.currentTimeMillis()).time.toString()
        return timestamp
    }

    fun getHash(): String {
        val input = "$timestamp$privateKey$apiKey"
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray()))
            .toString(16).padStart(32, '0')
    }
}