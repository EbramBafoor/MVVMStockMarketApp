package com.bafoor.stockmarketapp.data.mapper

import com.bafoor.stockmarketapp.data.local.CompanyListingEntity
import com.bafoor.stockmarketapp.data.remote.dto.CompanyInfoDto
import com.bafoor.stockmarketapp.domain.model.CompanyInfo
import com.bafoor.stockmarketapp.domain.model.CompanyListing

fun CompanyListingEntity.toCompanyListing() : CompanyListing {

    return CompanyListing(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}

fun CompanyListing.toCompanyListingEntity() : CompanyListingEntity {
    return CompanyListingEntity(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}


fun CompanyInfoDto.toCompanyInfo() : CompanyInfo {

    return CompanyInfo(
        symbol = symbol,
        description = description,
        name = name,
        country = country,
        industry = industry

    )
}


