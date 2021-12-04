package fgd.showroom.logic.network

import fgd.showroom.logic.dao.ServerUrlDao
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object ServiceCreator {

    private val client =
        OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10 * 60, TimeUnit.SECONDS)
            .writeTimeout(10 * 60, TimeUnit.SECONDS).build()

    private fun getRetrofit(jsonConverter: Boolean = true): Retrofit {
        var retrofit = Retrofit.Builder()
            .baseUrl("http://" + ServerUrlDao.getSavedUrl())
            .client(client)

        retrofit = if (jsonConverter) retrofit.addConverterFactory(GsonConverterFactory.create())
        else retrofit.addConverterFactory(ScalarsConverterFactory.create())

        return retrofit.build()
    }

    fun <T> create(serviceClass: Class<T>, jsonConverter: Boolean = true): T =
        getRetrofit(jsonConverter).create(serviceClass)

    inline fun <reified T> create(): T = create(T::class.java)

}
