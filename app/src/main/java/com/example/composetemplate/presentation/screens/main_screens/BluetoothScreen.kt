package com.example.composetemplate.presentation.screens.main_screens

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.webkit.WebView
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.composetemplate.navigation.Navigator
import com.example.composetemplate.presentation.screens.main_screens.viewmodels.BluetoothViewModel
import java.io.IOException

@Composable
fun BluetoothScreen(
    vm: BluetoothViewModel,
) {
    val permissionToRequest by vm.permissionToRequest.collectAsState()
    permissionToRequest?.let { permission ->
        RequestBluetoothPermissions(permission) {
            vm.onPermissionResult(permissionGranted = true)
        }
    }

    val discoveredDevices = vm.devices.collectAsState(emptyList())
    val connectionStatus by vm.connectionStatus.collectAsState()
    val isDiscovering by vm.isDiscovering.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    val bluetoothReceiver = remember {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                when (intent.action) {
                    BluetoothDevice.ACTION_FOUND -> {
                        val device: BluetoothDevice? =
                            intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                        device?.let { vm.onDeviceDiscovered(it) }
                    }

                    BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                        vm.onDiscoveryStarted()
                    }

                    BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                        vm.onDiscoveryFinished()
                    }
                }
            }
        }
    }

    // Register the receiver with lifecycle awareness
    DisposableEffect(lifecycleOwner) {
        vm.registerBluetoothReceiver(bluetoothReceiver)
        onDispose {
            vm.unregisterBluetoothReceiver(bluetoothReceiver)
            vm.disconnectDevice()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Connection Status
        Text(text = "Connection Status: $connectionStatus", style = MaterialTheme.typography.bodyLarge)

        // Enable Bluetooth Button
        Button(onClick = {
            if (vm.isBluetoothSupported() && !vm.isBluetoothEnabled()) {
                vm.enableBluetooth()
            }
        }) {
            Text("Enable Bluetooth")
        }

        // Discover Devices Button
        Button(
            onClick = { vm.startDiscovery() },
            enabled = !isDiscovering
        ) {
            Text(if (isDiscovering) "Discovering..." else "Discover Devices")
        }

        Button(
            onClick = { vm.disconnectDevice() },
        ) {
            Text("disconnect")
        }

        // Discovered Devices
        Text("Discovered Devices:", style = MaterialTheme.typography.labelLarge)

        // MediaPlayer instance to play audio
        val mediaPlayer = remember { MediaPlayer() }

        Button(
            onClick = {
                // Play audio using MediaPlayer (replace with any audio URL)
                val audioUrl = "http://www.hochmuth.com/mp3/Haydn_Cello_Concerto_D-1.mp3"

                if (vm.isBluetoothEnabled()) {
                    try {
                        mediaPlayer.reset() // Reset MediaPlayer
                        mediaPlayer.setDataSource(audioUrl)  // Set audio source
                        mediaPlayer.prepare()  // Prepare the player
                        mediaPlayer.start()  // Start playing the audio
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        ) {
            Text("Play Audio on Bluetooth")
        }

        LazyColumn {
            items(discoveredDevices.value) { device ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Name: ${device.name ?: "Unknown"}\nAddress: ${device.address}")
                    Button(onClick = { vm.connectToDevice(device) }) {
                        Text("Connect")
                    }
                }
            }
        }
        if (connectionStatus.contains("Connected to")){
//            PlayYouTubeVideo(url = "https://www.youtube.com/watch?v=YMr4WyGrdmc&list=RDJ-q28gVKEcA&index=13")
        }
    }
}

@Composable
private fun RequestBluetoothPermissions(permissions: Array<String>, onGranted: () -> Unit) {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        val allGranted = result.all { it.value }
        if (allGranted) {
            onGranted()
        } else {
            Toast.makeText(context, "Permissions Denied", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        launcher.launch(permissions)
    }
}

@Composable
fun PlayYouTubeVideo(url: String) {
    val context = LocalContext.current
    val webView = remember { WebView(context) }

    AndroidView(
        factory = {
            webView.apply {
                settings.javaScriptEnabled = true
                loadUrl(url)
            }
        }
    )
}