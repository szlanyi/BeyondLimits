package com.example.beyondlimits.feature_profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.beyondlimits.R
import com.example.beyondlimits.ui.theme.BackgroundDark

@Composable
fun ProfileScreen(vm: ProfileViewModel = viewModel()) {
    val text by vm.text.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(Color(0xFF121212), BackgroundDark, Color(0xFF1C1C1C))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // --- Profile Header ---
            ProfileHeader(
                name = "Robert Szlanyinka",
                motto = "No limits. Just progress.",
                imageRes = R.drawable.triathlon
            )

            // --- Quick Stats ---
            QuickStatsRow(
                stats = listOf(
                    "Level" to "Pro",
                    "Total Time" to "148h",
                    "Calories" to "62.4k",
                    "Distance" to "2,345 km"
                )
            )

            // --- Achievements ---
            AchievementSection()

            // --- About Section ---
            Text(
                text = text,
                color = Color.White.copy(alpha = 0.7f),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        FloatingActionButton(
            onClick = { /* TODO: Edit profile */ },
            containerColor = Color(0xFF43A047),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        ) {
            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.White)
        }
    }
}

@Composable
fun ProfileHeader(name: String, motto: String, imageRes: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(110.dp)
                .clip(CircleShape)
                .shadow(8.dp, CircleShape)
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "Profile picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = name,
            color = Color.White,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = motto,
            color = Color.White.copy(alpha = 0.6f),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun QuickStatsRow(stats: List<Pair<String, String>>) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            stats.chunked(2).first().forEach { (label, value) ->
                StatCard(label, value, Modifier.weight(1f))
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            stats.chunked(2).last().forEach { (label, value) ->
                StatCard(label, value, Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.08f))
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(label, color = Color.White.copy(alpha = 0.6f))
            Text(value, color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun AchievementSection() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            "Achievements",
            color = Color.White,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            AchievementBadge("🏅", "Marathon Finisher")
            AchievementBadge("🔥", "100 Days Active")
            AchievementBadge("💪", "Triathlon Beast")
        }
    }
}

@Composable
fun AchievementBadge(emoji: String, title: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White.copy(alpha = 0.1f))
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text("$emoji  $title", color = Color.White, style = MaterialTheme.typography.bodyMedium)
    }
}
