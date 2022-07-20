package com.bafoor.stockmarketapp.presentation.company_listings

import com.bafoor.stockmarketapp.domain.model.CompanyListing

data class CompanyListingState(
    val companyListings : List<CompanyListing> = emptyList(),
    val isLoading : Boolean = false,
    val isRefreshing : Boolean = false,
    val searchQuery : String = ""
)
