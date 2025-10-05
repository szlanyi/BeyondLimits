package com.example.beyondlimits.feature_map

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.beyondlimits.R
import org.maplibre.android.MapLibre
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapView
import org.maplibre.android.style.layers.PropertyFactory.iconAllowOverlap
import org.maplibre.android.style.layers.PropertyFactory.iconImage
import org.maplibre.android.style.layers.PropertyFactory.iconSize
import org.maplibre.android.style.layers.SymbolLayer
import org.maplibre.android.style.sources.GeoJsonSource
import org.maplibre.geojson.Feature
import org.maplibre.geojson.FeatureCollection
import org.maplibre.geojson.Point

@SuppressLint("MissingPermission")
@Composable
fun MapLibreView(
    modifier: Modifier = Modifier,
    userLocation: LatLng? = null, // <-- optionaler Parameter
    defaultLocation: LatLng = LatLng(48.2082, 16.3738)
) {
    val context = LocalContext.current
    val location = userLocation ?: defaultLocation

    AndroidView(
        modifier = modifier,
        factory = {
            MapLibre.getInstance(it)
            MapView(it).apply {
                getMapAsync { map ->
                    map.setStyle("https://raw.githubusercontent.com/go2garret/maps/main/src/assets/json/openStreetMap.json") { style ->
                        val position = CameraPosition.Builder()
                            .target(location)
                            .zoom(18.0)
                            .build()
                        map.cameraPosition = position

                        val point = Point.fromLngLat(location.longitude, location.latitude)
                        val feature = Feature.fromGeometry(point)
                        val source = GeoJsonSource(
                            "user-location-source",
                            FeatureCollection.fromFeature(feature)
                        )
                        style.addSource(source)

                        style.addImage(
                            "user-marker-icon",
                            BitmapFactory.decodeResource(
                                context.resources,
                                R.drawable.location,
                            )
                        )

                        val layer: SymbolLayer = SymbolLayer("user-marker-layer", "user-location-source")
                            .withProperties(
                                iconImage("user-marker-icon"),
                                iconAllowOverlap(true),
                                iconSize(0.05f),
                                )
                        style.addLayer(layer)
                    }
                }
            }
        },
        onRelease = { it.onStop() }
    )
}