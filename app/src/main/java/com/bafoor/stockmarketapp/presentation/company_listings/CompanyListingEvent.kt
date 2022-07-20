package com.bafoor.stockmarketapp.presentation.company_listings

sealed class CompanyListingEvent {
    object Refreshing : CompanyListingEvent()
    data class OnSearchQueryChange(val searchQuery : String) : CompanyListingEvent()
}