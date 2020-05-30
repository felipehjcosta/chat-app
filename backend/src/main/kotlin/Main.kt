import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import org.slf4j.event.Level

fun Application.main() {
    install(CallLogging) {
        level = Level.INFO
    }
    installChat()
    routing {
        get("/") { call.respondText("Hello World server!", ContentType.Text.Plain) }
    }
}
