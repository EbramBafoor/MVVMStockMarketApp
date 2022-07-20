package com.bafoor.stockmarketapp.data.csv

import java.io.InputStream

interface CSVParser <T> {

    suspend fun parse(inputStream : InputStream) : List<T>
}