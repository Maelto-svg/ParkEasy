package com.example.parkingapp

import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.InetAddress
import java.util.concurrent.TimeUnit
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object RetrofitClient {
    private const val BASE_URL = "http://parkeasy.cleverapps.io/api/parking/"
    private const val TAG = "RetrofitClient"

    private val cookieJar = mutableMapOf<String, List<Cookie>>()

    private val cookieHandler = object : CookieJar {
        override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
            cookieJar[url.host] = cookies
        }

        override fun loadForRequest(url: HttpUrl): List<Cookie> {
            return cookieJar[url.host] ?: listOf()
        }
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val loginInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val response = chain.proceed(originalRequest)

        if (response.code == 200 && response.peekBody(Long.MAX_VALUE).string().contains("Please sign in")) {
            response.close()

            val loginFormBody = FormBody.Builder()
                .add("username", "user")
                .add("password", "password")
                .build()

            val loginRequest = Request.Builder()
                .url("http://parkeasy.cleverapps.io/login")
                .post(loginFormBody)
                .build()

            val loginResponse = chain.proceed(loginRequest)

            if (loginResponse.isSuccessful) {
                loginResponse.close()
                return@Interceptor chain.proceed(originalRequest)
            }
            return@Interceptor loginResponse
        }

        response
    }

    private fun getUnsafeOkHttpClient(): OkHttpClient {
        try {
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
                override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
            })

            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())
            val sslSocketFactory = sslContext.socketFactory

            return OkHttpClient.Builder()
                .cookieJar(cookieHandler)
                .followRedirects(true)
                .followSslRedirects(true)
                .addInterceptor(loginInterceptor)
                .addInterceptor(loggingInterceptor)
                .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                .hostnameVerifier { _, _ -> true }
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(getUnsafeOkHttpClient())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val parkingApi: ParkingApi = retrofit.create(ParkingApi::class.java)
}