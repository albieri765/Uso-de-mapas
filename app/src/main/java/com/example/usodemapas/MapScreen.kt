package com.example.usodemapas

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition

@Composable
fun MapScreen() {
    val ArequipaLocation = LatLng(-16.4040102, -71.559611)
    val context = LocalContext.current

    // Lista de ubicaciones con marcadores
    val locations = listOf(
        Triple(LatLng(-16.433415, -71.5442652), "JLByR", R.drawable.paisaje),
        Triple(LatLng(-16.4205151, -71.4945209), "Paucarpata", R.drawable.paisaje_urbano),
        Triple(LatLng(-16.3524187, -71.5675994), "Zamacola", R.drawable.campo)
    )

    // Polígonos
    val mallAventuraPolygon = listOf(
        LatLng(-16.432292, -71.509145),
        LatLng(-16.432757, -71.509626),
        LatLng(-16.433013, -71.509310),
        LatLng(-16.432566, -71.508853)
    )

    val parqueLambramaniPolygon = listOf(
        LatLng(-16.422704, -71.530830),
        LatLng(-16.422920, -71.531340),
        LatLng(-16.423264, -71.531110),
        LatLng(-16.423050, -71.530600)
    )

    val plazaDeArmasPolygon = listOf(
        LatLng(-16.398866, -71.536961),
        LatLng(-16.398744, -71.536529),
        LatLng(-16.399178, -71.536289),
        LatLng(-16.399299, -71.536721)
    )

    // Polilínea de ejemplo (ruta sencilla)
    val rutaExample = listOf(
        LatLng(-16.4040102, -71.559611),  // Inicio en Arequipa centro
        LatLng(-16.4150, -71.5350),
        LatLng(-16.4205151, -71.4945209)  // Hacia Paucarpata
    )

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(ArequipaLocation, 13.5f)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            // Marcador principal
            Marker(
                state = rememberMarkerState(position = ArequipaLocation),
                icon = BitmapDescriptorFactory.fromResource(R.drawable.montana2d),
                title = "Arequipa, Perú"
            )

            // Marcadores adicionales
            locations.forEach { (location, title, iconRes) ->
                Marker(
                    state = rememberMarkerState(position = location),
                    icon = BitmapDescriptorFactory.fromResource(iconRes),
                    title = title,
                    snippet = "Punto turístico"
                )
            }

            // Polígonos
            Polygon(
                points = plazaDeArmasPolygon,
                strokeColor = Color.Red,
                fillColor = Color.Blue.copy(alpha = 0.3f),
                strokeWidth = 50f,
                clickable = true
            )

            Polygon(
                points = parqueLambramaniPolygon,
                strokeColor = Color.Green,
                fillColor = Color.Magenta.copy(alpha = 0.3f),
                strokeWidth = 50f,
                clickable = true
            )

            Polygon(
                points = mallAventuraPolygon,
                strokeColor = Color.Blue,
                fillColor = Color.Cyan.copy(alpha = 0.3f),
                strokeWidth = 50f,
                clickable = true
            )

            // Polilínea
            Polyline(
                points = rutaExample,
                color = Color.Yellow,
                width = 10f
            )
        }
    }
}
