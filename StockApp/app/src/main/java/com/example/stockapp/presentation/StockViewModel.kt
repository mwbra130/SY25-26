// File: app/src/main/java/com/example/stockapp/presentation/StockViewModel.kt
package com.example.stockapp.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockapp.presentation.data.StockRepository
import com.example.stockapp.presentation.model.StockData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

private const val TAG_VM = "StockViewModel"

sealed class SearchUiState {
    object Idle : SearchUiState()
    object Loading : SearchUiState()
    data class Success(val stock: StockData) : SearchUiState()
    data class Error(val message: String) : SearchUiState()
}

class StockViewModel(
    private val repo: StockRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    val watchlist: StateFlow<List<String>> = repo.watchlist
    val prices: StateFlow<Map<String, StockData>> = repo.prices

    private val _searchState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val searchState: StateFlow<SearchUiState> = _searchState.asStateFlow()

    init {
        // auto-refresh every 30 seconds by default
        repo.startAutoRefresh()
    }

    fun addToWatchlist(symbol: String) {
        repo.addToWatchlist(symbol)
    }

    fun removeFromWatchlist(symbol: String) {
        repo.removeFromWatchlist(symbol)
    }

    fun searchSymbol(symbol: String) {
        if (symbol.isBlank()) return
        viewModelScope.launch(ioDispatcher) {
            _searchState.value = SearchUiState.Loading
            val result = repo.fetchSymbolOnce(symbol.trim().uppercase())
            _searchState.value = result.fold(
                onSuccess = { SearchUiState.Success(it) },
                onFailure = {
                    Log.e(TAG_VM, "Search failed", it)
                    SearchUiState.Error(it.localizedMessage ?: "Unknown error")
                }
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        repo.stopAutoRefresh()
    }
}
