package com.example.beyondlimits.feature_traning.running

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.beyondlimits.feature_map.MapLibreView
import com.example.beyondlimits.feature_map.MapViewModel
import com.example.beyondlimits.ui.theme.ButtonRed
import com.example.beyondlimits.ui.theme.SurfaceDark
import com.google.accompanist.permissions.*

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RunningScreen(vm: RunningViewModel) {
    val isRunning by vm.isRunning
    val distance by vm.distanceKm
    val time by vm.timeSec
    val pace by vm.pace

    val mapVm: MapViewModel = viewModel()
    val userLocation by mapVm.userLocation.collectAsState(initial = null)

    val locationPermission = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    LaunchedEffect(locationPermission.status) {
        if (locationPermission.status.isGranted) {
            mapVm.fetchCurrentLocation()
        } else {
            locationPermission.launchPermissionRequest()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceDark)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // ---------- Titel ----------
        Text(
            "Running Session",
            color = Color.White,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        // ---------- Karte ----------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Black)
        ) {
            if (locationPermission.status.isGranted) {
                MapLibreView(
                    modifier = Modifier.fillMaxSize(),
                    userLocation = userLocation,
                )
            } else {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Location permission required to show map",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                }
            }
        }

        // ---------- Laufdaten ----------
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StatBox("Distance", "%.2f km".format(distance))
            StatBox("Time", formatTime(time))
            StatBox("Pace", pace)
        }

        // ---------- Start/Stop Button ----------
        Button(
            onClick = {
                if (!isRunning) {
                    vm.startRun()  // Tracking mit Location-Flow starten
                } else {
                    vm.stopRun()   // Stop + Speichern
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isRunning) Color.Gray else ButtonRed
            )
        ) {
            Text(
                if (isRunning) "Stop Running" else "Start Running",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun StatBox(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(label, color = Color.White.copy(alpha = 0.6f), fontSize = 13.sp)
    }
}

fun formatTime(seconds: Int): String {
    val mins = seconds / 60
    val secs = seconds % 60
    return String.format("%02d:%02d", mins, secs)
}
