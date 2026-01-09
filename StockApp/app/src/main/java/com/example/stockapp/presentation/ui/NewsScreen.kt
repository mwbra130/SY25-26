package com.example.stockapp.presentation.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://api.insightease.com/"
private const val API_KEY = "7dLM26Tn1trglPo6cwx8fc3EFfSTpu"
private const val TAG_NEWS = "NewsScreen"

// -------- Retrofit API --------
interface NewsApiService {
    @GET("news/latest")
    suspend fun latestNews(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("output") output: String = "json"
    ): NewsResponseDto
}

// -------- DTOs --------
data class NewsResponseDto(
    val status: Boolean? = null,
    val code: Int? = null,
    val msg: String? = null,
    val response: List<NewsDto>? = null
)

data class NewsDto(
    val id: String? = null,
    val headline: String? = null,
    val summary: String? = null,
    val url: String? = null,
    val published: String? = null
)

// -------- Composable Screen --------
@Composable
fun NewsScreen() {
    var news by remember { mutableStateOf<List<NewsDto>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            isLoading = true
            error = null
            try {
                val service = provideNewsService()
                val response = service.latestNews()
                news = response.response.orEmpty()
            } catch (t: Throwable) {
                Log.e(TAG_NEWS, "News fetch failed", t)
                error = t.localizedMessage ?: "Failed to fetch news"
            } finally {
                isLoading = false
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        Text("Latest News", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))

        when {
            isLoading -> {
                CircularProgressIndicator()
            }
            error != null -> {
                Text("Error: $error", color = MaterialTheme.colors.error)
            }
            news.isEmpty() -> {
                Text("No news available.")
            }
            else -> {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(news) { article ->
                        NewsCard(article)
                    }
                }
            }
        }
    }
}

@Composable
fun NewsCard(article: NewsDto) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(article.headline ?: "Untitled", fontWeight = FontWeight.Bold)
            if (!article.summary.isNullOrBlank()) {
                Text(article.summary, fontSize = 12.sp, maxLines = 2)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Published: ${article.published ?: "N/A"}",
                fontSize = 10.sp
            )
            article.url?.let { url ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Read more",
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.clickable {
                        // Could open browser via Intent (not ideal on watches, but works on phones/tablets)
                        Log.i(TAG_NEWS, "Open URL: $url")
                    },
                    fontSize = 12.sp
                )
            }
        }
    }
}

// -------- Retrofit Builder --------
private fun provideNewsService(): NewsApiService {
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

    return retrofit.create(NewsApiService::class.java)
}
