package com.example.listfetcher.data

import android.util.Log
import com.example.listfetcher.di.IoDispatcher
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import kotlinx.coroutines.withContext
import logcat.logcat
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDatasource @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    private val client: OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(getLoggingInterceptor())
            .build()

    private val request = Request.Builder()
        .url("https://fetch-hiring.s3.amazonaws.com/hiring.json")
        .build()

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val jsonAdapter = moshi.adapter(DataList::class.java)
    private fun getLoggingInterceptor(): HttpLoggingInterceptor {
        val logLevel = HttpLoggingInterceptor.Level.BODY
        return HttpLoggingInterceptor { message ->
            Log.d("OkHttp", message)
        }.setLevel(logLevel)
    }

    private suspend fun getJsonListAsString(): String =
        withContext(ioDispatcher) {
            try {
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    response.body.toString()
                } else {
                    response.code.toString()
                }
            } catch (e: Exception) {
                logcat("SafeCall") { "Exception, ${e.message}" }
                e.message.toString()
            }
        }

    suspend fun getParsedList(): Flow<DataList?> {
        return flowOf(jsonAdapter.fromJson(getJsonListAsString()))
    }
}
