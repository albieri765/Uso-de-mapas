package com.example.usodemapas

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource

@Composable
fun MapScreen() {
    val context = LocalContext.current
    var currentLocation by remember { mutableStateOf<LatLng?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            fetchCurrentLocation(context) { location, error ->
                location?.let {
                    currentLocation = LatLng(it.latitude, it.longitude)
                    isLoading = false
                }
                error?.let {
                    errorMessage = it
                    isLoading = false
                }
            }
        } else {
            errorMessage = "Permiso de ubicación denegado"
            isLoading = false
        }
    }

    LaunchedEffect(Unit) {
        when {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                if (isLocationEnabled(context)) {
                    fetchCurrentLocation(context) { location, error ->
                        location?.let {
                            currentLocation = LatLng(it.latitude, it.longitude)
                            isLoading = false
                        }
                        error?.let {
                            errorMessage = it
                            isLoading = false
                        }
                    }
                } else {
                    errorMessage = "Por favor, activa el GPS"
                    isLoading = false
                }
            }
            else -> {
                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        when {
            isLoading -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Obteniendo ubicación...")
                }
            }
            errorMessage != null -> {
                Text(
                    text = errorMessage ?: "Error desconocido",
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold
                )
            }
            currentLocation != null -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Ubicación actual:",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Latitud: ${currentLocation!!.latitude}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Longitud: ${currentLocation!!.longitude}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

private fun isLocationEnabled(context: Context): Boolean {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
}

private fun fetchCurrentLocation(
    context: Context,
    callback: (Location?, String?) -> Unit
) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    val cancellationTokenSource = CancellationTokenSource()

    try {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.getCurrentLocation(
                android.location.LocationRequest.QUALITY_HIGH_ACCURACY,
                cancellationTokenSource.token
            ).addOnSuccessListener { location ->
                if (location != null) {
                    Log.d("Location", "Location: ${location.latitude}, ${location.longitude}")
                    callback(location, null)
                } else {
                    callback(null, "No se pudo obtener la ubicación")
                }
            }.addOnFailureListener { exception ->
                callback(null, "Error: ${exception.localizedMessage}")
            }
        } else {
            callback(null, "Permisos no concedidos")
        }
    } catch (e: Exception) {
        callback(null, "Excepción: ${e.localizedMessage}")
    }
}