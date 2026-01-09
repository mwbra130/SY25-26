// File: app/src/main/java/com/example/stockapp/presentation/model/StockData.kt
package com.example.stockapp.presentation.model

/**
 * Domain model representing stock data in a clean format for UI consumption.
 * This keeps the app decoupled from raw API DTOs.
 */
data class StockData(
    val symbol: String,
    val price: Double,
    val currency: String,
    val change: Double,
    val changePercent: String,
    val high: Double,
    val low: Double,
    val exchange: String,
    val timestamp: String
) {
    fun isPositiveChange(): Boolean = change >= 0.0
}
