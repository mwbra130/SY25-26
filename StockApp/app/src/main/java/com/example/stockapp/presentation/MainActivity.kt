// File: app/src/main/java/com/example/stockwatchapp/presentation/MainActivity.kt
package com.example.stockapp.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import okhttp3.*
import java.util.concurrent.TimeUnit
import com.google.gson.annotations.SerializedName
import androidx.compose.material.*
import androidx.wear.compose.material.*
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextOverflow
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

// ----- CONFIG -----
private const val INSIGHTEASE_BASE = "https://api.insightease.com/"
private const val INSIGHTEASE_API_KEY = "7dLM26Tn1trglPo6cwx8fc3EFfSTpu" // provided by user
private const val TAG_MAIN = "MainActivity"

// ----- Retrofit API definitions (minimal, robust) -----
interface InsightEaseStockApi {
    // GET https://api.insightease.com/stock/latest?symbol=AAPL&api_key=KEY
    @GET("stock/latest")
    suspend fun latestBySymbol(
        @Query("symbol") symbolCsv: String,
        @Query("api_key") apiKey: String = INSIGHTEASE_API_KEY,
        @Query("output") output: String = "json"
    ): LatestResponseDto
}

// Data transfer objects (only what's needed; tolerant)
data class LatestResponseDto(
    val status: Boolean? = null,
    val code: Int? = null,
    val msg: String? = null,
    val response: List<StockDto>? = null,
    val info: Map<String, Any>? = null
)

data class StockDto(
    val id: Int? = null,
    val h: Double? = null,
    val l: Double? = null,
    val c: Double? = null,        // close/current price
    val cty: String? = null,
    val ccy: String? = null,
    val exch: String? = null,
    val ch: Double? = null,       // change
    val cp: String? = null,       // change percent like "+0.58%"
    val t: Long? = null,          // unix timestamp
    val s: String? = null,        // symbol
    val tm: String? = null        // timestamp string
)

// ----- Simple, local repository with auto-refresh -----
class StockRepository(
    private val api: InsightEaseStockApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private val _watchlist = MutableStateFlow<List<String>>(emptyList()) // symbols
    val watchlist: StateFlow<List<String>> = _watchlist.asStateFlow()

    private val _prices = MutableStateFlow<Map<String, StockDto>>(emptyMap())
    val prices: StateFlow<Map<String, StockDto>> = _prices.asStateFlow()

    private var refreshJob: Job? = null

    fun addToWatchlist(symbol: String) {
        val upper = symbol.trim().uppercase()
        _watchlist.update { existing ->
            if (existing.contains(upper)) existing else (existing + upper)
        }
    }

    fun removeFromWatchlist(symbol: String) {
        val upper = symbol.trim().uppercase()
        _watchlist.update { existing -> existing.filter { it != upper } }
        _prices.update { current -> current - upper }
    }

    suspend fun fetchSymbolOnce(symbol: String): Result<StockDto> {
        return try {
            val dto = api.latestBySymbol(symbol)
            val stock = dto.response?.firstOrNull()
            if (stock != null) {
                // update cache
                _prices.update { map -> map + (stock.s?.uppercase() ?: symbol.uppercase() to stock) }
                Result.success(stock)
            } else {
                Result.failure(Exception("Empty response for $symbol: ${dto.msg ?: "no data"}"))
            }
        } catch (t: Throwable) {
            Result.failure(t)
        }
    }

    fun startAutoRefresh(everyMillis: Long = 30_000L) {
        refreshJob?.cancel()
        refreshJob = CoroutineScope(ioDispatcher + SupervisorJob()).launch {
            while (isActive) {
                // take snapshot of watchlist and fetch in parallel
                val symbols = watchlist.first()
                if (symbols.isNotEmpty()) {
                    try {
                        val csv = symbols.joinToString(",")
                        val dto = api.latestBySymbol(csv)
                        val list = dto.response ?: emptyList()
                        _prices.update { current ->
                            val mutated = current.toMutableMap()
                            for (s in list) {
                                s.s?.let { mutated[it.uppercase()] = s }
                            }
                            mutated.toMap()
                        }
                    } catch (t: Throwable) {
                        Log.w(TAG_MAIN, "Auto-refresh failed: ${t.localizedMessage}")
                    }
                }
                delay(everyMillis)
            }
        }
    }

    fun stopAutoRefresh() {
        refreshJob?.cancel()
        refreshJob = null
    }
}

