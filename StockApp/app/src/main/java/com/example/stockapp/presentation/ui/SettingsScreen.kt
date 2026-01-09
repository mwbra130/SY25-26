package com.example.stockapp.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsScreen() {
    var refreshInterval by remember { mutableStateOf("30") } // seconds
    var apiKey by remember { mutableStateOf("7dLM26Tn1trglPo6cwx8fc3EFfSTpu") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Text("Settings", fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        Text("Auto-refresh interval (seconds):", fontSize = 14.sp)
        OutlinedTextField(
            value = refreshInterval,
            onValueChange = { refreshInterval = it.filter { c -> c.isDigit() } },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text("API Key (currently in use):", fontSize = 14.sp)
        OutlinedTextField(
            value = apiKey,
            onValueChange = { apiKey = it },
            singleLine = true,
            readOnly = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            "Note: Changing API key requires app restart. Refresh interval changes take effect immediately in future versions.",
            fontSize = 12.sp
        )
    }
}
