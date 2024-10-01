package com.example.listfetcher.data

import android.util.Log
import com.example.listfetcher.di.IoDispatcher
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext
import logcat.logcat
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDatasource @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    // Traditionally, I'd use Retrofit paired with relevant API calls
    // But since this is a simple datasource, I've streamlined this piece of the handling

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

    private val dataList = Types.newParameterizedType(List::class.java, DataObj::class.java)
    private val jsonAdapter: JsonAdapter<List<DataObj>> = moshi.adapter(dataList)
    private fun getLoggingInterceptor(): HttpLoggingInterceptor {
        val logLevel = HttpLoggingInterceptor.Level.BODY
        return HttpLoggingInterceptor { message ->
            Log.d("OkHttp", message)
        }.setLevel(logLevel)
    }

    suspend fun getParsedList(): Flow<List<DataObj>?> =
        withContext(ioDispatcher) {
            try {
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    return@withContext flowOf(response.body?.string()
                        ?.let { jsonAdapter.fromJson(it) })
                } else {
                    return@withContext flowOf(null)
                }
            } catch (e: Exception) {
                logcat("SafeCall") { "Exception, ${e.message}" }
                return@withContext flowOf(null)
            }
        }
}

