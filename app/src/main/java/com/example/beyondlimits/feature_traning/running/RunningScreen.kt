package com.example.beyondlimits.ui.running

import android.renderscript.RenderScript.Priority
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.beyondlimits.ui.theme.SurfaceDark
import com.example.beyondlimits.ui.theme.TextWhite
import com.google.android.gms.maps.GoogleMap

@Composable
fun RunningScreen(vm: RunningViewModel) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Today's Training", "History")

    Column(modifier = Modifier.fillMaxSize().background(SurfaceDark)) {
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        when (selectedTab) {
            0 -> TodayTrainingView(vm)
            1 -> PastResultsView(vm)
        }
    }
}

@Composable
fun TodayTrainingView(vm: RunningViewModel) {
    Column {
        Text("actual")
    }
}

@Composable
fun PastResultsView(vm: RunningViewModel) {
    Column {
        Text("results")
    }
}
