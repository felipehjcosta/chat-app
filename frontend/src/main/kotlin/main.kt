import com.github.felipehjcosta.chatapp.client.ChatInjector
import com.github.felipehjcosta.chatapp.logging.Logger
import com.github.felipehjcosta.chatapp.logging.LoggerAdapter
import kotlinext.js.require
import kotlinext.js.requireAll
import react.dom.render
import kotlinx.browser.document

fun main() {
    requireAll(require.context("/", true, js("/\\.css$/")))

    Logger {
        loggerAdapter = object : LoggerAdapter {
            override fun info(message: String) {
                console.log(message)
            }
        }
    }

    ChatInjector {
        baseUrl = "ws://localhost:8089/chat"
    }

    render(document.getElementById("root")) {
        app()
    }
}
