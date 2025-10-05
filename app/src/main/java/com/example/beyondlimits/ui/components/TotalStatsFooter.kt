package com.example.beyondlimits.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beyondlimits.ui.theme.ButtonRed

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TotalStatsFooter() {
    val sports = listOf("Running", "Cycling", "Swimming", "Triathlon")
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { sports.size })

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(16.dp))
            .background(
                brush = Brush.horizontalGradient(
                    listOf(ButtonRed.copy(alpha = 0.9f), ButtonRed.copy(alpha = 0.6f))
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 20.dp, vertical = 16.dp) // etwas kompakter
    ) {
        // 🔹 Header + Dots in einer Zeile = Platz sparen
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Total Progress",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )

            // 🔹 Zeige aktuelle Sportart + Page Index
            Text(
                text = "${sports[pagerState.currentPage]} (${pagerState.currentPage + 1}/${sports.size})",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )

            // 🔹 Dots-Indicator
            Row {
                repeat(sports.size) { index ->
                    val color =
                        if (pagerState.currentPage == index) Color.White else Color.White.copy(alpha = 0.4f)
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 2.dp)
                            .height(5.dp)
                            .width(if (pagerState.currentPage == index) 16.dp else 8.dp)
                            .clip(RoundedCornerShape(50))
                            .background(color)
                    )
                }
            }
        }


        Spacer(modifier = Modifier.height(8.dp))

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            val sport = sports[page]
            val (km, workouts, pace, progress) = when (sport) {
                "Running" -> listOf("132", "24", "5:42", "0.65f")
                "Cycling" -> listOf("210", "8", "28 km/h", "0.48f")
                "Swimming" -> listOf("3.2", "5", "2:05 / 100 m", "0.72f")
                "Triathlon" -> listOf("345", "12", "–", "0.55f")
                else -> listOf("0", "0", "-", "0f")
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    StatItemCompact(label = "Total KM", value = km)
                    StatItemCompact(label = "Workouts", value = workouts)
                    StatItemCompact(label = "Avg Pace", value = pace)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 🔸 Progress Bar und Text enger kombiniert
                LinearProgressIndicator(
                progress = { progress.toFloat() },
                modifier = Modifier
                                        .fillMaxWidth()
                                        .height(5.dp)
                                        .clip(RoundedCornerShape(50)),
                color = Color.White,
                trackColor = Color.White.copy(alpha = 0.3f),
                strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
                )

                Text(
                    text = "${(progress.toFloat() * 100).toInt()}% of goal",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
fun StatItemCompact(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            color = Color.White,
            fontSize = 15.sp, // kleiner
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 11.sp
        )
    }
}

