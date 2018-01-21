package logo

import react.*
import react.dom.*

@JsModule("../../resources/main/react.svg")
external val reactLogo: dynamic
@JsModule("web/kotlin.svg")
external val kotlinLogo: dynamic

fun RBuilder.logo(height: Int = 100) {
    div("Logo") {
        attrs.jsStyle.height = height
        img(alt = "React logo.logo", classes = "Logo-react") {}
        img(alt = "Kotlin logo.logo", classes = "Logo-kotlin") {}
    }
}
