package com.example.beyondlimits

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.beyondlimits.navigation.AppNav
import com.example.beyondlimits.ui.components.PermissionExplanationScreen
import com.example.beyondlimits.ui.theme.BeyondLimitsTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import org.maplibre.android.MapLibre
import org.maplibre.android.WellKnownTileServer

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()

        // 🔹 MapLibre einmalig initialisieren
        MapLibre.getInstance(
            applicationContext,
            "", // kein Key nötig für OpenSource Tiles
            WellKnownTileServer.MapLibre
        )

        setContent {
            BeyondLimitsTheme {
                val locationPermission = rememberPermissionState(
                    permission = Manifest.permission.ACCESS_FINE_LOCATION
                )

                LaunchedEffect(Unit) {
                    if (!locationPermission.status.isGranted) {
                        locationPermission.launchPermissionRequest()
                    }
                }

                if (locationPermission.status.isGranted) {
                    AppNav()
                } else {
                    PermissionExplanationScreen(locationPermission)
                }
            }
        }
    }
}
