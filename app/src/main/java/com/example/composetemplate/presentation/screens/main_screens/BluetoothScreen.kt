package com.example.composetemplate.presentation.screens.main_screens

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Button
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
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.composetemplate.navigation.Navigator
import com.example.composetemplate.presentation.screens.main_screens.viewmodels.BluetoothViewModel

@Composable
fun BluetoothScreen(
    navigator: Navigator,
    vm: BluetoothViewModel,
) {
    val permissionToRequest by vm.permissionToRequest.collectAsState()
    permissionToRequest?.let { permission ->
        RequestBluetoothPermissions(permission) {
            vm.onPermissionResult(permissionGranted = true)
        }
    }

    val discoveredDevices = remember { mutableStateListOf<BluetoothDevice>() }
    val isDiscovering = remember { mutableStateOf(false) }
    val lifecycleOwner = LocalLifecycleOwner.current

    val bluetoothReceiver = remember {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                when (intent.action) {
                    BluetoothDevice.ACTION_FOUND -> {
                        val device: BluetoothDevice? =
                            intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                        device?.let { discoveredDevices.add(it) }
                    }

                    BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                        isDiscovering.value = true
                    }

                    BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                        isDiscovering.value = false
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
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(onClick = {
            if (vm.isBluetoothSupported() && !vm.isBluetoothEnabled()) {
                vm.enableBluetooth()
            }
        }) {
            Text("Enable Bluetooth")
        }

        Button(
            onClick = {
                vm.startDiscovery()
            },
            enabled = !isDiscovering.value
        ) {
            Text("Discover Devices")
        }

        Text("Discovered Devices:")
        discoveredDevices.forEach { device ->
            BasicText(text = "Name: ${device.name}, Address: ${device.address}")
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