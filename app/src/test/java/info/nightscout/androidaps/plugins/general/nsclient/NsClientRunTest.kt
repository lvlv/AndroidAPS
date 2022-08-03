package info.nightscout.androidaps.plugins.general.nsclient

import info.nightscout.androidaps.TestBase
import info.nightscout.androidaps.plugins.general.nsclient.services.NSClientService
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.junit.Test
import java.util.logging.Level
import java.util.logging.Logger

class NsClientRunTest : TestBase() {

    private var connectCounter = 0
    private var socket: Socket? = null

    private val onConnect = Emitter.Listener {
        connectCounter++
        val socketId = socket?.id() ?: "NULL"
        System.out.println("connect #$connectCounter event. ID: $socketId")
    }

    @Test fun run() {
        System.out.println("hello")
        val opt = IO.Options()
        opt.forceNew = true
        opt.reconnection = true
        var nsURL = "http://8.8.8.8:10800/"
        val logger = Logger.getLogger(Socket::class.java.name)
        logger.level = Level.FINE
        System.out.println("try connecting to $nsURL")
        System.out.println("name " + logger.name)
        System.out.println("level " + logger.level)
        System.out.println("java class " + logger.javaClass.name)
        logger.info("info try connecting to $nsURL")
        logger.fine("fine try connecting to $nsURL")
        socket = IO.socket(nsURL, opt).also { socket ->
            socket.on(Socket.EVENT_CONNECT, onConnect)
            System.out.println("NSCLIENT do connect")
            socket.connect()
            System.out.println("NSCLIENT connected")
        }
        for (i in 1..10) {
            if (socket?.connected() == true) break
            Thread.sleep(500)
        }
        System.out.println(socket?.connected());
    }
}
