package com.example.beyondlimits.feature_traning.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.beyondlimits.feature_progress.ProgressViewModel
import com.example.beyondlimits.ui.theme.BackgroundDark
import kotlinx.coroutines.delay

@Composable
fun ProgressScreen(vm: ProgressViewModel = viewModel()) {

    val sports = listOf("Running", "Cycling", "Swimming", "Triathlon")
    val timeframes = listOf("Week", "Month", "Year")

    var selectedSport by remember { mutableStateOf("Running") }
    var selectedTimeframe by remember { mutableStateOf("Week") }
    var showChart by remember { mutableStateOf(false) }

    // Dummy data by sport + timeframe
    val chartData = remember(selectedSport to selectedTimeframe) {
        when (selectedTimeframe) {
            "Week" -> listOf(3f, 5f, 7f, 6f, 8f, 10f, 9f)
            "Month" -> List(4) { (4..12).random().toFloat() }  // 4 weeks
            "Year" -> List(12) { (10..50).random().toFloat() } // 12 months
            else -> emptyList()
        }
    }

    val color = when (selectedSport) {
        "Running" -> Color(0xFF43A047)
        "Cycling" -> Color(0xFFFB8C00)
        "Swimming" -> Color(0xFF039BE5)
        "Triathlon" -> Color(0xFF9C27B0)
        else -> Color(0xFF43A047)
    }

    LaunchedEffect(selectedSport, selectedTimeframe) {
        showChart = false
        delay(200)
        showChart = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(Color(0xFF121212), BackgroundDark, Color(0xFF1F1F1F))
                )
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "Your Progress",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium
            )

            // Sport Tabs
            SportTabRow(
                sports = sports,
                selectedSport = selectedSport,
                onSelected = { selectedSport = it }
            )

            // Timeframe Tabs
            TimeframeTabRow(
                timeframes = timeframes,
                selectedTimeframe = selectedTimeframe,
                onSelected = { selectedTimeframe = it }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Chart
            AnimatedVisibility(showChart) {
                BarChart(data = chartData, color = color)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Stats Cards (example)
            StatsGrid(selectedSport, selectedTimeframe, color)

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun SportTabRow(sports: List<String>, selectedSport: String, onSelected: (String) -> Unit) {
    ScrollableTabRow(
        selectedTabIndex = sports.indexOf(selectedSport),
        containerColor = Color.Transparent,
        contentColor = Color.White
    ) {
        sports.forEachIndexed { index, sport ->
            Tab(
                selected = selectedSport == sport,
                onClick = { onSelected(sport) },
                text = {
                    Text(
                        text = sport,
                        color = if (selectedSport == sport) Color.White else Color.Gray
                    )
                }
            )
        }
    }
}

@Composable
fun TimeframeTabRow(timeframes: List<String>, selectedTimeframe: String, onSelected: (String) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        timeframes.forEach { timeframe ->
            val isSelected = selectedTimeframe == timeframe
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isSelected) Color.White.copy(alpha = 0.1f) else Color.Transparent)
                    .clickable { onSelected(timeframe) }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = timeframe,
                    color = if (isSelected) Color.White else Color.Gray,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun BarChart(data: List<Float>, color: Color) {
    val max = data.maxOrNull() ?: 1f
    val animatedValues = data.map { animateFloatAsState(targetValue = it / max) }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(horizontal = 8.dp)
    ) {
        val barWidth = size.width / (data.size * 2)
        data.forEachIndexed { index, _ ->
            val progress = animatedValues[index].value
            val barHeight = progress * size.height

            drawRoundRect(
                brush = Brush.verticalGradient(
                    listOf(color, color.copy(alpha = 0.6f))
                ),
                topLeft = Offset(
                    x = (index * 2 + 1) * barWidth,
                    y = size.height - barHeight
                ),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(8f, 8f)
            )
        }
    }
}

@Composable
fun StatsGrid(sport: String, timeframe: String, color: Color) {
    val stats = when (sport) {
        "Running" -> listOf("Distance" to "42 km", "Time" to "3h 40m", "Pace" to "5:15/km")
        "Cycling" -> listOf("Distance" to "120 km", "Time" to "4h 10m", "Speed" to "29 km/h")
        "Swimming" -> listOf("Distance" to "4.2 km", "Time" to "1h 15m", "Pace" to "1:45/100m")
        "Triathlon" -> listOf("Total" to "180 km", "Duration" to "6h 20m", "Calories" to "3200 kcal")
        else -> emptyList()
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        stats.chunked(2).forEach { row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                row.forEach { (label, value) ->
                    StatCard(label, value, color, Modifier.weight(1f))
                }
                if (row.size == 1) Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun StatCard(title: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(color.copy(alpha = 0.15f))
            .padding(vertical = 12.dp, horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                title,
                color = Color.White.copy(alpha = 0.7f),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                value,
                color = color,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
