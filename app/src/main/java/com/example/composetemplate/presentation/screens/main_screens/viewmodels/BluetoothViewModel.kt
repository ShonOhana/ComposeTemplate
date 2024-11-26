package com.example.composetemplate.presentation.screens.main_screens.viewmodels

import BluetoothManager
import android.content.BroadcastReceiver
import com.example.composetemplate.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel responsible for managing Bluetooth-related operations and exposing states
 * for use in a Jetpack Compose UI. It interacts with the BluetoothManager to perform
 * Bluetooth operations and handles permission-related flows.
 *
 * @property bluetoothManager An instance of BluetoothManager to handle Bluetooth logic.
 */
class BluetoothViewModel(
    private val bluetoothManager: BluetoothManager
) : BaseViewModel() {

    /**
     * StateFlow to hold the array of permissions that need to be requested.
     * This is observed by the UI to initiate permission requests.
     */
    private val _permissionToRequest = MutableStateFlow<Array<String>?>(null)
    val permissionToRequest = _permissionToRequest.asStateFlow()

    /**
     * Registers a Bluetooth broadcast receiver to handle Bluetooth-related events.
     *
     * @param receiver The BroadcastReceiver to register.
     */
    fun registerBluetoothReceiver(receiver: BroadcastReceiver) = bluetoothManager.registerReceiver(receiver)

    /**
     * Unregisters a previously registered Bluetooth broadcast receiver.
     *
     * @param receiver The BroadcastReceiver to unregister.
     */
    fun unregisterBluetoothReceiver(receiver: BroadcastReceiver) = bluetoothManager.unregisterReceiver(receiver)

    /**
     * Checks if Bluetooth is supported on the current device.
     *
     * @return True if the device supports Bluetooth, false otherwise.
     */
    fun isBluetoothSupported() = bluetoothManager.isBluetoothSupported()

    /**
     * Checks if Bluetooth is currently enabled on the device.
     *
     * @return True if Bluetooth is enabled, false otherwise.
     */
    fun isBluetoothEnabled() = bluetoothManager.isBluetoothEnabled()

    /**
     * Attempts to enable Bluetooth on the device. If permissions are needed,
     * it notifies the UI through [_permissionToRequest].
     */
    fun enableBluetooth() {
        bluetoothManager.enableBluetooth { permissionNeeded ->
            _permissionToRequest.value = permissionNeeded
        }
    }

    /**
     * Initiates the Bluetooth device discovery process. If permissions are needed,
     * it notifies the UI through [_permissionToRequest].
     */
    fun startDiscovery() {
        bluetoothManager.startDiscovery { permissionNeeded ->
            // Notify the UI to request permission
            _permissionToRequest.value = permissionNeeded
        }
    }

    /**
     * Handles the result of a permission request. If permissions were granted,
     * it retries the Bluetooth operation (e.g., device discovery).
     *
     * @param permissionGranted True if the required permissions were granted, false otherwise.
     */
    fun onPermissionResult(permissionGranted: Boolean) {
        _permissionToRequest.value = null
        if (permissionGranted) {
            // Retry starting discovery after permission is granted
            startDiscovery()
        }
    }
}