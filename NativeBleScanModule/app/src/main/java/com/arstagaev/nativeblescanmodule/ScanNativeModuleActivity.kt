package com.arstagaev.nativeblescanmodule

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arstagaev.nativeblescanmodule.base.ItemElementsDelegate
import com.arstagaev.nativeblescanmodule.adapter.ShowWorkersAdapter
import com.arstagaev.nativeblescanmodule.ble.ConnectionEventListener
import com.arstagaev.nativeblescanmodule.ble.ConnectionManager
import timber.log.Timber


private const val ENABLE_BLUETOOTH_REQUEST_CODE = 1
private const val LOCATION_PERMISSION_REQUEST_CODE = 2

class ScanNativeModuleActivity : AppCompatActivity() {
    /*******************************************
     * Properties
     *******************************************/
    lateinit var scanButton : Button

    private val bluetoothAdapter: BluetoothAdapter by lazy {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    private val bleScanner by lazy {
        bluetoothAdapter.bluetoothLeScanner
    }
    private val scanSettings = ScanSettings.Builder()
        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
        .build()

    private var isScanning = false
        set(value) {
            field = value
            runOnUiThread { scanButton.text = if (value) "Stop Scan" else "Start Scan" }
        }

    private val scanResults = mutableListOf<ScanResult>()
    // list
    private lateinit var recycler_view : RecyclerView
    private lateinit var mainScreenLayout: RelativeLayout
    private val mAdapter = ShowWorkersAdapter()

    private val isLocationPermissionGranted
        get() = hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    /*******************************************
     * Activity function overrides
     *******************************************/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_native_scan)

        scanButton = findViewById(R.id.scan_button)

        recycler_view = findViewById(R.id.scan_results_recycler_view)

        initRecyclerview()
        refreshRecyclerview()
        scanButton.setOnClickListener {
            if (isScanning) stopBleScan() else startBleScan()
        }
    }

    override fun onResume() {
        super.onResume()
        ConnectionManager.registerListener(connectionEventListener)
        if (!bluetoothAdapter.isEnabled) {
            promptEnableBluetooth()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            ENABLE_BLUETOOTH_REQUEST_CODE -> {
                if (resultCode != Activity.RESULT_OK) {
                    promptEnableBluetooth()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.firstOrNull() == PackageManager.PERMISSION_DENIED) {
                    requestLocationPermission()
                } else {
                    startBleScan()
                }
            }
        }
    }

    /*******************************************
     * Private functions
     *******************************************/

    private fun promptEnableBluetooth() {
        if (!bluetoothAdapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, ENABLE_BLUETOOTH_REQUEST_CODE)
        }
    }

    private fun startBleScan() {
        timerOfSearch.cancel()
        timerOfSearch.start()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !isLocationPermissionGranted) {
            requestLocationPermission()
        } else {
            scanResults.clear()
            mAdapter.notifyDataSetChanged()
            bleScanner.startScan(null, scanSettings, scanCallback)
            isScanning = true
        }
    }

    private fun stopBleScan() {
        bleScanner.stopScan(scanCallback)
        isScanning = false
    }

    private fun requestLocationPermission() {
        if (isLocationPermissionGranted) {
            return
        }
        Toast.makeText(applicationContext,"Location permission required",Toast.LENGTH_LONG).show()
        requestPermission(
            Manifest.permission.ACCESS_FINE_LOCATION,
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    private fun refreshRecyclerview() {
        mAdapter.updateItems(scanResults)
    }

    private fun initRecyclerview() {



        recycler_view.adapter = mAdapter
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(false)

        mAdapter.attachDelegate(object : ItemElementsDelegate<ScanResult> {
            override fun onElementClick(model: ScanResult, view: View, clickedPosition: Int) {

                Log.d("TAG","click" + model.device.name)
                Toast.makeText(this@ScanNativeModuleActivity, "CLICKK",Toast.LENGTH_LONG).show()

            }
            //override fun onElementClick(model: WorkerInfo, view: View, clickedPosition: Int) {
            //
            //}
        })
    }

    /*******************************************
     * Callback bodies
     *******************************************/
    var timerOfSearch = object : CountDownTimer(180000,2000){
        override fun onTick(millisUntilFinished: Long) {
            refreshRecyclerview()
            Log.d("ccc","tick .size:"+scanResults.size)
            for(i in 0 until scanResults.size){

                Log.d("ccc","array of scrlt "+scanResults.get(i).device.name + "address "+scanResults.get(i).device.address)
            }

        }
        override fun onFinish() {
            stopBleScan()
        }

    }

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            val indexQuery = scanResults.indexOfFirst { it.device.address == result.device.address }
            if (indexQuery != -1) { // A scan result already exists with the same address
                //scanResults[indexQuery] = result
                ////scanResultAdapter.notifyItemChanged(indexQuery)
                //mAdapter.notifyItemRemoved(indexQuery)
            } else {
                with(result.device) {
                    Timber.i("Found BLE device! Name: ${name ?: "Unnamed"}, address: $address")
                }
                // here we may filtering and auto search
                scanResults.add(result)
                //scanResultAdapter.notifyItemInserted(scanResults.size - 1)
                //mAdapter.notifyItemInserted(scanResults.size - 1)
                //mAdapter.addItem(result)
                //refreshRecyclerview()
            }
        }

        override fun onScanFailed(errorCode: Int) {
            Timber.e("onScanFailed: code $errorCode")
        }
    }

    private val connectionEventListener by lazy {
        ConnectionEventListener().apply {
            onConnectionSetupComplete = { gatt ->
                Toast.makeText(applicationContext,"Connected!:) ",Toast.LENGTH_LONG).show()

                //Intent(this@MainActivity, BleOperationsActivity::class.java).also {
                //    it.putExtra(BluetoothDevice.EXTRA_DEVICE, gatt.device)
                //    startActivity(it)
                //}
                ConnectionManager.unregisterListener(this)
            }
            onDisconnect = {
                Toast.makeText(applicationContext,"Disconnected! ",Toast.LENGTH_LONG).show()
            }
        }
    }


}


/*******************************************
 * Extension functions
 *******************************************/

private fun Context.hasPermission(permissionType: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permissionType) ==
            PackageManager.PERMISSION_GRANTED
}

private fun Activity.requestPermission(permission: String, requestCode: Int) {
    ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
}