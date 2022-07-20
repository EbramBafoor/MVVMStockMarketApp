package com.bafoor.stockmarketapp.presentation.company_info

import com.bafoor.stockmarketapp.domain.model.CompanyInfo
import com.bafoor.stockmarketapp.domain.model.IntraDayInfo

data class CompanyInfoState(
    val companyStock : List<IntraDayInfo> = emptyList(),
    val companyInfo : CompanyInfo? = null,
    val isLoading : Boolean = false,
    val error : String? = null
)
