external fun require(module: String): dynamic

fun main(args: Array<String>) {
    println("Hello NodeJS!")

    val express = require("express")
    val app = express()

    val server = app.listen(8080) {
        println("Server is running on port 8080")
    }

    val socket = require("socket.io")
    val io = socket(server)

    io.on("connection") { socket ->
        println("socket ID: ${socket.id}")

        socket.on("SEND_MESSAGE") { data ->
            println("SEND_MESSAGE: ${data}")
            io.emit("RECEIVE_MESSAGE", data)
        }
    }

    app.get("/") { _, res ->
        res.type("text/plain")
        res.send("Hello World Kotlin JS!")
    }

}