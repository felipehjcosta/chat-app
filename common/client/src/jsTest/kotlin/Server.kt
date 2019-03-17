@file:JsModule("mock-socket")

external class Server(fakeUrl: String) {
    fun on(type: String, callback: ((dynamic) -> dynamic)?)

    fun stop(callback: (() -> Unit)? = definedExternally)

    fun simulate(event: String): dynamic
}