// ----- ViewModel -----
class StockMainViewModel(private val repo: StockRepository) : ViewModel() {

    val watchlist: StateFlow<List<String>> = repo.watchlist
    val prices: StateFlow<Map<String, StockDto>> = repo.prices

    // search UI state
    private val _searchState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val searchState: StateFlow<SearchUiState> = _searchState.asStateFlow()

    init {
        // start background refresh by default, 30s (InsightEase updates ~30s)
        repo.startAutoRefresh(30_000L)
    }

    fun addToWatchlist(symbol: String) {
        repo.addToWatchlist(symbol)
    }

    fun removeFromWatchlist(symbol: String) {
        repo.removeFromWatchlist(symbol)
    }

    fun searchSymbol(symbol: String) {
        if (symbol.isBlank()) return
        viewModelScope.launch {
            _searchState.value = SearchUiState.Loading
            val r = repo.fetchSymbolOnce(symbol.trim().uppercase())
            _searchState.value = r.fold(
                onSuccess = { SearchUiState.Success(it) },
                onFailure = { SearchUiState.Error(it.localizedMessage ?: "Unknown error") }
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        repo.stopAutoRefresh()
    }
}

sealed class SearchUiState {
    object Idle : SearchUiState()
    object Loading : SearchUiState()
    data class Success(val stock: StockDto) : SearchUiState()
    data class Error(val message: String) : SearchUiState()
}

// ----- Factory for ViewModel (wire up Retrofit & repository here so single-file works) -----
class StockMainViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // build Retrofit with an interceptor for resiliency and timeouts
        val ok = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .callTimeout(30, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                // defensive: ensure requests include api_key if not provided
                val req = chain.request()
                val url = req.url.newBuilder()
                    // don't overwrite if already present
                    .also { b ->
                        if (req.url.queryParameter("api_key") == null) {
                            b.addQueryParameter("api_key", INSIGHTEASE_API_KEY)
                        }
                    }
                    .build()
                chain.proceed(req.newBuilder().url(url).build())
            }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(INSIGHTEASE_BASE)
            .client(ok)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(InsightEaseStockApi::class.java)
        val repo = StockRepository(api, Dispatchers.IO)

        @Suppress("UNCHECKED_CAST")
        return StockMainViewModel(repo) as T
    }
}

