package com.bafoor.stockmarketapp.data.remote


import com.bafoor.stockmarketapp.data.remote.dto.CompanyInfoDto
import com.bafoor.stockmarketapp.util.Constant.API_KEY
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface StockMarketApi {

    @GET("query?function=LISTING_STATUS")
    suspend fun getListings(
        @Query("apikey") apikey : String = API_KEY
    ) : ResponseBody

    @GET("query?function=TIME_SERIES_INTRADAY&interval=60min&datatype=csv")
    suspend fun getIntraDayInfo(
        @Query("symbol") symbol : String,
        @Query("apikey") apikey : String = API_KEY
    ) : ResponseBody

    @GET("query?function=OVERVIEW")
    suspend fun getCompanyInfo(
        @Query("symbol") symbol : String,
        @Query("apikey") apikey : String = API_KEY
    ) : CompanyInfoDto

}

















