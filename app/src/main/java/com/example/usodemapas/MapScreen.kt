package com.example.usodemapas

import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun MapScreen() {
    // Configuración inicial del mapa (Ciudad de México)
    val arequipa = LatLng(-16.4090, -71.5375)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(arequipa, 12f)
    }

    // Estado para el tipo de mapa
    var mapType by remember { mutableStateOf(MapType.NORMAL) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Selector de tipos de mapa
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            MapTypeButton("Normal", MapType.NORMAL, mapType) { mapType = it }
            MapTypeButton("Satélite", MapType.SATELLITE, mapType) { mapType = it }
            MapTypeButton("Híbrido", MapType.HYBRID, mapType) { mapType = it }
            MapTypeButton("Terreno", MapType.TERRAIN, mapType) { mapType = it }
        }

        // El mapa en sí
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(mapType = mapType)
        )
    }
}

@Composable
fun MapTypeButton(
    text: String,
    type: MapType,
    currentType: MapType,
    onClick: (MapType) -> Unit
) {
    Button(
        onClick = { onClick(type) },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (type == currentType) Color.Blue else Color.LightGray
        ),
        modifier = Modifier.padding(4.dp)
    ) {
        Text(text)
    }
}