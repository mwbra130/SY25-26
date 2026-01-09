// File: app/src/main/java/com/example/stockapp/presentation/data/StockRepository.kt
package com.example.stockapp.presentation.data

import android.util.Log
import com.example.stockapp.presentation.model.StockData
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://api.insightease.com/"
private const val API_KEY = "7dLM26Tn1trglPo6cwx8fc3EFfSTpu"
private const val TAG_REPO = "StockRepository"

// -------- Retrofit API --------
interface StockApiService {
    @GET("stock/latest")
    suspend fun latestBySymbol(
        @Query("symbol") symbolCsv: String,
        @Query("api_key") apiKey: String = API_KEY,
        @Query("output") output: String = "json"
    ): LatestResponseDto
}

// -------- DTOs (map API to app) --------
data class LatestResponseDto(
    val status: Boolean? = null,
    val code: Int? = null,
    val msg: String? = null,
    val response: List<StockDto>? = null
)

data class StockDto(
    val id: Int? = null,
    val h: Double? = null,
    val l: Double? = null,
    val c: Double? = null,
    val cty: String? = null,
    val ccy: String? = null,
    val exch: String? = null,
    val ch: Double? = null,
    val cp: String? = null,
    val t: Long? = null,
    val s: String? = null,
    val tm: String? = null
)

// -------- Repository --------
class StockRepository(
    private val api: StockApiService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private val _watchlist = MutableStateFlow<List<String>>(emptyList())
    val watchlist: StateFlow<List<String>> = _watchlist.asStateFlow()

    private val _prices = MutableStateFlow<Map<String, StockData>>(emptyMap())
    val prices: StateFlow<Map<String, StockData>> = _prices.asStateFlow()

    private var refreshJob: Job? = null

    fun addToWatchlist(symbol: String) {
        val upper = symbol.trim().uppercase()
        _watchlist.update { list ->
            if (list.contains(upper)) list else list + upper
        }
    }

    fun removeFromWatchlist(symbol: String) {
        val upper = symbol.trim().uppercase()
        _watchlist.update { list -> list.filter { it != upper } }
        _prices.update { map -> map - upper }
    }

    suspend fun fetchSymbolOnce(symbol: String): Result<StockData> {
        return try {
            val dto = api.latestBySymbol(symbol)
            val stock = dto.response?.firstOrNull()
            if (stock != null && stock.s != null) {
                val mapped = stock.toDomain()
                _prices.update { current -> current + (stock.s.uppercase() to mapped) }
                Result.success(mapped)
            } else {
                Result.failure(Exception("No data for $symbol: ${dto.msg ?: "empty response"}"))
            }
        } catch (t: Throwable) {
            Log.e(TAG_REPO, "Fetch failed for $symbol", t)
            Result.failure(t)
        }
    }

    fun startAutoRefresh(intervalMillis: Long = 30_000L) {
        refreshJob?.cancel()
        refreshJob = CoroutineScope(ioDispatcher + SupervisorJob()).launch {
            while (isActive) {
                val symbols = watchlist.first()
                if (symbols.isNotEmpty()) {
                    try {
                        val csv = symbols.joinToString(",")
                        val dto = api.latestBySymbol(csv)
                        val list = dto.response.orEmpty()
                        _prices.update { current ->
                            val updated = current.toMutableMap()
                            for (stock in list) {
                                stock.s?.let { updated[it.uppercase()] = stock.toDomain() }
                            }
                            updated.toMap()
                        }
                    } catch (t: Throwable) {
                        Log.w(TAG_REPO, "Auto-refresh error: ${t.localizedMessage}")
                    }
                }
                delay(intervalMillis)
            }
        }
    }

    fun stopAutoRefresh() {
        refreshJob?.cancel()
        refreshJob = null
    }

    companion object {
        // build a default instance (singleton style)
        fun create(): StockRepository {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            }
            val okHttp = OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttp)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val api = retrofit.create(StockApiService::class.java)
            return StockRepository(api)
        }
    }
}

// -------- Mapper --------
private fun StockDto.toDomain(): StockData {
    return StockData(
        symbol = s ?: "",
        price = c ?: 0.0,
        currency = ccy ?: "",
        change = ch ?: 0.0,
        changePercent = cp ?: "",
        high = h ?: 0.0,
        low = l ?: 0.0,
        exchange = exch ?: "",
        timestamp = tm ?: ""
    )
}
