package com.bafoor.stockmarketapp.presentation.company_info

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bafoor.stockmarketapp.domain.repository.StockRepository
import com.bafoor.stockmarketapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyInfoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: StockRepository
) : ViewModel() {


    var state by mutableStateOf(CompanyInfoState())


    init {
        viewModelScope.launch {
            val symbol = savedStateHandle.get<String>("symbol") ?: return@launch
            state = state.copy(isLoading = true)
            val companyInfoResult = async { repository.getCompanyInfo(symbol) }
            val intraDayInfo = async { repository.getIntraDayInfo(symbol) }
            when (val result = companyInfoResult.await()) {
                is Resource.Success -> {
                    state = state.copy(
                        companyInfo = result.data,
                        isLoading = false,
                        error = null
                    )
                }
                is Resource.Error -> {
                    state = state.copy(
                        error = result.message,
                        isLoading = false,
                        companyInfo = null
                    )
                }

                is Resource.Loading -> Unit

            }
            when (val result = intraDayInfo.await()) {
                is Resource.Success -> {
                    state = state.copy(
                        companyStock = result.data ?: emptyList(),
                        isLoading = false,
                        error = null
                    )
                }
                is Resource.Error -> {
                    state = state.copy(
                        error = result.message,
                        isLoading = false,
                        companyStock = emptyList()
                    )
                }

                is Resource.Loading -> Unit

            }
        }
    }
}




















