package com.example.stockapp.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stockapp.presentation.model.StockData

@Composable
fun WatchlistScreen(
    watchlist: List<String>,
    prices: Map<String, StockData>,
    onRemove: (String) -> Unit
) {
    if (watchlist.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "No symbols in watchlist.\nAdd some from Search.",
                fontSize = 14.sp
            )
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(watchlist) { symbol ->
            val stock = prices[symbol]
            WatchlistRow(symbol = symbol, data = stock, onRemove = onRemove)
        }
    }
}

@Composable
fun WatchlistRow(symbol: String, data: StockData?, onRemove: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(symbol, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("Price: ${data?.price ?: "—"} ${data?.currency ?: ""}", fontSize = 13.sp)
                Text("Change: ${data?.change ?: "—"} (${data?.changePercent ?: "—"})", fontSize = 12.sp)
                Text("Updated: ${data?.timestamp ?: "—"}", fontSize = 11.sp)
            }
            Button(
                onClick = { onRemove(symbol) },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("Remove")
            }
        }
    }
}
