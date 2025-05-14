package com.example.st129_gravityfalls_maker.utils

import android.app.Service
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.os.Binder
import android.os.IBinder
import com.example.st129_gravityfalls_maker.utils.SystemUtils


class NetworkMonitorService : Service() {
    private val binder = LocalBinder()
    private var networkCallback: ConnectivityManager.NetworkCallback? = null

    override fun onCreate() {
        super.onCreate()
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onLost(network: Network) {
                super.onLost(network)
                if (SystemUtils.isConnectInternet.value != false) {
                    SystemUtils.isConnectInternet.postValue(false)
                }
            }

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                if (SystemUtils.isConnectInternet.value != true) {
                    SystemUtils.isConnectInternet.postValue(true)
                }
            }
        }
        connectivityManager.registerDefaultNetworkCallback(networkCallback!!)
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    inner class LocalBinder : Binder() {
        fun getService(): NetworkMonitorService {
            return this@NetworkMonitorService
        }
    }

}