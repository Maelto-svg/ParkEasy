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

    private val loginInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val response = chain.proceed(originalRequest)

        if (response.code == 200 && response.peekBody(Long.MAX_VALUE).string().contains("Please sign in")) {
            response.close()

            // Perform login
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
                // Retry original request
                return@Interceptor chain.proceed(originalRequest)
            }
            return@Interceptor loginResponse
        }

        return@Interceptor response
    }

    private val debugInterceptor = Interceptor { chain ->
        val request = chain.request()
        Log.d(TAG, "Request URL: ${request.url}")

        val response = chain.proceed(request)
        val rawJson = response.peekBody(Long.MAX_VALUE).string()
        Log.d(TAG, "Raw Response: $rawJson")
        Log.d(TAG, "Response Code: ${response.code}")

        response
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private object CustomDns : Dns {
        override fun lookup(hostname: String): List<InetAddress> {
            return try {
                Dns.SYSTEM.lookup(hostname)
            } catch (e: Exception) {
                Log.e(TAG, "DNS lookup failed: ${e.message}")
                try {
                    listOf(InetAddress.getByName(hostname))
                } catch (e: Exception) {
                    Log.e(TAG, "Backup DNS lookup failed: ${e.message}")
                    throw e
                }
            }
        }
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
                .dns(CustomDns)
                .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                .hostnameVerifier { _, _ -> true }
                .cookieJar(cookieHandler)
                .followRedirects(true)
                .followSslRedirects(true)
                .addInterceptor(loginInterceptor)
                .addInterceptor(debugInterceptor)
                .addInterceptor(loggingInterceptor)
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