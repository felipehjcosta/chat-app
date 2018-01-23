package logo

import react.*
import react.dom.*

@JsModule("react.svg")
external val reactLogo: dynamic
@JsModule("kotlin.svg")
external val kotlinLogo: dynamic

fun RBuilder.logo(height: Int = 100) {
    div("Logo") {
        attrs.jsStyle.height = height
        img(alt = "React logo.logo", src = reactLogo, classes = "Logo-react") {}
        img(alt = "Kotlin logo.logo", src = kotlinLogo, classes = "Logo-kotlin") {}
    }
}
