package com.bafoor.stockmarketapp.di

import android.app.Application
import androidx.room.Room
import com.bafoor.stockmarketapp.data.local.StockDatabase
import com.bafoor.stockmarketapp.data.remote.StockMarketApi
import com.bafoor.stockmarketapp.util.Constant.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideStockApi() : StockMarketApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }).build()
            )
            .build()
            .create()
    }


    @Provides
    @Singleton
    fun provideStockDatabase(app : Application) : StockDatabase {
        return Room.databaseBuilder(
            app,
            StockDatabase :: class.java,
            "stockdb.db"
        )
            .build()
    }

}





















