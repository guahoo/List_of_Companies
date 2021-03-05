package com.list_of_companies.network

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


const val BASE_LINK = "https://lifehack.studio/test_task/"

interface ApiServiceCompanyList {

    @GET("test.php")
    fun getCompanyDetailAsync(
            @Query("id") id: String = "2"
    ): Deferred<CompanyDetailRequest>

    @GET("test.php")
    fun getCompanyListAsync(): Deferred<CompaniesListRequest>

    companion object {
        operator fun invoke(
        ): ApiServiceCompanyList {

            return Retrofit.Builder()
                    .client(OkHttpClient.Builder().build())
                    .baseUrl(BASE_LINK)
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
                    .build()
                    .create(ApiServiceCompanyList::class.java)

        }
    }
}