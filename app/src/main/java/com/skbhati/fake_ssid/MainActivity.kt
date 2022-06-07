package com.skbhati.fake_ssid

import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.SupplicantState
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.skbhati.fake_ssid.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        const val PERMISSION_CODE_ACCEPTED = 1
        const val PERMISSION_CODE_NOT_AVAILABLE = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        when(requestLocationPermission()){
            MainActivity.PERMISSION_CODE_ACCEPTED -> getWifiSSID()
        }
    }

    fun requestLocationPermission(): Int {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                // request permission
                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    MainActivity.PERMISSION_CODE_ACCEPTED)
            }
        } else {
            // already granted
            return MainActivity.PERMISSION_CODE_ACCEPTED
        }

        // not available
        return MainActivity.PERMISSION_CODE_NOT_AVAILABLE
    }

    fun getWifiSSID() {
        val mWifiManager: WifiManager = (this.getApplicationContext().getSystemService(Context.WIFI_SERVICE) as WifiManager)!!
        val info: WifiInfo = mWifiManager.connectionInfo

        if (info.getSupplicantState() == SupplicantState.COMPLETED) {
            val ssid: String = info.getSSID()
            Log.d("wifi name", ssid)
        } else {
            Log.d("wifi name", "could not obtain the wifi name")
        }
    }
}