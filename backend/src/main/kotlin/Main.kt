external fun require(module: String): dynamic

fun main(args: Array<String>) {
    println("Hello JavaScript!")

    val express = require("express")
    val app = express()

    app.get("/", { req, res ->
        res.type("text/plain")
        res.send("Hello World Kotlin JS!")
    })

    app.listen(3000, {
        println("Listening on port 3000")
    })
}