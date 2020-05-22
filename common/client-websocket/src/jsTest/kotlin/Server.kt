@file:JsModule("mock-socket")
@file:JsNonModule

external class Server(fakeUrl: String) {
    fun on(type: String, callback: ((dynamic) -> dynamic)?)

    fun stop(callback: (() -> Unit)? = definedExternally)

    fun simulate(event: String): dynamic
}
