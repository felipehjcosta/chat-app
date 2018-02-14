import kotlinext.js.require
import kotlinext.js.requireAll
import react.dom.render
import kotlin.browser.document

fun main(args: Array<String>) {
    requireAll(require.context("/", true, js("/\\.css$/")))
    console.log("Hello World Kotlin JS")

    render(document.getElementById("root")) {
        app()
    }
}
