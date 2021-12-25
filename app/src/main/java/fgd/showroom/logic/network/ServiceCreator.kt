package fgd.showroom.logic.network

import com.google.gson.GsonBuilder
import fgd.showroom.logic.dao.ServerUrlDao
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit


object ServiceCreator {
    private fun getClient(type: String = "long"): OkHttpClient {
        var connTimeout = 1
        var readTimeout = 10 * 60
        var writeTimeout = 10 * 60
        if (type == "short") {
            connTimeout = 1
            writeTimeout = 1
            readTimeout = 1
        }
        val client = OkHttpClient.Builder().connectTimeout(connTimeout.toLong(), TimeUnit.SECONDS)
            .readTimeout(readTimeout.toLong(), TimeUnit.SECONDS)
            .writeTimeout(writeTimeout.toLong(), TimeUnit.SECONDS)

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        client.interceptors().add(interceptor)
        return client.build()
    }

    private fun getRetrofit(jsonConverter: Boolean = true, timeout: String): Retrofit {
        val gson = GsonConverterFactory.create(GsonBuilder().setLenient().create())
        val converter = if (jsonConverter) gson else ScalarsConverterFactory.create()

        return Retrofit.Builder()
            .baseUrl("http://" + ServerUrlDao.getSavedUrl())
            .addConverterFactory(converter)
            .client(getClient(timeout)).build()
    }

    fun <T> create(serviceClass: Class<T>, jsonConverter: Boolean = true, timeout: String = "long"): T =
        getRetrofit(jsonConverter, timeout).create(serviceClass)

    inline fun <reified T> create(): T = create(T::class.java)

}
