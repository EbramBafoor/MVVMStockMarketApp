package com.bafoor.stockmarketapp.data.csv

import com.bafoor.stockmarketapp.data.mapper.toIntraDayInfo
import com.bafoor.stockmarketapp.data.remote.dto.CompanyInfoDto
import com.bafoor.stockmarketapp.data.remote.dto.IntraDayInfoDto
import com.bafoor.stockmarketapp.domain.model.CompanyInfo
import com.bafoor.stockmarketapp.domain.model.CompanyListing
import com.bafoor.stockmarketapp.domain.model.IntraDayInfo
import com.opencsv.CSVReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.InputStreamReader
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IntraDayParser @Inject constructor() : CSVParser<IntraDayInfo> {


    override suspend fun parse(inputStream: InputStream): List<IntraDayInfo> {
        val csvReader = CSVReader(InputStreamReader(inputStream))
        return withContext(Dispatchers.IO) {
            csvReader
                .readAll()
                .drop(1) // <-- first row of csv file describe what column contain
                .mapNotNull { line ->
                    val timestamp = line.getOrNull(0) ?: return@mapNotNull null
                    val close = line.getOrNull(4) ?: return@mapNotNull null
                    val dto = IntraDayInfoDto(timestamp, close.toDouble())
                    dto.toIntraDayInfo()
                }
                .filter {

                    it.date.dayOfMonth == LocalDateTime.now().minusDays(4).dayOfMonth
                }
                .sortedBy {
                    it.date.hour
                }
                .also {
                    csvReader.close()
                }
        }
    }
}
