package com.example.usodemapas

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun MapScreen() {
    // Coordenadas de TECSUP Arequipa
    val tecsupArequipa = LatLng(-16.398803, -71.536018)

    // Estado de la cámara
    val cameraPositionState = rememberCameraPositionState {
        position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(tecsupArequipa, 17f)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            Marker(
                state = MarkerState(position = tecsupArequipa),
                title = "TECSUP Arequipa",
                snippet = "Aquí estás"
            )
        }

        // Opcional: etiqueta superior
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Ubicación: TECSUP Arequipa",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
