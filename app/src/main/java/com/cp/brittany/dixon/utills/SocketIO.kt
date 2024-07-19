package com.cp.brittany.dixon.utills

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject
import javax.inject.Inject

class SocketIO @Inject constructor() {
    companion object {
        private const val socketBaseUrl = Constants.SOCKET_URL
        private var id: String? = null
        private var mSocket: Socket? = null
        private val opts = IO.Options()
    }

    fun getSocketId(): String? {
        return id
    }

    init {
        if (mSocket == null) {
            mSocket = IO.socket(socketBaseUrl, opts)
        }
        if (mSocket?.connected() == false) {
            mSocket?.connect()
        }
        mSocket?.on(Socket.EVENT_CONNECT, Emitter.Listener {
            id = mSocket?.id()
            Log.d("socket_Id", id ?: "-1")
            Log.d("connected successfully", "True")
        })
        mSocket?.on(Socket.EVENT_CONNECT_ERROR, Emitter.Listener {
            // ...
            Log.d("connection error", "Error " + it.size)

        })
    }

    fun connect() {
        mSocket?.connect()
    }

    fun emit(name: String, data: JSONObject) {
        if (mSocket?.connected() == false) {
            Log.d("Socket em connected -> ", "${mSocket?.connected()}")
            mSocket?.connect()
        } else {
            Log.d("Socket em connected : ", "${mSocket?.connected()}")
        }
        mSocket?.emit(name, data)

    }

    fun isConnected() = mSocket?.connected()

    fun offSocketEvent(event: String) {
        mSocket?.off(event)
    }

    fun disconnect() {
        mSocket?.off()
        mSocket?.close()
        mSocket?.disconnect()
        Log.e("Socket", "Disconnected")
    }
}