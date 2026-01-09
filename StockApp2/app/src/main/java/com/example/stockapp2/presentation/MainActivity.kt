package com.example.stockapp2.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stockapp2.presentation.theme.StockWatchTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StockWatchTheme {
                val stockViewModel: StockViewModel = viewModel()
                val uiState by stockViewModel.uiState.collectAsState()
                val coroutineScope = rememberCoroutineScope()

                Scaffold(
                    topBar = {
                        SmallTopAppBar(title = { Text("StockWatch") })
                    }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

                        // Search Bar
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )

                        // Buttons for Navigation
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(onClick = {
                                coroutineScope.launch {
                                    stockViewModel.fetchStock(searchQuery.text)
                                }
                            }) {
                                Text("Search")
                            }

                            Button(onClick = { stockViewModel.loadWatchlist() }) {
                                Text("Watchlist")
                            }

                            Button(onClick = { stockViewModel.loadNews() }) {
                                Text("News")
                            }

                            Button(onClick = { stockViewModel.loadSettings() }) {
                                Text("Settings")
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Display stock data
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(uiState.stocks.size) { index ->
                                val stock = uiState.stocks[index]
                                StockRow(stock.name, stock.price, stock.change)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StockRow(name: String, price: Double, change: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = name)
        Text(text = "$$price")
        Text(text = "${if (change >= 0) "+" else ""}$change%")
    }
}