// ----- Compose UI - Wear-friendly -----
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set immersive mode for watches often desired; keep simple here
        setContent {
            // Provide a lifecycle-aware VM instantiation
            val viewModel: StockMainViewModel = viewModel(factory = StockMainViewModelFactory())

            MaterialTheme {
                WearAppScaffold(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun WearAppScaffold(viewModel: StockMainViewModel) {
    val lifecycleOwner = LocalLifecycleOwner.current

    var selectedTab by remember { mutableStateOf(MainTab.Search) }

    // observe watchlist/prices
    val watchlist by viewModel.watchlist.collectAsState()
    val prices by viewModel.prices.collectAsState()

    // simple top-level layout: bottom nav + content
    Scaffold(
        timeText = { TimeText() },
        vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) },
        positionIndicator = { /* no position indicator */ }
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(6.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Minimal header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("StockWatch", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text(selectedTab.title, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.height(6.dp))

                Crossfade(targetState = selectedTab) { tab ->
                    when (tab) {
                        MainTab.Search -> SearchScreen(viewModel)
                        MainTab.News -> NewsScreen()
                        MainTab.Watchlist -> WatchlistScreen(
                            watchlist = watchlist,
                            prices = prices,
                            onRemove = { viewModel.removeFromWatchlist(it) }
                        )
                        MainTab.Settings -> SettingsScreen()
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
                // bottom nav for watch (chip style)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    MainTab.values().forEach { t ->
                        Chip(
                            onClick = { selectedTab = t },
                            label = { Text(t.title) },
                            icon = {
                                when (t) {
                                    MainTab.Search -> Icon(imageVector = Icons.Default.Search, contentDescription = null)
                                    MainTab.News -> Icon(imageVector = Icons.Default.Article, contentDescription = null)
                                    MainTab.Watchlist -> Icon(imageVector = Icons.Default.List, contentDescription = null)
                                    MainTab.Settings -> Icon(imageVector = Icons.Default.Settings, contentDescription = null)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

enum class MainTab(val title: String) {
    Search("Search"),
    News("News"),
    Watchlist("Watchlist"),
    Settings("Settings")
}

@Composable
fun SearchScreen(viewModel: StockMainViewModel) {
    var query by remember { mutableStateOf("") }
    val searchState by viewModel.searchState.collectAsState()

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Symbol (e.g. AAPL)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row {
            Button(onClick = {
                viewModel.searchSymbol(query)
            }) {
                Text("Search")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                if (query.isNotBlank()) viewModel.addToWatchlist(query)
            }) {
                Text("Add to Watchlist")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        when (searchState) {
            is SearchUiState.Idle -> Text("Enter a symbol and press Search.")
            is SearchUiState.Loading -> CircularProgressIndicator(modifier = Modifier.size(24.dp))
            is SearchUiState.Error -> Text("Error: ${(searchState as SearchUiState.Error).message}", color = MaterialTheme.colors.error)
            is SearchUiState.Success -> {
                val s = (searchState as SearchUiState.Success).stock
                SearchResultCard(s) {
                    viewModel.addToWatchlist(it)
                }
            }
        }
    }
}

@Composable
fun SearchResultCard(stock: StockDto, onAdd: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { /* expand on future versions */ },
        elevation = 6.dp
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(stock.s ?: "UNKNOWN", fontWeight = FontWeight.Bold)
            Text("Price: ${stock.c ?: "N/A"} ${stock.ccy ?: ""}", maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text("Change: ${stock.ch ?: 0.0} (${stock.cp ?: ""})")
            Spacer(modifier = Modifier.height(6.dp))
            Row {
                Button(onClick = { stock.s?.let { onAdd(it) } }) { Text("Add to Watchlist") }
                Spacer(modifier = Modifier.width(8.dp))
                Text("Updated: ${stock.tm ?: "N/A"}", modifier = Modifier.align(Alignment.CenterVertically))
            }
        }
    }
}

@Composable
fun WatchlistScreen(
    watchlist: List<String>,
    prices: Map<String, StockDto>,
    onRemove: (String) -> Unit
) {
    if (watchlist.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No symbols in watchlist.\nAdd them from Search.", textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        }
        return
    }

    Column {
        for (symbol in watchlist) {
            val dto = prices[symbol]
            WatchlistRow(symbol = symbol, dto = dto, onRemove = onRemove)
            Divider()
        }
    }
}

@Composable
fun WatchlistRow(symbol: String, dto: StockDto?, onRemove: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(symbol, fontWeight = FontWeight.Bold)
            Text("Price: ${dto?.c ?: "—"} ${dto?.ccy ?: ""}", fontSize = 12.sp)
            Text("Chg: ${dto?.ch ?: "—"} (${dto?.cp ?: "—"})", fontSize = 11.sp)
        }
        Column(horizontalAlignment = Alignment.End) {
            Button(onClick = { onRemove(symbol) }) {
                Text("Remove")
            }
            dto?.tm?.let {
                Text(it, fontSize = 10.sp)
            }
        }
    }
}

@Composable
fun NewsScreen() {
    // Placeholder: insight-ease also provides a News API. We'll show a placeholder and a note.
    Column(modifier = Modifier.fillMaxSize().padding(6.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("News - live news integration is ready.", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(6.dp))
        Text("Future: pull from InsightEase /news endpoints and render headlines with " +
                "click-to-open details. (Not implemented in this single file pass.)", fontSize = 12.sp)
    }
}

@Composable
fun SettingsScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(6.dp)) {
        Text("Settings", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(6.dp))
        Text("• Auto-refresh interval: 30s (default)")
        Text("• API: InsightEase (configurable in future file)")
        Spacer(modifier = Modifier.height(8.dp))
        Text("Note: API key is currently baked into network layer for demo purposes. For production put it in secure storage.", fontSize = 11.sp)
    }
}

// Simple icons used in chips (bring Compose Material icons)
object Icons {
    object Default {
        @Composable
        fun Search() = androidx.compose.material.icons.Icons.Default.Search
        @Composable
        fun Article() = androidx.compose.material.icons.Icons.Default.Description
        @Composable
        fun List() = androidx.compose.material.icons.Icons.Default.List
        @Composable
        fun Settings() = androidx.compose.material.icons.Icons.Default.Settings
    }
}