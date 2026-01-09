package com.example.stockapp.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stockapp.presentation.SearchUiState
import com.example.stockapp.presentation.StockViewModel
import com.example.stockapp.presentation.model.StockData

@Composable
fun SearchScreen(viewModel: StockViewModel) {
    var query by remember { mutableStateOf("") }
    val searchState by viewModel.searchState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Symbol (e.g. AAPL)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Button(
                onClick = { viewModel.searchSymbol(query) },
                enabled = query.isNotBlank()
            ) {
                Text("Search")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { viewModel.addToWatchlist(query) },
                enabled = query.isNotBlank()
            ) {
                Text("Add")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        when (searchState) {
            is SearchUiState.Idle -> Text("Enter a stock symbol to begin.")
            is SearchUiState.Loading -> CircularProgressIndicator()
            is SearchUiState.Error -> Text(
                "Error: ${(searchState as SearchUiState.Error).message}",
                color = MaterialTheme.colors.error
            )
            is SearchUiState.Success -> {
                val stock = (searchState as SearchUiState.Success).stock
                SearchResultCard(stock) {
                    viewModel.addToWatchlist(it)
                }
            }
        }
    }
}

@Composable
fun SearchResultCard(stock: StockData, onAdd: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { /* Expand details later */ },
        elevation = 6.dp
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(stock.symbol, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(
                "Price: ${stock.price} ${stock.currency}",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text("Change: ${stock.change} (${stock.changePercent})")
            Spacer(modifier = Modifier.height(6.dp))
            Button(onClick = { onAdd(stock.symbol) }) {
                Text("Add to Watchlist")
            }
        }
    }
}
