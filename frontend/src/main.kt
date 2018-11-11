import com.github.felipehjcosta.chatapp.logging.Logger
import com.github.felipehjcosta.chatapp.logging.LoggerAdapter
import kotlinext.js.require
import kotlinext.js.requireAll
import react.dom.render
import kotlin.browser.document

fun main(args: Array<String>) {
    requireAll(require.context("/", true, js("/\\.css$/")))

    Logger {
        loggerAdapter = object : LoggerAdapter {
            override fun info(message: String) {
                console.log(message)
            }
        }
    }

    render(document.getElementById("root")) {
        app()
    }
}
