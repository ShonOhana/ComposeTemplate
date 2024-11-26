import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity

class BluetoothManager(private val context: Context) {

    private val bluetoothManager: BluetoothManager =
        context.getSystemService(BluetoothManager::class.java)
    private val bluetoothAdapter = bluetoothManager.adapter

    /**
     * Check if Bluetooth is supported on the device.
     * @return True if Bluetooth is supported, false otherwise.
     */
    fun isBluetoothSupported(): Boolean {
        return bluetoothAdapter != null
    }

    /**
     * Check if Bluetooth is enabled.
     * @return True if Bluetooth is enabled, false otherwise.
     */
    fun isBluetoothEnabled(): Boolean {
        return bluetoothAdapter?.isEnabled == true
    }

    /**
     * Start Bluetooth discovery.
     * @return True if discovery started successfully, false otherwise.
     */
    fun startDiscovery(onPermissionNeeded: (Array<String>) -> Unit) {
        val permissionList = getBluetoothPermissionsToRequest()
        if (permissionList.isNotEmpty()) {
            onPermissionNeeded(permissionList)
            return
        }
        // TODO: if false notice the bluetooth is off
        bluetoothAdapter?.startDiscovery()
    }

    /**
     * Cancel Bluetooth discovery.
     * @return True if discovery was successfully canceled, false otherwise.
     */
    fun cancelDiscovery(): Boolean {
        return bluetoothAdapter?.cancelDiscovery() ?: false
    }

    /**
     * Enables Bluetooth if supported and not already enabled.
     *
     * This method checks if Bluetooth is supported on the device. If Bluetooth is not enabled,
     * it prompts the user to enable it using the `BluetoothAdapter.ACTION_REQUEST_ENABLE` intent.
     *
     * Before initiating the intent, it verifies that the necessary permissions are granted.
     * If permissions are missing, it invokes the `onPermissionNeeded` callback with the required
     * permissions for the caller to request.
     *
     * @param onPermissionNeeded A callback invoked with the list of permissions that need to be requested.
     */
    fun enableBluetooth(onPermissionNeeded: (Array<String>) -> Unit) {
        bluetoothAdapter?.takeIf { !it.isEnabled }?.apply {
            val permissionList = getBluetoothPermissionsToRequest()
            if (permissionList.isNotEmpty()) {
                onPermissionNeeded(permissionList)
                return@apply
            }
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            enableBtIntent.addFlags(FLAG_ACTIVITY_NEW_TASK)
            startActivity(context, enableBtIntent, null)
        }
    }

    /**
     * Get the list of currently paired devices.
     * @return A set of paired Bluetooth devices.
     */
    fun getPairedDevices(): Set<BluetoothDevice> {
        return bluetoothAdapter?.bondedDevices ?: emptySet()
    }

    /**
     * Registers a broadcast receiver for Bluetooth-related events.
     *
     * This method sets up a `BroadcastReceiver` to listen for the following Bluetooth events:
     * - `BluetoothDevice.ACTION_FOUND`: Triggered when a new Bluetooth device is discovered during scanning.
     * - `BluetoothAdapter.ACTION_DISCOVERY_STARTED`: Triggered when the Bluetooth adapter starts discovery mode.
     * - `BluetoothAdapter.ACTION_DISCOVERY_FINISHED`: Triggered when the Bluetooth adapter finishes discovery mode.
     *
     * Ensure that the appropriate permissions (e.g., `BLUETOOTH_SCAN`) are granted before calling this method.
     *
     * @param receiver The `BroadcastReceiver` instance to register.
     */
    fun registerReceiver(receiver: BroadcastReceiver) {
        val intentFilter = IntentFilter().apply {
            addAction(BluetoothDevice.ACTION_FOUND)
            addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
            addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        }
        context.registerReceiver(receiver, intentFilter)
    }

    /**
     * Unregisters the previously registered broadcast receiver.
     *
     * This method stops the `BroadcastReceiver` from listening to Bluetooth-related events.
     * It is essential to call this method when the broadcast receiver is no longer needed
     * or when the associated component (e.g., an Activity or Fragment) is destroyed
     * to avoid memory leaks.
     *
     * @param receiver The `BroadcastReceiver` instance to unregister.
     */
    fun unregisterReceiver(receiver: BroadcastReceiver) {
        context.unregisterReceiver(receiver)
    }

    /**
     * Check if a specific permission is granted.
     * @param permission The permission to check.
     * @return True if the permission is granted, false otherwise.
     */

    /**
     * Checks and returns a list of Bluetooth-related permissions that need to be requested.
     * The function checks the required permissions based on the Android SDK version and
     * the permissions that are already granted.
     *
     * - For devices running Android 12 (API level 31) and above, it checks for the following:
     *   - `BLUETOOTH_SCAN`: Required for scanning Bluetooth devices.
     *   - `BLUETOOTH_CONNECT`: Required for connecting to Bluetooth devices.
     *
     * - For all devices, it checks for the following:
     *   - `ACCESS_FINE_LOCATION`: Required for scanning Bluetooth devices on Android versions below 12.
     *
     * The function returns a list of permissions that are not granted and need to be requested.
     *
     * @return A list of permission names (strings) that need to be requested.
     */

    private fun getBluetoothPermissionsToRequest(): Array<String> {
        val permissionList = arrayListOf<String>()
        // Check if the app needs Bluetooth permissions based on SDK version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!isPermissionGranted(Manifest.permission.BLUETOOTH_SCAN)) {
                permissionList.add(Manifest.permission.BLUETOOTH_SCAN)
            }
            if (!isPermissionGranted(Manifest.permission.BLUETOOTH_CONNECT)) {
                permissionList.add(Manifest.permission.BLUETOOTH_CONNECT)
            }
        }

        // Check if ACCESS_FINE_LOCATION permission is granted
        if (!isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        return permissionList.toArray(arrayOf<String>())
    }

    /**
     * Checks if a specific permission is granted.
     *
     * This method checks whether the given permission has been granted to the application.
     * Use this method to verify Bluetooth-related permissions before starting discovery
     * or performing Bluetooth operations.
     *
     * @param permission The permission to check (e.g., `Manifest.permission.BLUETOOTH_SCAN`).
     * @return True if the permission is granted, false otherwise.
     */
    private fun isPermissionGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }
}

