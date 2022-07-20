package com.bafoor.stockmarketapp.data.repository

import com.bafoor.stockmarketapp.data.csv.CSVParser
import com.bafoor.stockmarketapp.data.local.StockDatabase
import com.bafoor.stockmarketapp.data.mapper.toCompanyInfo
import com.bafoor.stockmarketapp.data.mapper.toCompanyListing
import com.bafoor.stockmarketapp.data.mapper.toCompanyListingEntity
import com.bafoor.stockmarketapp.data.remote.StockMarketApi
import com.bafoor.stockmarketapp.domain.model.CompanyInfo
import com.bafoor.stockmarketapp.domain.model.CompanyListing
import com.bafoor.stockmarketapp.domain.model.IntraDayInfo
import com.bafoor.stockmarketapp.domain.repository.StockRepository
import com.bafoor.stockmarketapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImpl @Inject constructor(
    private val api: StockMarketApi,
    private val db: StockDatabase,
    private val companyListingsParser: CSVParser<CompanyListing>,
    private val intraDayInfoParser: CSVParser<IntraDayInfo>
) : StockRepository {

    private val dao = db.stockDao


    override suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>> = flow {

        emit(Resource.Loading(true))
        val localListing = dao.searchCompanyListing(query)
        emit(Resource.Success(
            localListing.map { it.toCompanyListing() }
        ))

        val isDbEmpty = localListing.isEmpty() && query.isBlank()
        val shouldJustLoadFromCaching = !isDbEmpty && !fetchFromRemote
        if (shouldJustLoadFromCaching) {
            emit(Resource.Loading(false))
            return@flow
        }
        val remoteListings = try {
            val response = api.getListings()
            companyListingsParser.parse(response.byteStream())
        } catch (e: IOException) {
            e.printStackTrace()
            emit(
                Resource.Error(
                    "Couldn't reach server, please check your  internet connection."
                )
            )
            null
        } catch (e: HttpException) {
            e.printStackTrace()
            emit(
                Resource.Error(
                    "oops, something wrong occurred"
                )
            )
            null
        }
        remoteListings?.let { listings ->
            dao.clearCompanyListing()
            dao.insertCompanyListing(listings.map { it.toCompanyListingEntity() })
            emit(Resource.Success(
                data = dao.searchCompanyListing("")
                    .map { it.toCompanyListing() }
            ))
            emit(Resource.Loading(false))
        }
    }

    override suspend fun getIntraDayInfo(
        symbol: String
    ): Resource<List<IntraDayInfo>> {

        return try {
            val response = api.getIntraDayInfo(symbol)
            val result = intraDayInfoParser.parse(response.byteStream())
            Resource.Success(result)

        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't reach server, please check your  internet connection."
            )
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error(
                message = "oops, something wrong occurred"
            )
        }
    }

    override suspend fun getCompanyInfo(
        symbol: String
    ): Resource<CompanyInfo> {
        return try {
            val result = api.getCompanyInfo(symbol)
            Resource.Success(result.toCompanyInfo())

        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error(
                message = "Couldn't reach server, please check your  internet connection."
            )
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error(
                message = "oops, something wrong occurred"
            )
        }
    }
}



