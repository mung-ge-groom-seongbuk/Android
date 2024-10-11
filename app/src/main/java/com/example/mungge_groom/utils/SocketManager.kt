package com.example.mungge_groom.utils

// SocketManager.kt
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

object SocketManager {
    private lateinit var mSocket: Socket

    @Synchronized
    fun setSocket() {
        try {
            mSocket = IO.socket("http://34.64.138.179:80")
        } catch (e: URISyntaxException) {
            throw RuntimeException(e)
        }
    }

    @Synchronized
    fun getSocket(): Socket {
        return mSocket
    }

    @Synchronized
    fun establishConnection() {
        mSocket.connect()
    }

    @Synchronized
    fun closeConnection() {
        mSocket.disconnect()
    }
}